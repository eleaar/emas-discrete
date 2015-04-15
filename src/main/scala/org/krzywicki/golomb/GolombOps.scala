package org.krzywicki.golomb

import org.krzywicki.problem.IncrementalGeneticOps

/**
 * Created by Daniel on 2015-04-15.
 */
trait GolombOps extends IncrementalGeneticOps[GolombOps] {

  type Solution = Array[Int]
  type Evaluation = Int
  type Change = GolombOps.MarkChange
}

object GolombOps {
  case class MarkChange(index: Int, mark: Int)
}
