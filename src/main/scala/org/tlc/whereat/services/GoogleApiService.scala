package org.tlc.whereat.services

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.{OnConnectionFailedListener, ConnectionCallbacks}
import com.google.android.gms.location.LocationServices

import scala.concurrent.{Future, Promise}

/**
 * Author: @aguestuser
 * Date: 5/10/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */

trait GoogleApiService
  extends ConnectionCallbacks
  with OnConnectionFailedListener {

  import scala.concurrent.ExecutionContext.Implicits.global

  var gApiClient: Option[GoogleApiClient] = None
  var connectionPromise: Promise[Unit] = Promise()

  protected def getLocation: Future[Option[Location]] =
    connectionPromise.future map { _ ⇒
      gApiClient flatMap { cl ⇒
        Option(LocationServices.FusedLocationApi.getLastLocation(cl)) } }

  protected def buildGoogleApiClient(ctx: Context): Option[GoogleApiClient] = {
    connectionPromise = Promise()
    synchronized {
      Some(
        new GoogleApiClient.Builder(ctx)
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
    gApiClient foreach { _.connect } }
}
