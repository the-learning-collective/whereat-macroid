package org.tlc.whereat.activities

import android.app.Activity
import android.content.{ComponentName, Intent}
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds
import android.telephony.SmsManager
import android.widget.{Button, LinearLayout, TextView}
import macroid.FullDsl._
import macroid._
import org.tlc.whereat.model.Conversions
import org.tlc.whereat.msg.Logger
import org.tlc.whereat.services.{GoogleApiService, IntersectionService}
import org.tlc.whereat.ui.tweaks.MainTweaks

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
  var getContactsButton = slot[Button]
  var phoneNumberTextView = slot[TextView]
  var shareLocationButton = slot[Button]
  var successMessage = slot[TextView]

  // Activy UI & life cycle methods

  override protected def onCreate(savedInstanceState: Bundle): Unit = {
    super .onCreate(savedInstanceState)

    gApiClient = buildGoogleApiClient(this)

    setContentView {
      getUi {
        l[LinearLayout](
          w[Button] <~
            text("Get Location") <~
            On.click {
              (intersectionTextView <~~ getIntersection.map { fadeInText }) ~~
              (getContactsButton <~~ fadeIn(100))
            },
          w[TextView] <~
            wire(intersectionTextView) <~ hide,
          w[Button] <~
            wire(getContactsButton) <~ hide <~ text("Get Contacts") <~
            On.click { 
              (phoneNumberTextView <~~ getPhoneNumber.map { fadeInText }) ~~
              (shareLocationButton <~~ fadeIn(100))
            },
          w[TextView] <~
            wire(phoneNumberTextView) <~ hide, 
          w[Button] <~
            wire(shareLocationButton) <~ hide <~ text("Share Location") <~
            On.click {
              successMessage <~~ sendSms.map(_ ⇒ show)
            },
          w[TextView] <~
            wire(successMessage) <~ hide <~ text("Success!")
        ) <~ MainTweaks.orient } } }

  def fadeInText(str: String): Snail[TextView] = text(str) ++ fadeIn(100)
  def showText(str: String): Tweak[TextView] = text(str) + show

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

  // sms intent passing
  // TODO implement this and extract it to a trait!

  def sendSms: Future[Unit] = {
    phoneNumberPromise.future flatMap { recipient ⇒
      intersectionPromise.future flatMap { msg ⇒
        SmsManager.getDefault.sendTextMessage(recipient, null, msg, null, null)
        Future.successful(()) } } }

  def resolve(intent: Intent): Option[ComponentName] =
    Option(intent.resolveActivity(getPackageManager))
}

