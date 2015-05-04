package org.tlc.whereat.model

import play.api.libs.json.Json

/**
 * Author: @aguestuser
 * Date: 4/25/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */

class ApiIntersectionTest extends org.specs2.mutable.Specification with ApiIntersectionFormatJson {

  lazy val apiIntersection = ApiIntersection(
    "40.72084", //lat
    "-74.000661", //lng
    "0.03", //distance
    "Broadway", //street1
    "Grand St", //street2
    "32", //street1Bearing
    "124", //street2Bearing
    "New York", //placename
    "New York", //admin1name
    "New York", //admin2name
    "NY", //adminCode1
    "10013", // postalcode
    "US", // countryCode
    "S1400", // mtfcc1
    "S1400" //mtfcc2
  )

  lazy val apiIntersectionJsonString = """{
    "intersection": {
      "lat": "40.72084",
      "lng": "-74.000661",
      "distance": "0.03",
      "street1": "Broadway",
      "street2": "Grand St",
      "street1Bearing": "32",
      "street2Bearing": "124",
      "placename": "New York",
      "adminName1": "New York",
      "adminName2": "New York",
      "adminCode1": "NY",
      "postalcode": "10013",
      "countryCode": "US",
      "mtfcc1": "S1400",
      "mtfcc2": "S1400"
    }
  }"""

  lazy val apiIntersectionJsonString2 = """{
    "intersection": {
      "lat": "40.72084",
      "lng": "-74.000661",
      "distance": "0.03",
      "street2": "Grand St",
      "street1": "Broadway",
      "street1Bearing": "32",
      "street2Bearing": "124",
      "adminName1": "New York",
      "placename": "New York",
      "adminName2": "New York",
      "adminCode1": "NY",
      "postalcode": "10013",
      "countryCode": "US",
      "mtfcc1": "S1400",
      "mtfcc2": "S1400"
    }
  }"""

  "ApiIntersection" should {

    "unmarshal from JSON string regardless of order of keys" >> {

      (Json.parse(apiIntersectionJsonString) \ "intersection").as[ApiIntersection] === apiIntersection
      (Json.parse(apiIntersectionJsonString2) \ "intersection").as[ApiIntersection] === apiIntersection
    }
  }
}
