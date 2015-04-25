package org.tlc.whereat.model

import play.api.libs.json.Json

/**
 * Author: @aguestuser
 * Date: 4/24/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */

case class ApiIntersection(
                            lat: Double,
                            lng: Double,
                            distance: Double,
                            street1: String,
                            street2: String,
                            street1Bearing: Int,
                            street2Bearing: String,
                            placename: String,
                            adminCode1: String,
                            adminName1: String,
                            adminName2: String,
                            postalcode: Int,
                            countryCode: String,
                            mtfcc1: String,
                            mtfcc2: String
                          )

trait ApiIntersectionFormatJson {
//  implicit val formatApiIntersection: Format[ApiIntersection] =
//    ((JsPath \ "lat").format[Double] and
//      (JsPath \ "lng").format[Double] and
//      (JsPath \ "distance").format[Double] and
//      (JsPath \ "street1").format[String] and
//      (JsPath \ "street2").format[String] and
//      (JsPath \ "street1Bearing").format[Int] and
//      (JsPath \ "street2Bearing").format[String] and
//      (JsPath \ "placename").format[String] and
//      (JsPath \ "adminCode1").format[String] and
//      (JsPath \ "adminName1").format[String] and
//      (JsPath \ "adminName2").format[String] and
//      (JsPath \ "postalcode").format[Int] and
//      (JsPath \ "countryCode").format[String] and
//      (JsPath \ "mtfcc1").format[String] and
//      (JsPath \ "mtfcc2").format[String]
//    )(ApiIntersection.apply, unlift(ApiIntersection.unapply))

  implicit val formatApiIntersection = Json.format[ApiIntersection]
}