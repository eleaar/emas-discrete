package org.krzywicki.localsearch

import pl.edu.agh.scalamas.genetic.GeneticProblem

/**
 * Created by Daniel on 2015-04-13.
 */
trait LocalSearch {
  this: GeneticProblem =>

  def localSearchStrategy: LocalSearchStrategy
}


