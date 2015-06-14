package org.tlc.whereat.ui.components

import android.content.Context
import android.graphics.Paint.Style
import android.graphics.{Canvas, Paint}
import android.util.AttributeSet
import android.view.View
import macroid.FullDsl._
import macroid.{AppContext, Snail, Tweak}

/**
 * Author: @aguestuser
 * Date: 6/13/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */

class CircleView(context: Context, attr: AttributeSet, defStyleAttr: Int)
  extends View(context, attr, defStyleAttr) {

  def this(context: Context) = this(context, null, 0)
  def this(context: Context, attr: AttributeSet) = this(context, attr, 0)

  private val paint = { val p = new Paint(); p.setStyle(Style.FILL); p}
  def setColor(color: Int) = paint.setColor(color)
  def getColor: Int = paint.getColor

  override def onDraw(canvas: Canvas): Unit = {
    super.onDraw(canvas)
    canvas.drawCircle(getWidth / 2, getHeight / 2, getWidth / 2, paint)
  }

}

object CircleView {
  type W = CircleView

  def cvColor(color: Int) = Tweak[W] (_.setColor(color))
  def cvColorChange(color: Int) = Tweak[W] ({ cv ⇒  cv.invalidate(); cv.setColor(color) })
  def delayedCvColorChange(color: Int, time: Int): Snail[W] = cvColorChange(color) ++ delay(time)

  def toggleGoButton(on: Int, off: Int) = Tweak[W] { cv ⇒
    cv.invalidate()
    val next = if (cv.getColor == on) off else on
    cv.setColor(next)
  }
  def toastSharingStatus(cv: Option[CircleView], on: Int, off: Int)(implicit appContext: AppContext) = {
    val msg = if (cv.get.getColor == on) "off" else "on"
    toast(s"Location sharing turned $msg.") <~ fry
  }
}