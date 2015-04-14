package org.krzywicki.labs

import org.krzywicki.problem.IncrementalGeneticOps

/**
 * Created by Daniel on 2015-04-14.
 */
trait LabsOps extends IncrementalGeneticOps[LabsOps]  {
  type Feature = Boolean
  type Solution = Array[Feature]
  type Evaluation = Double
  type Change = LabsOps.Bitflip
}

object LabsOps {
  case class Bitflip(idx: Int)
}