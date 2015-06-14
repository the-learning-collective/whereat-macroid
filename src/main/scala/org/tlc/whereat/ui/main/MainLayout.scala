package org.tlc.whereat.ui.main

import android.widget.LinearLayout
import macroid.FullDsl._
import macroid.{ActivityContext, AppContext}
import org.tlc.whereat.ui.components.CircleView
import org.tlc.whereat.ui.main.MainStyles._

/**
 * Author: @aguestuser
 * Date: 6/13/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */

trait MainLayout {

  var goButton = slot[CircleView]

  def layout(implicit appContext: AppContext, context: ActivityContext) = {

    getUi {
      l[LinearLayout](
        w[CircleView] <~
          wire(goButton) <~ goButtonStyle
      ) <~ goButtonWrapperStyle
    }
  }
}
