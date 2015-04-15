package org.krzywicki.localsearch

import pl.edu.agh.scalamas.genetic.GeneticProblem

/**
 * Created by Daniel on 2015-04-15.
 */
trait NoLocalSearch extends LocalSearch {
  this: GeneticProblem =>

  def localSearchStrategy = NoLocalSearchStrategy

  object NoLocalSearchStrategy extends LocalSearchStrategy[Genetic] {

    def search[C](baseEvaluation: Genetic#Evaluation, helper: LocalSearchHelper[C, Genetic]) = baseEvaluation

  }

}