package org.tlc.whereat.support

import org.specs2.matcher.{ExceptionMatchers, MustMatchers, ThrownExpectations}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
 * Author: @aguestuser
 * Date: 5/3/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */

object AsyncUtils {

  implicit class RichAsyncResponseMatcher[T](futureResult: Future[T])
    extends ThrownExpectations
    with ExceptionMatchers
    with MustMatchers {

    def *===[U](expected: => U) = Await.result(futureResult, Duration.Inf) === expected

  }

}
