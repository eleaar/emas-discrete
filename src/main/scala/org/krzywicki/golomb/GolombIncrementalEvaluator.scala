package org.krzywicki.golomb

import org.krzywicki.localsearch.LocalSearchStrategy
import org.krzywicki.problem.IncrementalGeneticEvaluator

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Daniel on 2015-04-15.
 */
trait GolombIncrementalEvaluator extends GolombEvaluator with IncrementalGeneticEvaluator[GolombOps] {

  def localSearchStrategy: LocalSearchStrategy[GolombOps]

  override def evaluate(s: GolombOps#Solution) = {
    val marks = GolombEvaluator.decode(s)
    val helper = new GolombLocalSearchHelper(marks)
    val baseEvaluation = super.evaluate(s)

    localSearchStrategy.search(baseEvaluation, helper)
  }

  def possibleChanges(solution: GolombOps#Solution): Stream[GolombOps#Change] = {
    val size = solution.length
    val changes = ArrayBuffer[GolombOps.MarkChange]()

    // Changes to '0' mark moves the ruler
    // Changing from 1st to (n - 1)th mark
    var i = 1
    while (i < size - 1) {
      var j = solution(i - 1) + 1
      val maxIdx = solution(i + 1) - 1
      while (j <= maxIdx) {
        changes += GolombOps.MarkChange(i, j)
        j += 1
      }
      i += 1
    }
    // Changing last mark
    i = solution(size - 2) + 1
    val maxIdx = solution(size - 1) - 1
    while (i <= maxIdx) {
      changes += GolombOps.MarkChange(size - 1, i)
      i += 1
    }

    changes.toStream
  }

  def evaluateChange(solution: GolombOps#Solution, change: GolombOps#Change) = {
    // TODO: reuse distances
    val distances = GolombEvaluator.distancesMap(solution)
    val newDistances = GolombEvaluator.distancesMapAfterFlip(solution, distances, change.index, change.mark)
    val newViolations = GolombEvaluator.distanceViolations(newDistances)
    GolombEvaluator.evaluateWithViolations(solution, newViolations)
  }

  def applyChange(solution: GolombOps#Solution, change: GolombOps#Change) = {
    val newSolution = solution.clone()
    newSolution(change.index) = change.mark
    newSolution
  }
}
