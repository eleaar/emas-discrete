package org.krzywicki.labs

import org.krzywicki.localsearch.LocalSearchStrategy
import org.krzywicki.problem.IncrementalGeneticEvaluator

import scala.collection.mutable

/**
 * Created by Daniel on 2015-04-14.
 */
trait LabsIncrementalEvaluator extends LabsEvaluator with IncrementalGeneticEvaluator[LabsOps] {

  def localSearchStrategy: LocalSearchStrategy[LabsOps]

  lazy val flippers = new mutable.WeakHashMap[LabsOps#Solution, OneBitFastFlipper]

  override def evaluate(s: LabsOps#Solution) = {
    localSearchStrategy.search(super.evaluate(s), new LabsLocalSearchHelper(s))

  }

  def possibleChanges(solution: LabsOps#Solution): Stream[LabsOps#Change] = {
    Stream.tabulate(solution.length)(LabsOps.Bitflip(_))
  }

  def evaluateChange(solution: LabsOps#Solution, change: LabsOps#Change) = {
    val flipper = flippers.getOrElseUpdate(solution, OneBitFastFlipper(solution))
    flipper.energyWithFlipped(change.idx)
  }

  def applyChange(solution: LabsOps#Solution, change: LabsOps#Change) = {
    val copy = solution.clone()
    copy(change.idx) = !copy(change.idx)
    copy
  }
}
