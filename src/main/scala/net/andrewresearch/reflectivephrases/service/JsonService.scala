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

import io.circe._, io.circe.generic.auto._, io.circe.parse._, io.circe.syntax._
import net.andrewresearch.reflectivephrases.data.{CodedSentence, Reflection}

/**
  * Perform the necessary translations to and from json
  * Created by andrew on 20/11/2015.
  */

import org.slf4j.LoggerFactory

object JsonService {

  def logger = LoggerFactory.getLogger(this.getClass)

  def makeString(reflection:Reflection,codedSentences:Seq[CodedSentence],metaTagSummary:Map[String,Int],phraseTagSummary:Map[String,Int]):String = {
    "{\"counts\":" + reflect(reflection) +
      ",\"summary\":" + summary(metaTagSummary,phraseTagSummary) +
      ",\"tags\": [" + coded(codedSentences) + "]}"
  }

  def reflect(reflection:Reflection):String = new Reflect(
    reflection.reflectionTime,
    reflection.wordCount,
    reflection.averageWordLength,
    reflection.sentenceCount,
    reflection.averageSentenceLength
  ).asJson.spaces2

  def summary(metaTagSummary:Map[String,Int],phraseTagSummary:Map[String,Int]) = new Summary(
    metaTagSummary,
    phraseTagSummary
  ).asJson.spaces2

  def coded(codedSentences:Seq[CodedSentence]):String = {
    codedSentences.map { cs =>
      new Coded(
        cs.sentence,
        cs.phrases.map(p => p.phrase + "[" + p.phraseType + "," + p.start + "," + p.end + "]"),
        //cs.phraseTags,
        cs.subTags,
        cs.metacognitionTags
      ).asJson.spaces2
    }.mkString(",")
  }


  case class Reflect(
                      timestamp:Long,
                      wordCount:Int,
                      avgWordLength:Double,
                      sentenceCount:Int,
                      avgSentenceLength:Double
                    )

  case class Summary(
                      metaTagSummary:Map[String,Int],
                      phraseTagSummary:Map[String,Int]
                    )

  case class Coded(
                    sentence:String,
                    phrases:Array[String],
                    subTags:Array[String],
                    metaTags:Array[String]
                  )
}
