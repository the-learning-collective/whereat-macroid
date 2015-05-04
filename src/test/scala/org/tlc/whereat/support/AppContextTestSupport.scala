package org.tlc.whereat.support

import com.fortysevendeg.macroid.extras.AppContextProvider
import macroid.AppContext
import org.specs2.mock.Mockito
import org.specs2.specification.Scope

/**
 * Author: @aguestuser
 * Date: 5/3/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */

trait AppContextTestSupport
  extends Mockito
  with AppContextProvider
  with TestConfig
  with Scope {

  implicit val appContextProvider: AppContext = mock[AppContext]

  appContextProvider.get returns mockContext

}
