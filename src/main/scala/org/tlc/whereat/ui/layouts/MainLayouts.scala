package org.tlc.whereat.ui.layouts

import android.widget._
import macroid.ActivityContext
import macroid.FullDsl._

/**
 * Author: @aguestuser
 * Date: 4/22/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */
// layouts are composable!
object MainLayouts {

   def layout1(implicit ctx: ActivityContext) =
     l[LinearLayout]( // `l` aliases `layout`
       w[TextView], // `w` aliases `widget`
       w[ImageView],
       w[Button]
     )

   def layout2(implicit ctx: ActivityContext) =
     l[FrameLayout](
       w[ProgressBar]
     )

   def comboLayout(implicit ctx: ActivityContext) =
     l[FrameLayout](
       layout1,
       layout2
     )
 }
