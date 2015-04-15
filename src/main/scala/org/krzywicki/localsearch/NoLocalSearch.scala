package org.krzywicki.localsearch

import org.krzywicki.problem.IncrementalGeneticProblem

/**
 * Created by Daniel on 2015-04-15.
 */
trait NoLocalSearch extends LocalSearch {
  this: IncrementalGeneticProblem =>

  def localSearchStrategy = NoLocalSearchStrategy

  object NoLocalSearchStrategy extends LocalSearchStrategy[Genetic] {

    def search[C](baseEvaluation: Genetic#Evaluation, helper: LocalSearchHelper[C, Genetic]) = baseEvaluation

  }

}