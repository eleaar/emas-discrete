package org.krzywicki.labs

import org.krzywicki.localsearch.LocalSearchStrategy
import org.krzywicki.problem.IncrementalGeneticEvaluator

/**
 * Created by Daniel on 2015-04-14.
 */
trait LabsIncrementalEvaluator extends LabsEvaluator with IncrementalGeneticEvaluator[LabsOps] {

  def localSearchStrategy: LocalSearchStrategy[LabsOps]

  override def evaluate(s: LabsOps#Solution) = {
    val result = localSearchStrategy.search(s, super.evaluate(s))
    result._2
  }

  def possibleChanges(solution: LabsOps#Solution): Stream[LabsOps#Change] = {
    val flipper = OneBitFastFlipper(solution)
    Stream.tabulate(solution.length)(LabsOps.Bitflip(_, flipper))
  }

  def evaluateChange(solution: LabsOps#Solution, change: LabsOps#Change) = {
    val idx = change.idx
    val flipper = change.flipper
    flipper.energyWithFlipped(idx)
  }

  def applyChange(solution: LabsOps#Solution, change: LabsOps#Change) = {
    val copy = solution.clone()
    copy(change.idx) = !copy(change.idx)
    copy
  }
}
