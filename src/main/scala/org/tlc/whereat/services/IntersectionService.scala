package org.tlc.whereat.services

import java.io.InputStream

import io.taig.communicator.internal.response.Plain
import io.taig.communicator.internal.result.Parser
import macroid.AppContext
import org.tlc.whereat.model.{ApiIntersection, ApiIntersectionFormatJson, Conversions, Loc}
import org.tlc.whereat.msg.{IntersectionRequest, IntersectionResponse, Logger}
import org.tlc.whereat.net.NetUtil
import play.api.libs.json.Json

import scala.concurrent.{Promise, Future}

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

trait IntersectionService
  extends NetUtil
  with Logger
  with Conversions {

  var intersectionPromise = Promise[String]()

  def geocodeLocation(l: Loc)(implicit appContextProvider: AppContext): Future[IntersectionResponse] =
    requestGeocode(toIntersectionRequest(l))

  def parseGeocoding(res: IntersectionResponse): String = res.maybe match {
    case None ⇒ "Location not available"
    case Some(i) ⇒
      val res = i.toString()
      intersectionPromise.success { res }
      res }

  def requestGeocode(req: IntersectionRequest)(implicit appContextProvider: AppContext): Future[IntersectionResponse] = {

    import scala.concurrent.ExecutionContext.Implicits.global
    implicit val parser = IntersectionApiJsonParser
    val url = "http://api.geonames.org/findNearestIntersectionJSON"

    reqJson[ApiIntersection](IntersectionRequest.urlWithQuery(url,req)).transform(
      res ⇒ IntersectionResponse(Some(toIntersection(res))),
      throwable ⇒ throwable ) }

}

object IntersectionService extends IntersectionService
