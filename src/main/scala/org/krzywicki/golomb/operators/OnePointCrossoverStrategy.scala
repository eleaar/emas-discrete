/*
 * This file is a part of ogronage project.
 * (c) Copyright 2012, Specter
 * All rights reserved. Check the documentation for licensing terms.
 */
/*
 * File: OnePointCrossoverStrategy.scala
 * Created: 2012-12-18
 * Author: Specter
 */

package org.krzywicki.golomb.operators

import org.krzywicki.golomb.problem.{IndirectRuler, Ruler}
import pl.edu.agh.scalamas.random.RandomGeneratorComponent

trait OnePointCrossover {

  this: RandomGeneratorComponent =>

  def crossoverStrategy = OnePointCrossoverStrategy

  object OnePointCrossoverStrategy {

    def recombine(ruler1: Ruler, ruler2: Ruler) = {
      val first = ruler1.indirectRepresentation.toBuffer
      val second = ruler2.indirectRepresentation.toBuffer

      val minSize = math.min(first.length, second.length)
      val cutPoint = randomData.nextInt(1, minSize - 1)

      val (firstHead, firstTail) = first.splitAt(cutPoint)
      val (secondHead, secondTail) = second.splitAt(cutPoint)

      (IndirectRuler(firstHead ++ secondTail), IndirectRuler(secondHead ++ firstTail))
    }
  }
}
