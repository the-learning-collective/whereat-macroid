package org.tlc.whereat.ui.tweaks

import macroid.AppContext
import macroid.FullDsl._
import macroid.contrib.TextTweaks

/**
 * Author: @aguestuser
 * Date: 4/22/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */
// tweaks are composable!
object MainTweaks
{

  def greeting(greeting: String)(implicit appCtx: AppContext) =
    TextTweaks.large +
      text(greeting) +
      hide

  def orient(implicit appCtx: AppContext) =
    landscape ? horizontal | vertical


}
