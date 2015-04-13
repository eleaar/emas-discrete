package org.krzywicki.problem

import pl.edu.agh.scalamas.genetic.GeneticEvaluator

/**
 * Created by Daniel on 2015-04-13.
 */
trait IncrementalGeneticEvaluator[G <: IncrementalGeneticOps[G]] extends GeneticEvaluator[G] {

  type Change

  def possibleChanges(solution: G#Solution): Stream[G#Change]

  def evaluateChange(solution: G#Solution, change: G#Change): G#Evaluation

  def applyChange(solution: G#Solution, change: G#Change): G#Solution

}
