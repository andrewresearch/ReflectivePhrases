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

package net.andrewresearch.reflectivephrases.data

/**
 * The data class for persisting analysis
 * Created by andrew on 10/06/15.
 */


import org.bson.types.ObjectId


trait BasicReflection {
  var authorId:ObjectId = _
  var groupId:ObjectId = _
  var reflectionDataId:ObjectId = _
  var authorDataId:ObjectId = _
  var groupDataId:ObjectId = _
  var hasText: Boolean = _
  var wordCount: Int = _
  var textComplexity: Double = _
  var averageWordLength: Double = _
  var reflectionPoint: Double = _
  var reflectionText: String = _
  var sentenceCount: Int = _
  var averageSentenceLength: Double = _
  var reflectionTime: Long = _
  var words: Array[String] = _
  var sentences: Array[String] = _
  var averageWordsPerSentence: Double = _
}


case class Reflection(organisationDataId:ObjectId) extends GenericEntityAnalysis with BasicReflection with PosPhraseAnalysis

object Reflection {
  def apply(reflection:String) = {
    val r = new Reflection (new ObjectId())
    r.reflectionText = reflection
    r.reflectionTime = System.currentTimeMillis()
    r
  }
}
