package org.tlc.whereat.net

import com.squareup.okhttp.OkHttpClient
import io.taig.communicator.internal.result.Parser

import scala.concurrent.Future

/**
 * Author: @aguestuser
 * Date: 4/24/15
 * License: GPLv2 (https://www.gnu.org/licenses/gpl-2.0.html)
 */

trait NetUtil {

  def reqJson[T](url: String)(implicit parser: Parser[T], client: OkHttpClient = new OkHttpClient()): Future[T] = {

    import io.taig.communicator._
    import scala.concurrent.ExecutionContext.Implicits.global

    Request(url)
      .get()
      .parse[T]
      .transform(
        res ⇒ res.payload,
        throwable ⇒ throwable) }
}
