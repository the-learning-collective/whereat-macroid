package org.tlc.whereat.services

import java.io.InputStream

import android.util.Log
import io.taig.communicator.internal.response.Plain
import io.taig.communicator.internal.result.Parser
import macroid.AppContext
import org.tlc.whereat.model.{Loc, Conversions, ApiIntersectionFormatJson, ApiIntersection}
import org.tlc.whereat.msg.{IntersectionResponse, IntersectionRequest}
import org.tlc.whereat.net.NetUtil
import play.api.libs.json.Json

import scala.concurrent.Future

/**
 * Author: @aguestuser
 * Date: 4/24/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */

object IntersectionApiJsonParser extends Parser[ApiIntersection] with ApiIntersectionFormatJson {
  override def parse(res: Plain, stream: InputStream): ApiIntersection =
    (Json.parse(scala.io.Source.fromInputStream(stream).mkString) \ "intersection")
      .as[ApiIntersection]
}

trait IntersectionService extends NetUtil with Conversions {

  def geocodeLocation(l: Loc)(implicit appContextProvider: AppContext): Future[IntersectionResponse] =
    requestIntersection(toIntersectionRequest(l))

  def parseGeocoding(res: IntersectionResponse): String = res.maybe match {
    case Some(i) ⇒ i.toString
    case None ⇒ "Location not available" }

  def requestIntersection(req: IntersectionRequest)(implicit appContextProvider: AppContext): Future[IntersectionResponse] = {

    import scala.concurrent.ExecutionContext.Implicits.global
    implicit val parser = IntersectionApiJsonParser
    val url = "http://api.geonames.org/findNearestIntersectionJSON"
    log(Log.INFO, "WHEREAT", "running getIntersection")

    reqJson[ApiIntersection](IntersectionRequest.urlWithQuery(url,req)).transform(
      res ⇒ IntersectionResponse(Some(toIntersection(res))),
      throwable ⇒ throwable ) }

  def log(level: Int, tag: String, message: String): Int = {
    level match {
      case Log.VERBOSE => Log.v(tag, message)
      case Log.DEBUG => Log.d(tag, message)
      case Log.INFO => Log.i(tag, message)
      case Log.WARN => Log.w(tag, message)
      case Log.ERROR => Log.e(tag, message)
      case Log.ASSERT => Log.wtf(tag, message)
    }
  }


}

object IntersectionService extends IntersectionService
