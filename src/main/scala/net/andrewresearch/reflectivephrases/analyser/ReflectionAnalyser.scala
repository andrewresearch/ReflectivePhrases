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

package net.andrewresearch.reflectivephrases.analyser

import java.util.Date

import cc.factorie.app.nlp.pos.OntonotesForwardPosTagger
import cc.factorie.app.nlp.{Document, DocumentAnnotator, DocumentAnnotatorPipeline, MutableDocumentAnnotatorMap}
import net.andrewresearch.reflectivephrases.data.{CodedSentence, Reflection, ReflectionCoded}
import net.andrewresearch.reflectivephrases.service.JsonService
import org.slf4j.LoggerFactory

import scala.collection.mutable

/**
 * Analyses Reflections, creates POS tags, calls PosTagAnalyser, and classsifies as strong, weak or undetermined
 * Created by Andrew Gibson on 15/07/15.
 */

class ReflectionAnalyser {

  private val annotators = new mutable.ArrayBuffer[DocumentAnnotator] += OntonotesForwardPosTagger
  private val map = new MutableDocumentAnnotatorMap ++= DocumentAnnotatorPipeline.defaultDocumentAnnotationMap
  private val pipeline = DocumentAnnotatorPipeline(map = map.toMap, prereqs = Nil, annotators.flatMap(_.postAttrs))

  private var ref: Reflection = _
  private var codedSen: Seq[CodedSentence] = _
  private var metaTagSummary: Map[String,Int] = _
  private var phraseTagSummary: Map[String,Int] = _
  private var posDoc: Document = new Document()

  def logger = LoggerFactory.getLogger(this.getClass)

  def load(reflection:String): ReflectionAnalyser = {
    ref = Reflection(reflection)
    this
  }

  def basicAnalysis:ReflectionAnalyser = {
    val clean = ref.reflectionText.replaceAll("[\u0000-\u001f]", "")
      .replaceAll("(\\\\r)|(\\\\n)"," ")
      .replaceAll("[''']","_")
      .replaceAll("([^a-zA-Z]['_'])|(['_'][^a-zA-Z])"," ")
    ref.words = clean.split("\\W+")
    ref.sentences = clean.split("(?<=[.!?])\\s+(?=[A-Z,a-z,0-9,\\$])")
    ref.wordCount = ref.words.length
    ref.sentenceCount = ref.sentences.length
    if (ref.sentenceCount > 0) ref.averageWordsPerSentence = ref.wordCount / ref.sentenceCount
    ref.setWordCounts(mutable.HashMap() ++ ref.words.groupBy(word => word.toLowerCase).mapValues(_.length.toLong))
    val wordLengths: mutable.Map[String, Long] = mutable.HashMap()
    var totalLengths = 0
    ref.words.foreach { word =>
      totalLengths += word.length
      wordLengths.+=(word.length.toString -> (wordLengths.getOrElse(word.length.toString, 0L) + 1L))
    }
    ref.setWordLengths(wordLengths)
    if (ref.wordCount > 0) ref.averageWordLength = totalLengths.toDouble / ref.wordCount
    if (ref.sentenceCount > 0) ref.averageSentenceLength = ref.wordCount.toDouble / ref.sentenceCount
    ref.textComplexity = 0.0
    this
  }

  def posAnalysis:ReflectionAnalyser = {
    posDoc = new Document(ref.reflectionText)
    pipeline.process(posDoc)
    this
  }

  def analysePhrases: ReflectionAnalyser = {

    val docSentences = posDoc.sentences.toSeq
    val sentencePosTags = docSentences.map(_.tokens.map(_.posTag.categoryValue))
    val posTagAnalyser = new PosTagAnalyser()
    codedSen = posTagAnalyser
      .addSentenceTags(sentencePosTags)
      .addSentenceWords(docSentences.map(_.tokens.map(_.lemmaString)))
      .analyse
      .getCodedSentences

    this
  }

  def summariseMetrics: ReflectionAnalyser = {
    metaTagSummary = codedSen.flatMap(_.metacognitionTags).groupBy(identity).mapValues(_.size)
    metaTagSummary += "none" -> codedSen.count(_.metacognitionTags.length<1)
    Array("knowledge","experience","regulation").foreach { k =>
      if (!metaTagSummary.contains(k)) metaTagSummary += k -> 0
    }
    phraseTagSummary = codedSen.flatMap(_.phraseTags).filterNot(_.contains("general")).groupBy(identity).mapValues(_.size)
    phraseTagSummary += "none" -> codedSen.count(_.phraseTags.length<1)
    Array("outcome","temporal","pertains","consider","anticipate","definite","possible","selfReflexive","emotive","selfPossessive","compare","manner").foreach { k =>
      if (!phraseTagSummary.contains(k)) phraseTagSummary += k -> 0
    }
    this
  }

  def reflection = {
    ref.lastAnalysed = new Date().getTime
    JsonService.makeString(ref,codedSen,metaTagSummary,phraseTagSummary) //,refCoded)
  }

}

