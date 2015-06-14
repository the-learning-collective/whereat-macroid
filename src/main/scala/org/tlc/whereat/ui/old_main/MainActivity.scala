package org.tlc.whereat.ui.old_main

import android.app.Activity
import android.content.{ComponentName, Intent}
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds
import android.telephony.SmsManager
import android.widget.{Button, LinearLayout, TextView}
import macroid.FullDsl._
import macroid._
import macroid.contrib.TextTweaks
import org.tlc.whereat.model.Conversions
import org.tlc.whereat.msg.Logger
import org.tlc.whereat.services.{GoogleApiService, IntersectionService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

/**
 * Author: @aguestuser
 * Date: 4/22/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */


class MainActivity extends Activity
  with Contexts[Activity]
  with GoogleApiService
  with IntersectionService
  with Conversions
  with Logger {

  implicit lazy val appContextProvider: AppContext = activityAppContext

  var intersectionTextView = slot[TextView]
  var shareViaCellyButton = slot[Button]
  var shareViaPeoplesMicButton = slot[Button]
  var phoneNumberTextView = slot[TextView]
  var sendTextButton = slot[Button]
  var successMessage = slot[TextView]
  var resetButton = slot[Button]

  val slots = List(
    intersectionTextView,
    shareViaCellyButton,
    shareViaPeoplesMicButton,
    phoneNumberTextView,
    sendTextButton,
    successMessage,
    resetButton
  )
  // Activy UI & life cycle methods

  override protected def onCreate(savedInstanceState: Bundle): Unit = {
    super .onCreate(savedInstanceState)

    gApiClient = buildGoogleApiClient(this)

    setContentView {
      getUi {
        l[LinearLayout](
          w[Button] <~
            largeText("Get Location") <~
            On.click {
              (intersectionTextView <~~ getIntersection.map { fadeInText } ) ~~
                ((shareViaPeoplesMicButton <~~ fadeIn(100)) ~
                 (shareViaCellyButton <~~ fadeIn(100))) },
          w[TextView] <~
            wire(intersectionTextView) <~ hide,
          w[Button] <~
            wire(shareViaPeoplesMicButton) <~ hide <~ largeText("Share via Text") <~
            On.click { 
              (phoneNumberTextView <~~ getPhoneNumber.map { fadeInText }) ~~
                ((shareViaCellyButton <~ hide) ~
                 (sendTextButton <~~ fadeIn(100))) },
          w[Button] <~
            wire(shareViaCellyButton) <~ hide <~ largeText("Share via Celly") <~
            On.click {
              (successMessage <~~ sendSmsToCelly.map( _ ⇒ show)) ~~
                (resetButton <~ show) },
          w[TextView] <~
            wire(phoneNumberTextView) <~ hide, 
          w[Button] <~
            wire(sendTextButton) <~ hide <~ largeText("Send Text") <~
            On.click {
              (successMessage <~~ sendSmsToFriend.map(_ ⇒ show)) ~
                (resetButton <~ show)
            },
          w[TextView] <~
            wire(successMessage) <~ hide <~ largeText("Success!"),
          w[Button] <~
            wire(resetButton) <~ hide <~ largeText("Reset") <~
            On.click { reset }
        ) <~ MainTweaks.orient } } }


  def reset =
//    slots <~ hide TODO: why won't the less verbose version work?
    (intersectionTextView <~ hide) ~
    (shareViaPeoplesMicButton <~ hide) ~
    (shareViaCellyButton <~ hide) ~
    (phoneNumberTextView <~ hide) ~
    (sendTextButton <~ hide) ~
    (successMessage <~ hide) ~
    (resetButton <~ hide)

  def fadeInText(str: String): Snail[TextView] = largeText(str) ++ fadeIn(100)
  def showText(str: String): Tweak[TextView] = largeText(str) + show
  def largeText(str: String): Tweak[TextView] = text(str) + TextTweaks.large

  override protected def onStart(): Unit = {
    super.onStart()
    gApiClient foreach { _.connect } }

  override protected def onStop(): Unit = {
    super.onStop()
    gApiClient foreach { cl ⇒ if(cl.isConnected) cl.disconnect() } }

  // location sharing

  def getIntersection: Future[String] =
    getLocation flatMap {
      case None ⇒ Future.successful("Location not available") //Future.successful ("Location not available")
      case Some(l) ⇒ geocodeLocation(toLoc(l)) map parseGeocoding }

  // contact intent passing
  // TODO extract this to a trait!

  var phoneNumberPromise: Promise[String] = Promise()
  val REQUEST_SELECT_PHONE_NUMBER = 1
  val RESULT_OK = -1

  def getPhoneNumber: Future[String] = {
    val intent = new Intent(Intent.ACTION_PICK).setType(CommonDataKinds.Phone.CONTENT_TYPE)
    resolve(intent) foreach  { _ ⇒ startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER) }
    phoneNumberPromise.future }

  protected override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    if (requestCode == REQUEST_SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
      val (uri, projection) = (data.getData, Array(CommonDataKinds.Phone.NUMBER))
      val cursor = getContentResolver.query(uri, projection, null, null, null)
      if (cursor != null && cursor.moveToFirst()) {
        val numberIndex = cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER)
        phoneNumberPromise.success { cursor.getString(numberIndex) } } } }

  private def resolve(intent: Intent): Option[ComponentName] =
    Option(intent.resolveActivity(getPackageManager))

  // sms intent passing
  // TODO extract this to a trait!

  def sendSmsToFriend: Future[Unit] = {
    phoneNumberPromise.future flatMap { recipient ⇒
      intersectionPromise.future flatMap { msg ⇒
        sendSms(recipient, msg)
        resetPromises; Future.successful(()) } } }

  def sendSmsToCelly: Future[Unit] = {
    intersectionPromise.future flatMap { msg ⇒
      sendSms("23559", s"@teeellsee $msg")
      resetPromises; Future.successful(()) } }

  def sendSms(recipient: String, msg: String): Unit =
    SmsManager.getDefault.sendTextMessage(recipient, null, msg, null, null)

  def resetPromises: Unit = {
    phoneNumberPromise = Promise()
    intersectionPromise = Promise() }

}

