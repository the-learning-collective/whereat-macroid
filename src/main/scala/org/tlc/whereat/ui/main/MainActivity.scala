package org.tlc.whereat.ui.main

import android.app.Activity
import android.os.Bundle
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import macroid.FullDsl._
import macroid.{Contexts, Ui}
import org.tlc.whereat.R
import org.tlc.whereat.ui.components.CircleView._

/**
 * Author: @aguestuser
 * Date: 6/13/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */
class MainActivity
  extends Activity
  with Contexts[Activity]
  with MainLayout
  with MainTweaks {

  override def onCreate(savedInstanceState: Bundle) = {

    super.onCreate(savedInstanceState)

    setContentView(layout)

    val off = resGetColor(R.color.go_button_off)
    val on = resGetColor(R.color.go_button_on)

    runUi {
      goButton <~ cvColor(off) <~
//        On.click {
//           goButton <~ cvColorChange(off) <~~ delay(100) <~ cvColorChange(on)
//        } <~
//
          /*TODO:
          * The call to `delay(100)` on line 34 above triggers the following error message:
          *
          * Don't know how to snail
          *   macroid.Ui[Option[org.tlc.whereat.ui.components.CircleView]] with macroid.Snail[android.view.View].
          * Try importing an instance of
          *   CanSnail[macroid.Ui[Option[org.tlc.whereat.ui.components.CircleView]], macroid.Snail[android.view.View], ...].
          *
          * How can I fix this?
          * */
        On.longClick {
          toastSharingStatus(goButton, on, off) ~
          ( goButton <~ toggleGoButton(on,off) ) ~
          Ui(true)
        }
    }
  }
}
