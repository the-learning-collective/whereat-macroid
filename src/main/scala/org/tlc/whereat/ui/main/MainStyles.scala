package org.tlc.whereat.ui.main

import android.view.Gravity
import android.widget.LinearLayout
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import macroid.{Tweak, AppContext}
import macroid.FullDsl._

import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.ViewTweaks._

import org.tlc.whereat.R
import org.tlc.whereat.ui.components.CircleView

/**
 * Author: @aguestuser
 * Date: 6/13/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */

object MainStyles {

  def goButtonWrapperStyle(implicit appContext: AppContext): Tweak[LinearLayout] = {
    vMatchWidth +
      llGravity(Gravity.CENTER_HORIZONTAL) +
      llHorizontal
  }

  def goButtonStyle(implicit appContext: AppContext): Tweak[CircleView] = {
    val s = resGetDimensionPixelSize(R.dimen.go_button_size)
    val m = resGetDimensionPixelSize(R.dimen.go_button_margin)
    val mt = resGetDimensionPixelSize(R.dimen.go_button_margin_top)
    lp[LinearLayout](s,s) + vMargin(m,mt,m,m)
  }
}
