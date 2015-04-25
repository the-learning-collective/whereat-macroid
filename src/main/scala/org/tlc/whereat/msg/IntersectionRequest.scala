package org.tlc.whereat.msg

/**
 * Author: @aguestuser
 * Date: 4/24/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */

case class IntersectionRequest(lat: Double, lng: Double)

object IntersectionRequest {
  def urlWithQuery(url: String, req: IntersectionRequest): String =
    s"$url?lat=${req.lat}&lng=${req.lng}"
}


