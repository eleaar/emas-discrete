/*
 * This file is a part of ogronage project.
 * (c) Copyright 2012, Specter
 * All rights reserved. Check the documentation for licensing terms.
 */
/*
 * File: TabooSearchStrategy.scala
 * Created: 2012-12-18
 * Author: Specter
 */

package org.krzywicki.golomb.operators

import org.apache.commons.math3.random.RandomDataGenerator
import org.krzywicki.golomb.problem.Ruler
import org.krzywicki.golomb.problem.RulerEvaluator
import pl.edu.agh.scalamas.app.AgentRuntimeComponent
import pl.edu.agh.scalamas.random.RandomGeneratorComponent

import scala.collection.mutable.HashMap


case class MarkChange(index: Int, mark: Int, violation: Int)

class TabooFilter[G](randomData: RandomDataGenerator) {

  private var iteration = 0
  private val map = new HashMap[G, Int]

  def setTaboo(item: G) {
    val duration = randomData.nextInt(4, 100)
    map.put(item, iteration + duration)
  }

  def isTaboo(item: G) = {
    map.get(item) match {
      case None => false
      case Some(limit) if iteration > limit => false
      case Some(limit) if iteration <= limit => true
    }
  }

  def flush(): Unit = {
    iteration += 1
  }
}


trait TabooSearchStrategy {

  this: AgentRuntimeComponent with RandomGeneratorComponent =>

  lazy val maxIterationCount = agentRuntime.config.getInt("genetic.golomb.iterationCount")
  require(maxIterationCount > 0)

  def search(ruler: Ruler): Int = {
    val filter = new TabooFilter[MarkChange](randomData)
    var bestRuler = RulerEvaluator.decode(ruler)
    var bestRulerViolation = RulerEvaluator.violations(bestRuler)
    var iteration = 0
    var shouldStop = false

    while (iteration <= maxIterationCount && bestRulerViolation > 0 && !shouldStop) {
      val change = findChange(bestRuler, filter)
      change match {
        case Some(tabuItem) =>
          filter.setTaboo(tabuItem)
          if (tabuItem.violation < bestRulerViolation) {
            val representation = bestRuler.toBuffer
            representation(tabuItem.index) = tabuItem.mark
            bestRuler = representation.toIndexedSeq
            bestRulerViolation = tabuItem.violation
          }

        case None => shouldStop = true
      }
      filter.flush()
      iteration += 1
    }

    // TODO: move to evaluator
    return bestRuler.last + 375 * bestRulerViolation
  }

  private def findChange(marks: IndexedSeq[Int], filter: TabooFilter[MarkChange]): Option[MarkChange] = {
    var bestChange: Option[MarkChange] = None
    val size = marks.length
    val distances = RulerEvaluator.distancesMap(marks)

    // Changes to '0' mark moves the ruler
    // Changing from 1st to (n - 1)th mark
    var i = 1
    while (i < size - 1) {
      var j = marks(i - 1) + 1
      val maxIdx = marks(i + 1) - 1
      while (j <= maxIdx) {
        val currentChange = createChange(marks, distances, i, j)
        bestChange = chooseBetterChange(currentChange, bestChange, filter)
        j += 1
      }
      i += 1
    }
    // Changing last mark
    i = marks(size - 2) + 1
    val maxIdx = marks(size - 1) - 1
    while (i <= maxIdx) {
      val currentChange = createChange(marks, distances, size - 1, i)
      bestChange = chooseBetterChange(currentChange, bestChange, filter)
      i += 1
    }

    return bestChange
  }

  private def createChange(marks: IndexedSeq[Int], distances: Array[Int], index: Int, newMark: Int): MarkChange = {
    val newDistances = RulerEvaluator.distancesMapAfterFlip(marks, distances, index, newMark)
    MarkChange(index, newMark, RulerEvaluator.distanceViolations(newDistances))
  }

  private def chooseBetterChange(currentChange: MarkChange, bestChange: Option[MarkChange], filter: TabooFilter[MarkChange]): Option[MarkChange] = {
    if (filter.isTaboo(currentChange)) {
      bestChange
    } else {
      bestChange match {
        case None => Some(currentChange)
        case Some(previousChange) if currentChange.violation < previousChange.violation => Some(currentChange)
        case Some(previousChange) if currentChange.violation >= previousChange.violation => bestChange
      }
    }
  }
}
