package org.krzywicki.labs

import org.krzywicki.labs.LabsLocalSearchHelper.BitFlip
import org.krzywicki.localsearch.LocalSearchHelper

object LabsLocalSearchHelper {

  case class BitFlip(bitToFlip: Int)

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
