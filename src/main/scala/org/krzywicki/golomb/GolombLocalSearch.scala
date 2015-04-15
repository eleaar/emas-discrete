package org.krzywicki.golomb

import org.krzywicki.golomb.GolombLocalSearch.MarkChange
import org.krzywicki.localsearch.{LocalSearchHelper, LocalSearchStrategy}
import pl.edu.agh.scalamas.genetic.GeneticEvaluator

import scala.collection.mutable.ArrayBuffer

object GolombLocalSearch {

  case class MarkChange(index: Int, mark: Int)

}

trait GolombLocalSearch extends GeneticEvaluator[GolombOps] {

  def localSearchStrategy: LocalSearchStrategy

  abstract override def evaluate(s: GolombOps#Solution) = {
    val marks = GolombEvaluator.decode(s)
    val helper = new GolombLocalSearchHelper(marks)
    val baseEvaluation = super.evaluate(s)

    localSearchStrategy.search(baseEvaluation, helper)(ordering)
  }
}

class GolombLocalSearchHelper(marks: Array[Int]) extends LocalSearchHelper[MarkChange, GolombOps] {

  lazy val distances = GolombEvaluator.distancesMap(marks)

  override def possibleChanges = {
    val size = marks.length
    val changes = ArrayBuffer[MarkChange]()

    // Changes to '0' mark moves the ruler
    // Changing from 1st to (n - 1)th mark
    var i = 1
    while (i < size - 1) {
      var j = marks(i - 1) + 1
      val maxIdx = marks(i + 1) - 1
      while (j <= maxIdx) {
        changes += MarkChange(i, j)
        j += 1
      }
      i += 1
    }
    // Changing last mark
    i = marks(size - 2) + 1
    val maxIdx = marks(size - 1) - 1
    while (i <= maxIdx) {
      changes += MarkChange(size - 1, i)
      i += 1
    }

    changes.toStream
  }

  override def evaluateChange(change: MarkChange) = {
    val newDistances = GolombEvaluator.distancesMapAfterFlip(marks, distances, change.index, change.mark)
    val newViolations = GolombEvaluator.distanceViolations(newDistances)
    GolombEvaluator.evaluateWithViolations(marks, newViolations)
  }

  override def applyChange(change: MarkChange) = {
    val newMarks = marks.clone()
    newMarks(change.index) = change.mark
    new GolombLocalSearchHelper(newMarks)
  }
}
