package org.krzywicki.problem

import pl.edu.agh.scalamas.genetic.GeneticProblem

/**
 * Created by Daniel on 2015-04-13.
 */
trait IncrementalGeneticProblem extends GeneticProblem {

  override type Genetic <: IncrementalGeneticOps[Genetic]

  override def genetic: Genetic

}
