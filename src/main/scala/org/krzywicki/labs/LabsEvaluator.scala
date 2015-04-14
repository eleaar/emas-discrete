package org.krzywicki.labs

import org.apache.commons.math3.random.RandomGenerator
import pl.edu.agh.scalamas.genetic.GeneticEvaluator

/**
 * Created by Daniel on 2015-04-14.
 */
trait LabsEvaluator extends GeneticEvaluator[LabsOps] {
  def random: RandomGenerator

  def problemSize: Int

  def generate = Array.fill(problemSize)(random.nextBoolean())

  def minimal = 0.0

  def ordering = scala.math.Ordering.Double

  def evaluate(s: LabsOps#Solution) = {
    val size = s.size
    val correlations = Array.ofDim[Double](size - 1)

    for (i <- 0 until size - 1;
         j <- 0 until size - 1 - i) {
      correlations(i) += (if (s(j) != s(j + i + 1)) 1.0 else -1.0)
    }

    size * size / (2.0 * correlations.map(x => x * x).sum)
  }
}