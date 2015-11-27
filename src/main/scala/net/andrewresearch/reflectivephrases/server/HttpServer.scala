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

import com.twitter.finagle.http.filter.Cors
import com.twitter.finagle.http.filter.Cors.HttpFilter
import com.twitter.server.TwitterServer


/**
  * The instance of TwitterServer for the webapp
  * Created by andrew on 20/11/2015.
  */
object HttpServer extends TwitterServer {
  val service = new HttpFilter(Cors.UnsafePermissivePolicy) andThen Api.endpoints
  onExit {
    this.service.close()
  }
}
