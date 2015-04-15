package org.krzywicki.labs

import org.krzywicki.localsearch.LocalSearchStrategy
import pl.edu.agh.scalamas.genetic.GeneticEvaluator

/**
 * Created by Daniel on 2015-04-14.
 */
trait LabsLocalSearch extends GeneticEvaluator[LabsOps] {

  def localSearchStrategy: LocalSearchStrategy[LabsOps]

  abstract override def evaluate(s: LabsOps#Solution) = {
    localSearchStrategy.search(super.evaluate(s), new LabsLocalSearchHelper(s))
  }
}
