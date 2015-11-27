/*
 * Copyright 2015 Andrew Gibson http://andrewresearch.net/
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.andrewresearch.reflectivephrases.server

import com.twitter.util.FuturePool
import io.finch._
import io.finch.request._
import net.andrewresearch.reflectivephrases.service.Logger


/**
  * These are the end points for the app
  * Created by andrew on 4/11/2015.
  */
object Api extends Logger {

  val realtime = new RealTimeController()

  def endpoints = {
    val adm = hello
    val rt = reflection
    (adm :+: rt).toService
  }

  val reflection: Endpoint[String] = post("reflection" ? body.as[String]) { reflection: String =>
    log.debug("POST:/reflection request")
    FuturePool.unboundedPool { //To prevent work threads from being blocked
      val responseData = realtime.analyseReflection(reflection)
      Ok(responseData).withContentType(Some("application/json"))
    }
  }

  val hello: Endpoint[String] = get("health") {
    log.debug("GET:/ (default) request")
    Ok("""{"health":"ok"}""").withContentType(Some("application/json"))
  }


}
