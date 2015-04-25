package org.tlc.whereat.model

import play.api.libs.json.Json

/**
 * Author: @aguestuser
 * Date: 4/24/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */

case class Intersection(street1: String, street2: String) {
  override def toString = s"$street1 at $street2"
}

object Intersection {

//  implicit val formatIntersection: Format[Intersection] =
//    ((JsPath \ "street1").format[String] and
//      (JsPath \ "street2").format[String]
//      )(Intersection.apply, unlift(Intersection.unapply))

  implicit val readsIntersection =  Json.reads[Intersection]

}
