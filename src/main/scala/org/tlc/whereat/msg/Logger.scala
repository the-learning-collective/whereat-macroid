package org.tlc.whereat.msg

import android.util.Log

/**
 * Author: @aguestuser
 * Date: 5/10/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */

trait Logger {

  protected def log(level: Int, tag: String, message: String): Int = {
    level match {
      case Log.VERBOSE => Log.v(tag, message)
      case Log.DEBUG => Log.d(tag, message)
      case Log.INFO => Log.i(tag, message)
      case Log.WARN => Log.w(tag, message)
      case Log.ERROR => Log.e(tag, message)
      case Log.ASSERT => Log.wtf(tag, message) } }
}
