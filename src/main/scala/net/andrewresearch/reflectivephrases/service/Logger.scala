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

package net.andrewresearch.reflectivephrases.service

/**
  * Created by andrew on 4/11/2015.
  */
import org.slf4j.LoggerFactory

trait Logger {

  lazy val log = LoggerFactory.getLogger(this.getClass)

  def timeInfo[T](f: => T): T = {
    val start = System.nanoTime()
    val result = f
    val end = System.nanoTime()
    log.info("Elapsed time: " + (end - start)/1000 + "ms")
    result
  }

}
