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
 * Created by andrew on 22/05/15.
 */

import org.bson.types.ObjectId
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.collection.mutable

class GenericEntityAnalysis {

  val _id:ObjectId = new ObjectId
  var lastAnalysed: Long = _
  var wordLengths:java.util.Map[String,Long] = new java.util.HashMap[String,Long]()
  var wordCounts:java.util.Map[String,Long] = new java.util.HashMap[String,Long]()

  def logger = LoggerFactory.getLogger(this.getClass)

  def setWordLengths(wl:mutable.Map[String,Long]) = {
    wordLengths = wl.asJava
  }
  def getWordLengths:mutable.Map[String,Long] = wordLengths.asScala

  def setWordCounts(wc:mutable.Map[String,Long]) = {
    wordCounts = wc.asJava
  }
  def getWordCounts:mutable.Map[String,Long] = wordCounts.asScala
}

trait PosPhraseAnalysis {
  var posPhrases:java.util.Map[String,java.util.Map[String,Long]] = new java.util.HashMap[String,java.util.Map[String,Long]]()
  var phraseCounts:java.util.Map[String,Long] = new java.util.HashMap[String,Long]()
  var phraseDistribution:java.util.Map[String,Double] = new java.util.HashMap[String,Double]()

  def addToPosPhrases(phraseType:String,phrases:Map[String,Long]) = {
    val pp = posPhrases.asScala += phraseType -> phrases.asJava
    posPhrases = pp.asJava
  }
  def setPosPhrases(posPhrases:mutable.Map[String,mutable.Map[String,Long]]) = {
    this.posPhrases = posPhrases.map(p => p._1 -> p._2.asJava).asJava
  }
  def getPosPhrases:Map[String,Map[String,Long]] = {
    posPhrases.asScala.map(p => p._1 -> p._2.asScala.toMap).toMap
  }

  def setPhraseCounts(counts:mutable.Map[String,Long]) = {
    phraseCounts = counts.asJava
  }
  def getPhraseCounts:mutable.Map[String,Long] = phraseCounts.asScala

  def setPhraseDistribution(distribution:mutable.Map[String,Double]) = {
    phraseDistribution = distribution.asJava
  }
  def getPhraseDistribution:mutable.Map[String,Double] = phraseDistribution.asScala

}
