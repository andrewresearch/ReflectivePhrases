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

package net.andrewresearch.reflectivephrases

import com.twitter.finagle.Http
import com.twitter.util.Await
import net.andrewresearch.reflectivephrases.server.HttpServer

/**
  * Created by andrew on 4/11/2015.
  */
object Application extends App {
  //AppSettings.load //We want to check that settings are ok on boot
  val server = Http.serve(":8888", HttpServer.service)
  Await.ready(server)
}