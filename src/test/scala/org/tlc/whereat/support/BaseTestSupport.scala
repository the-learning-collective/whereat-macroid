package org.tlc.whereat.support

import org.specs2.execute.{AsResult, Result}
import org.specs2.specification.{Scope, Around}

/**
 * Author: @aguestuser
 * Date: 5/3/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */

trait BaseTestSupport extends Around with Scope {

  override def around[T: AsResult](t: => T): Result = AsResult.effectively(t)

}
