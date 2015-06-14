package org.tlc.whereat.ui.components

import android.content.Context
import android.util.AttributeSet
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import macroid.{AppContext, Tweak}
import org.tlc.whereat.R
import org.tlc.whereat.ui.components.CircleView._

/**
 * Author: @aguestuser
 * Date: 6/13/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */

class GoButtonView (ctx: Context, attr: AttributeSet, defStyleAttr: Int)
  extends CircleView(ctx, attr, defStyleAttr) {
}

object GoButtonView {



  type GBV = GoButtonView

  def toggle(on:Int, off:Int) = Tweak[GBV] { gbv ⇒
    if (gbv.getColor == on) cvColorChange(off) else cvColorChange(on)
  }
}