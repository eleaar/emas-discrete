package org.krzywicki.labs

import pl.edu.agh.scalamas.genetic.GeneticOps

/**
 * Created by Daniel on 2015-04-14.
 */
trait LabsOps extends GeneticOps[LabsOps]  {
  type Feature = Boolean
  type Solution = Array[Feature]
  type Evaluation = Double
}
