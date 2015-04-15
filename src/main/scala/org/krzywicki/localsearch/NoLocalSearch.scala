package org.krzywicki.localsearch

import pl.edu.agh.scalamas.genetic.{GeneticOps, GeneticProblem}

/**
 * Created by Daniel on 2015-04-15.
 */
trait NoLocalSearch extends LocalSearch {
  this: GeneticProblem =>

  def localSearchStrategy = NoLocalSearchStrategy

  object NoLocalSearchStrategy extends LocalSearchStrategy {

    def search[C, G <: GeneticOps[G]](baseEvaluation: G#Evaluation, helper: LocalSearchHelper[C, G])(implicit ordering: Ordering[G#Evaluation]) = baseEvaluation
  }

}