package org.krzywicki.golomb

import org.krzywicki.golomb.problem.{RulerEvaluator, Ruler}
import pl.edu.agh.scalamas.genetic.{GeneticOps, GeneticProblem}
import pl.edu.agh.scalamas.random.RandomGeneratorComponent
import pl.edu.agh.scalamas.util.Util._

import scala.collection.mutable.ArrayBuffer

/**
 * Created by krzywick on 2015-04-01.
 */
trait GolombProblem extends GeneticProblem {

  this: RandomGeneratorComponent =>

  type Genetic = GolombOps

  def genetic = new GolombOps

  class GolombOps extends GeneticOps[GolombOps] {

    type Solution = Ruler
    type Evaluation = Double

    def countOfMarks: Int = 0
    def maxMarkSize: Int = 0

    // Mark '0' is illegal, mark '1' will be in ALL rulers, we need to permutate only n - 1 numbers
    val possibleMarks = (2 to maxMarkSize).toList

    def generate = {
      implicit val shuffler = randomData

      // TODO refactor this stuff
      // This method generates INDIRECT representation. INDIRECT representation is shorter than DIRECT by one
      // In next step we add mark '1' so we now must select n - 2 numbers
      val marks = possibleMarks.shuffled.take(countOfMarks - 2).toArray
      val position = random.nextInt(marks.size + 1)
      marks(position) = 1
      new Ruler(marks, false)
    }

    def evaluate(solution: Solution) = RulerEvaluator.evaluate(solution)

    val minimal = 10000.0

    val ordering = Ordering[Double].reverse

    def transform(solution: Solution) = solution

    def transform(solution1: Solution, solution2: Solution) = (solution1, solution2)

  }

}
