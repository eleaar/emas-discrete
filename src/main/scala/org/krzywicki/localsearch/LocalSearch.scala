package org.krzywicki.localsearch

import pl.edu.agh.scalamas.genetic.GeneticProblem

/**
 * Created by Daniel on 2015-04-13.
 */
trait LocalSearch {
  this: GeneticProblem =>

  def localSearchStrategy: LocalSearchStrategy

  trait LocalSearchStrategy {

    def search(solution: Genetic#Solution): (Genetic#Solution, Genetic#Evaluation)
  }
}
