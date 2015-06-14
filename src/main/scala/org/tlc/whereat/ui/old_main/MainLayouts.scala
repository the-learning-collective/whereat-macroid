package org.tlc.whereat.ui.old_main

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

//  def default(greeting: Option[TextView])(implicit ctx: ActivityContext): Ui[LinearLayout] =
//    l[LinearLayout](
//      w[Button] <~
//        text("Get Location") <~
//        On.click {
//          greeting <~ show
//        },
//      w[TextView] <~
//        wire(greeting) <~
//        MainTweaks.greeting("Hello!")
//    ) <~ MainTweaks.orient

//  def locGetter(locText: Option[TextView])(implicit ctx: ActivityContext): Ui[LinearLayout] =
//    l[LinearLayout](
//      w[Button] <~
//        text("Get Location") <~
//        On.click {
//          locText <~ show
//        },
//      w[TextView] <~
//        wire(locText) <~
//        MainTweaks.greeting("Hello!")
//    ) <~ MainTweaks.orient

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
