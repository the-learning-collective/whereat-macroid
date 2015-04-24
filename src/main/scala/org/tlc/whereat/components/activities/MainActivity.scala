package org.tlc.whereat.components.activities

import android.app.Activity
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.{Button, LinearLayout, TextView}
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.{ConnectionCallbacks, OnConnectionFailedListener}
import com.google.android.gms.location.LocationServices
import macroid.Contexts
import macroid.FullDsl._
import org.tlc.whereat.ui.tweaks.MainTweaks

import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Author: @aguestuser
 * Date: 4/22/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */


class MainActivity extends Activity with Contexts[Activity] with ConnectionCallbacks with OnConnectionFailedListener { // include implicit contexts

  var apiClient: Option[GoogleApiClient] = None
  var locView: Option[TextView] = slot[TextView]
  var connectionPromise = Promise[Unit]()

  override protected def onCreate(savedInstanceState: Bundle): Unit = {
    super .onCreate(savedInstanceState)

    apiClient = buildClient

    setContentView {
      getUi {
        l[LinearLayout](
          w[Button] <~
            text("Get Location") <~
            On.click { locView <~ getLocation.map { l ⇒ text(parseLocation(l)) + show } },
          w[TextView] <~
            wire(locView) <~ hide
        ) <~ MainTweaks.orient } } }

  override protected def onStart(): Unit = {
    super.onStart()
    apiClient foreach { _.connect } }

  override protected def onStop(): Unit = {
    super.onStop()
    apiClient foreach { cl ⇒ if(cl.isConnected) cl.disconnect() } }

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
    Log.i("whereat", "Connection failed: ConnectionResult.getErrorCode() = " + res.getErrorCode) }

  override def onConnectionSuspended(cause: Int) {
    connectionPromise = Promise()
    Log.i("whereat", "Connection suspended")
    apiClient foreach { _.connect } }

  private def getLocation: Future[Option[Location]] =
    connectionPromise.future map { _ ⇒
      apiClient flatMap { cl ⇒
        Option(LocationServices.FusedLocationApi.getLastLocation(cl)) } }

  private def parseLocation(l: Option[Location]): String = l match {
    case Some(ll) ⇒ s"Lat: ${ll.getLatitude}, Lon: ${ll.getLongitude}"
    case None ⇒ "Location not available" }

}

