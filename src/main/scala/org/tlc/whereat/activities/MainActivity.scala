package org.tlc.whereat.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.{Button, LinearLayout, TextView}
import macroid.FullDsl._
import macroid.{AppContext, Contexts}
import org.tlc.whereat.model.Conversions
import org.tlc.whereat.services.{GoogleApiService, IntersectionService}
import org.tlc.whereat.ui.tweaks.MainTweaks

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Author: @aguestuser
 * Date: 4/22/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */


class MainActivity extends Activity
  with Contexts[Activity]
  with Conversions
  with GoogleApiService
  with IntersectionService {

  implicit lazy val appContextProvider: AppContext = activityAppContext
  var locView: Option[TextView] = slot[TextView]

  // Activy UI & life cycle methods

  override protected def onCreate(savedInstanceState: Bundle): Unit = {
    super .onCreate(savedInstanceState)

    gApiClient = buildGoogleApiClient(this)

    setContentView {
      getUi {
        l[LinearLayout](
          w[Button] <~
            text("Get Location") <~
            On.click { locView <~ getIntersection.map { i ⇒ text(i) + show } },
          w[TextView] <~
            wire(locView) <~ hide
        ) <~ MainTweaks.orient } } }

  override protected def onStart(): Unit = {
    super.onStart()
    gApiClient foreach { _.connect } }

  override protected def onStop(): Unit = {
    super.onStop()
    gApiClient foreach { cl ⇒ if(cl.isConnected) cl.disconnect() } }

  // Location & Geocoder API calls

  def getIntersection: Future[String] =
    getLocation flatMap {
      case Some(l) ⇒
        Log.i("WHEREAT", s"Location retrieved: $l")
        geocodeLocation(toLoc(l)) map parseGeocoding
      case None ⇒ Future.successful ("Location not available") }

  // contact picking

//  val REQUEST_SELECT_CONTACT = 1

//  def selectContact(): Unit = {
//    val intent = new Intent(Intent.ACTION_PICK)
//    intent.setType(ContactsContract.Contacts.CONTENT_TYPE)
//    if (intent.resolveActivity(getPackageManager) != null)
//      startActivityForResult(intent, REQUEST_SELECT_CONTACT)
//  }
//
//  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
//    if (requestCode == REQUEST_SELECT_CONTACT && resultCode == 1)
//      val contactUri = data.getData
//  }

}

