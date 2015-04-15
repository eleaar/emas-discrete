package org.krzywicki.localsearch

import pl.edu.agh.scalamas.genetic.GeneticOps

/**
 * Created by Daniel on 2015-04-15.
 */
trait LocalSearchHelper[C, G <: GeneticOps[G]] {

  def possibleChanges: Stream[C]

  def evaluateChange(change: C): G#Evaluation

  def applyChange(change: C): LocalSearchHelper[C, G]
}
