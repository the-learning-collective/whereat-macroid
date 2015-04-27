package org.tlc.whereat.activities

import android.app.Activity
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.{Button, LinearLayout, TextView}
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.{ConnectionCallbacks, OnConnectionFailedListener}
import com.google.android.gms.location.LocationServices
import macroid.{AppContext, Contexts}
import macroid.FullDsl._
import org.tlc.whereat.model.{Conversions, Loc}
import org.tlc.whereat.msg.IntersectionResponse
import org.tlc.whereat.services.IntersectionService
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
  with ConnectionCallbacks
  with OnConnectionFailedListener
  with Conversions {

  implicit lazy val appContextProvider: AppContext = activityAppContext
  var apiClient: Option[GoogleApiClient] = None
  var locView: Option[TextView] = slot[TextView]
  var connectionPromise = Promise[Unit]()
  val TAG = "whereat"

  // Activy UI & life cycle methods

  override protected def onCreate(savedInstanceState: Bundle): Unit = {
    super .onCreate(savedInstanceState)

    apiClient = buildClient

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
    apiClient foreach { _.connect } }

  override protected def onStop(): Unit = {
    super.onStop()
    apiClient foreach { cl ⇒ if(cl.isConnected) cl.disconnect() } }

  // Location API Client builder & callbacks

  protected def buildClient: Option[GoogleApiClient] = {
    connectionPromise = Promise()
    synchronized {
      Some(
        new GoogleApiClient.Builder(this)
          .addConnectionCallbacks(this)
          .addOnConnectionFailedListener(this)
          .addApi(LocationServices.API)
          .build()) } }

  override def onConnected(connectionHint: Bundle): Unit =
    connectionPromise.trySuccess(())

  override def onConnectionFailed(res: ConnectionResult): Unit = {
    connectionPromise = Promise()
    Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + res.getErrorCode) }

  override def onConnectionSuspended(cause: Int) {
    connectionPromise = Promise()
    Log.i(TAG, "Connection suspended")
    apiClient foreach { _.connect } }

  // Location & Geocoder API calls

  def getIntersection: Future[String] =
    getLocation flatMap {
      case Some(l) ⇒
        Log.i(TAG, s"Location retrieved: $l")
        geocodeLocation(toLoc(l)) map parseGeocoding
      case None ⇒ Future.successful ("Location not available") }

  private def getLocation: Future[Option[Location]] =
    connectionPromise.future map { _ ⇒
      apiClient flatMap { cl ⇒
        Option(LocationServices.FusedLocationApi.getLastLocation(cl)) } }

  def geocodeLocation(l: Loc): Future[IntersectionResponse] =
    IntersectionService.getIntersection(toIntersectionRequest(l)) //TODO do i need to pass an AppContext here?

  def parseGeocoding(res: IntersectionResponse): String = res.maybe match {
    case Some(i) ⇒ i.toString
    case None ⇒ "Location not available" }

}

