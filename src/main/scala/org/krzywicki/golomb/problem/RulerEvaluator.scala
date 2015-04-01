/*
 * This file is a part of ogronage project.
 * (c) Copyright 2012, Specter
 * All rights reserved. Check the documentation for licensing terms.
 */
/*
 * File: RulerEvaluator.scala
 * Created: 2012-12-13
 * Author: Specter
 */

package org.krzywicki.golomb.problem

/**
 *
 * @author Specter
 */
object RulerEvaluator {

  def evaluate(ruler: Ruler): Double = {
    // FitnessCount.increment
    return calculateViolations(ruler.getDirectRepresentation) * 375 - ruler.length
  }

  /**
   * The performance of this method is REALLY IMPORTANT, because it is used in fitness computing. The implementation uses array instead of Map, because it is
   * more than two times faster. Implementation also uses while loops because for loops in Scala uses iterable or range, which needs more time than simple
   * incrementation of variable.
   */
  def calculateViolations(representation: Array[Int]): Int = {
    var violationCount = 0
    val size = representation.length
    val sizeMinusOne = size - 1
    val distances = new Array[Int](representation(sizeMinusOne) + 1)
    var i = 0
    while (i < sizeMinusOne) {
      var j = i + 1
      while (j < size) {
        val idx = representation(j) - representation(i)
        distances(idx) = distances(idx) + 1
        j += 1
      }
      i += 1
    }
    i = 0
    while (i < distances.length) {
      if (distances(i) > 0) {
        // IF distance occurred ONE time - it isn't an error
        violationCount += distances(i) - 1
      }
      i += 1
    }
    return -violationCount
  }

}
