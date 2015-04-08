/*
 * This file is a part of ogronage project.
 * (c) Copyright 2012, Specter
 * All rights reserved. Check the documentation for licensing terms.
 */
/*
 * File: SegmentsLengthMutationStrategy.scala
 * Created: 2012-12-18
 * Author: Specter
 */

package org.krzywicki.golomb.operators

import org.krzywicki.golomb.problem.{Ruler}
import pl.edu.agh.scalamas.random.RandomGeneratorComponent

trait SegmentsLengthMutation {

  this: RandomGeneratorComponent =>

  def maxMarkSize: Int

  def mutationStrategy = SegmentsLengthMutationStrategy

  object SegmentsLengthMutationStrategy {

    def mutateSolution(ruler: Ruler): Ruler = {
      val diffs = ruler.indirectRepresentation.toBuffer

      // We change at least one and at most half of the diffs
      val diffsToChange = randomData.nextInt(1, diffs.length / 2)

      // We don't want the change the '1' diff if it is present
      val unitIndex = diffs.indexOf(1)
      val allIndexes = (0 until diffs.length).toSet

      // We select one more in case unitIndex is present
      val randomIndexes = randomData.nextPermutation(diffs.length, diffsToChange + 1).toSet
      val otherThanUnit = randomIndexes - unitIndex
      val indexesToChange = otherThanUnit.take(diffsToChange)
      val indexesToKeep = allIndexes -- indexesToChange

      val diffsToKeep = indexesToKeep.map(diffs(_))


      val randomDiffs = randomData.nextPermutation(maxMarkSize, maxMarkSize)
      val diffsToChooseFrom = randomDiffs.filter(!diffsToKeep.contains(_))
      val newDiffs = diffsToChooseFrom.distinct.take(diffsToChange)

      for ((i,d) <- indexesToChange.zip(newDiffs)) {
        diffs(i) = d
      }

      Ruler(diffs.toIndexedSeq)
    }

    def randomIntStream(min: Int, max: Int) = Stream.continually(randomData.nextInt(min, max))

  }
}
