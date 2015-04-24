package org.tlc.whereat.components.activities

import android.app.Activity
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

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise


/**
 * Author: @aguestuser
 * Date: 4/22/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */


class MainActivity extends Activity with Contexts[Activity] with ConnectionCallbacks with OnConnectionFailedListener { // include implicit contexts

  var context = activityActivityContext
  var apiClient: Option[GoogleApiClient] = None
  var locView: Option[TextView] = slot[TextView]
  val locText: Promise[String] = Promise[String]()

  override protected def onCreate(savedInstanceState: Bundle): Unit = {
    super .onCreate(savedInstanceState)

    apiClient = buildClient

    setContentView {
      getUi {
        l[LinearLayout](
          w[Button] <~
            text("Get Location") <~
            On.click {
              locView <~ show
            },
          w[TextView] <~
            wire(locView) <~
            locText.future.map(text)
        ) <~ MainTweaks.orient } } }

  override protected def onStart(): Unit = {
    super.onStart()
    apiClient.get.connect() }

  override protected def onStop(): Unit = {
    super.onStop()
    if (apiClient.get.isConnected) apiClient.get.disconnect() }

  protected def buildClient: Option[GoogleApiClient] = {
    Log.i("whereat", "running buildClient")
    synchronized {
      Some(
        new GoogleApiClient.Builder(this)
          .addConnectionCallbacks(this)
          .addOnConnectionFailedListener(this)
          .addApi(LocationServices.API)
          .build()) } }

  override def onConnected(connectionHint: Bundle): Unit = {
    Log.i("whereat", "API connected")
    Log.i("whereat", s"${apiClient.get}")
    val loc = LocationServices.FusedLocationApi.getLastLocation(apiClient.get)
    Log.i("whereat", s"Location: $loc")
    if (loc != null) locText.success { s"Lat: ${loc.getLatitude}, Lon: ${loc.getLongitude}"}
    else locText.success { "No location detected!" } }

  override def onConnectionFailed(res: ConnectionResult): Unit = {
    Log.i("whereat", "Connection failed: ConnectionResult.getErrorCode() = " + res.getErrorCode) }

  override def onConnectionSuspended(cause: Int) {
    Log.i("whereat", "Connection suspended")
    apiClient.get.connect() }

}

