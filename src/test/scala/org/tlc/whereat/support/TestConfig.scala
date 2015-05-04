package org.tlc.whereat.support

import android.content.Context
import org.specs2.mock.Mockito

/**
 * Author: @aguestuser
 * Date: 5/3/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */


trait TestConfig extends Mockito {

  val mockContext = mock[Context]

}
