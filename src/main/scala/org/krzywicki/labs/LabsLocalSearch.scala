package org.krzywicki.labs

import org.krzywicki.localsearch.{LocalSearchHelper, LocalSearchStrategy}
import pl.edu.agh.scalamas.genetic.GeneticEvaluator
import org.krzywicki.labs.LabsLocalSearch.BitFlip

object LabsLocalSearch {

  case class BitFlip(bitToFlip: Int)

}

trait LabsLocalSearch extends GeneticEvaluator[LabsOps] {

  def localSearchStrategy: LocalSearchStrategy[LabsOps]

  abstract override def evaluate(s: LabsOps#Solution) = {
    localSearchStrategy.search(super.evaluate(s), new LabsLocalSearchHelper(s))
  }
}

class LabsLocalSearchHelper(solution: LabsOps#Solution) extends LocalSearchHelper[BitFlip, LabsOps] {

  lazy val flipper = OneBitFastFlipper(solution)

  override def possibleChanges = Stream.tabulate(solution.length)(BitFlip(_))

  override def evaluateChange(change: BitFlip) = flipper.energyWithFlipped(change.bitToFlip)

  override def applyChange(change: BitFlip) = {
    val copy = solution.clone()
    copy(change.bitToFlip) = !copy(change.bitToFlip)
    new LabsLocalSearchHelper(copy)
  }
}
