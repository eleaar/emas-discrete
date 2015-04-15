package org.krzywicki.golomb

import org.krzywicki.localsearch.LocalSearchStrategy
import pl.edu.agh.scalamas.genetic.GeneticEvaluator

/**
 * Created by Daniel on 2015-04-15.
 */
trait GolombLocalSearch extends GeneticEvaluator[GolombOps] {

  def localSearchStrategy: LocalSearchStrategy[GolombOps]

  abstract override def evaluate(s: GolombOps#Solution) = {
    val marks = GolombEvaluator.decode(s)
    val helper = new GolombLocalSearchHelper(marks)
    val baseEvaluation = super.evaluate(s)

    localSearchStrategy.search(baseEvaluation, helper)
  }
}
