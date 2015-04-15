package org.krzywicki.localsearch

import pl.edu.agh.scalamas.genetic.{GeneticOps, GeneticProblem}

/**
 * Created by Daniel on 2015-04-13.
 */
trait LocalSearch {
  this: GeneticProblem =>

  def localSearchStrategy: LocalSearchStrategy[Genetic]
}

trait LocalSearchStrategy[G <: GeneticOps[G]] {

  def search[C](baseEvaluation: G#Evaluation, helper: LocalSearchHelper[C, G]): G#Evaluation
}
