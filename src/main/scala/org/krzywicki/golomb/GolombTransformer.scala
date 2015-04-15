package org.krzywicki.golomb

import org.apache.commons.math3.random.RandomDataGenerator
import pl.edu.agh.scalamas.genetic.GeneticTransformer

trait GolombTransformer extends GeneticTransformer[GolombOps] {

  def maxMarkSize: Int

  def randomData: RandomDataGenerator

  override def transform(solution: GolombOps#Solution) = {
    val diffs = solution.clone()

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

    for ((i, d) <- indexesToChange.zip(newDiffs)) {
      diffs(i) = d
    }

    diffs
  }

  override def transform(solution1: GolombOps#Solution, solution2: GolombOps#Solution) = {
    val minSize = math.min(solution1.length, solution2.length)
    val cutPoint = randomData.nextInt(1, minSize - 1)

    val (firstHead, firstTail) = solution1.splitAt(cutPoint)
    val (secondHead, secondTail) = solution2.splitAt(cutPoint)

    (firstHead ++ secondTail, secondHead ++ firstTail)
  }
}

