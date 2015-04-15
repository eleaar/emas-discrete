package org.krzywicki.localsearch

import pl.edu.agh.scalamas.genetic.GeneticOps

/**
 * Created by Daniel on 2015-04-15.
 */
trait LocalSearchStrategy {

  def search[C, G <: GeneticOps[G]](baseEvaluation: G#Evaluation, helper: LocalSearchHelper[C, G])(implicit ordering: Ordering[G#Evaluation]): G#Evaluation
}
