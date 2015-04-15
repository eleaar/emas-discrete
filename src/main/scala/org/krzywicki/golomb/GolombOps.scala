package org.krzywicki.golomb

import pl.edu.agh.scalamas.genetic.GeneticOps

/**
 * Created by Daniel on 2015-04-15.
 */
trait GolombOps extends GeneticOps[GolombOps] {

  type Solution = Array[Int]
  type Evaluation = Int
}

