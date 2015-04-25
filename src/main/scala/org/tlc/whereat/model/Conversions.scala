package org.tlc.whereat.model

import org.tlc.whereat.msg.IntersectionRequest

/**
 * Author: @aguestuser
 * Date: 4/24/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */

trait Conversions {

  def toIntersection(ai: ApiIntersection) = Intersection(ai.street1, ai.street2)
  def toIntersectionRequest(l: Loc) = IntersectionRequest(l.lat,l.lng)
  def toLoc(l: android.location.Location) = org.tlc.whereat.model.Loc(l.getLatitude, l.getLongitude)

}
