package org.krzywicki.labs

import org.apache.commons.math3.random.RandomGenerator
import pl.edu.agh.scalamas.genetic.GeneticTransformer

/**
 * Created by Daniel on 2015-04-14.
 */
trait LabsTransformer extends GeneticTransformer[LabsOps]{
  def random: RandomGenerator

  def mutationChance: Double
  def mutationRate: Double

  def transform(solution: LabsOps#Solution) =
    mutateSolution(solution)

  def transform(solution1: LabsOps#Solution, solution2: LabsOps#Solution) =
    mutateSolutions(recombineSolutions(solution1, solution2))

  def mutateSolution(s: LabsOps#Solution) =
    if (random.nextDouble() < mutationChance)
      s.map(f => if (random.nextDouble() < mutationRate) !f else f)
    else
      s

  def mutateSolutions(s: (LabsOps#Solution, LabsOps#Solution)) = s match {
    case (solution1, solution2) => (mutateSolution(solution1), mutateSolution(solution2))
  }

  def recombineSolutions(s1: LabsOps#Solution, s2: LabsOps#Solution): (LabsOps#Solution, LabsOps#Solution) = {
    val (s3, s4) = s1.zip(s2).map(recombineFeatures).unzip
    (s3.toArray, s4.toArray)
  }

  def recombineFeatures(features: (LabsOps#Feature, LabsOps#Feature)): (LabsOps#Feature, LabsOps#Feature) = {
    val (a, b) = features
    if (a != b && random.nextBoolean()) {
      (b, a)
    } else {
      (a, b)
    }
  }
}
