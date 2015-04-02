/*
 * This file is a part of ogronage project.
 * (c) Copyright 2012, Specter
 * All rights reserved. Check the documentation for licensing terms.
 */
/*
 * File: TabuSearchStrategy.scala
 * Created: 2012-12-18
 * Author: Specter
 */

package org.krzywicki.golomb.operators

import org.krzywicki.golomb.problem.{DirectRuler, IndirectRuler, RulerEvaluator, Ruler}
import org.krzywicki.golomb.problem.RulerEvaluator._
import pl.edu.agh.scalamas.app.AgentRuntimeComponent
import pl.edu.agh.scalamas.random.RandomGeneratorComponent

import scala.collection.mutable.HashMap
import scala.collection.mutable.Map

/**
 *
 * @author Specter
 */
trait TabuSearchStrategy {

  this: AgentRuntimeComponent with RandomGeneratorComponent =>

//  private var memConf: MemeticConfigurator[Ruler, Int] = _

  def maxIterationCount = agentRuntime.config.getInt("genetic.golomb.iterationCount")

  def search(ruler: Ruler): Int = {
    if (maxIterationCount < 1) {
      return 1
    }

    var bestRuler = DirectRuler(ruler.directRepresentation.toBuffer)
    var bestRulerViolation = -distanceViolations(ruler.directRepresentation.toArray)

    var iteration = 0
    val tabu = new HashMap[TabuItem, Int]

    var shouldStop = false
    while (iteration <= maxIterationCount && bestRulerViolation < 0 && !shouldStop) {
      val bestChange = getBestChange(bestRuler, bestRulerViolation, iteration, tabu)
      bestChange match {
        case change: TabuItem =>
          val it = randRange(4, 100)
          tabu.put(change, iteration + it)
          if (change.newViolation > bestRulerViolation) {
            val representation = bestRuler.directRepresentation.toArray
            representation(change.index) = change.newMark
            bestRuler = DirectRuler(representation)
            bestRulerViolation = change.newViolation
          }
        case _ => shouldStop = true
      }
      iteration += 1
    }


//      memConf.put(ruler, bestRuler)


    return bestRulerViolation * 375 - ruler.length
  }

  private def randRange(lowerInclusive: Int, upperInclusive: Int): Int = {
    val number = upperInclusive - lowerInclusive + 1
    return random.nextInt(number) + lowerInclusive
  }

  private def createChange(newRepresentation: Array[Int], i: Int, mark: Int): TabuItem = {
    newRepresentation(i) = mark
    val violations = -distanceViolations(newRepresentation)
    return new TabuItem(i, mark, violations)
  }

  private def validateChange(proposalChange: TabuItem, bestChange: TabuItem, bestRulerViolation: Int, iteration: Int, tabu: Map[TabuItem, Int]): TabuItem = {
    // 1 in comparison means that proposalChange has less violations - is better
    val meetsCriteria = bestChange == null || bestChange.compareTo(proposalChange) == 1
    tabu.get(proposalChange) match {
      case Some(it: Int) =>
        if (it <= iteration && meetsCriteria) {
          return proposalChange
        }
      case _ =>
    }
    if (proposalChange.newViolation > bestRulerViolation && meetsCriteria) {
      return proposalChange
    }
    return bestChange
  }

  private def getBestChange(ruler: Ruler, bestRulerViolation: Int, iteration: Int, tabu: Map[TabuItem, Int]): TabuItem = {
    var bestChange: TabuItem = null

    val representation = ruler.directRepresentation
    val size = representation.length
    // Changes to '0' mark moves the ruler
    // Changing from 1st to (n - 1)th mark
    var i = 1
    val sizeMinusOne = size - 1
    while (i < sizeMinusOne) {
      var j = representation(i - 1) + 1
      val maxIdx = representation(i + 1) - 1
      while (j <= maxIdx) {
        val proposalChange = createChange(ruler.directRepresentation.toArray.clone, i, j)
        bestChange = validateChange(proposalChange, bestChange, bestRulerViolation, iteration, tabu)
        j += 1
      }
      i += 1
    }
    // Changing last mark
    i = representation(size - 2) + 1
    val maxIdx = representation(sizeMinusOne) - 1
    while (i <= maxIdx) {
      val proposalChange = createChange(ruler.directRepresentation.toArray.clone, sizeMinusOne, i)
      bestChange = validateChange(proposalChange, bestChange, bestRulerViolation, iteration, tabu)
      i += 1
    }

    return bestChange
  }

}
