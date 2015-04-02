package org.krzywicki.golomb

import org.apache.commons.math3.random.RandomDataGenerator
import org.krzywicki.golomb.operators.{TabuSearchStrategy, OnePointCrossover, SegmentsLengthMutation}
import org.krzywicki.golomb.problem._
import pl.edu.agh.scalamas.app.AgentRuntimeComponent
import pl.edu.agh.scalamas.genetic.{GeneticTransformer, GeneticOps, GeneticProblem}
import pl.edu.agh.scalamas.random.RandomGeneratorComponent
import pl.edu.agh.scalamas.util.Util._
import scala.collection.mutable.ArrayBuffer

/**
 * Created by krzywick on 2015-04-01.
 */

trait GolombProblem extends GeneticProblem with SegmentsLengthMutation with OnePointCrossover with TabuSearchStrategy {

  this: AgentRuntimeComponent with RandomGeneratorComponent =>

  type Genetic = GolombOps

  def genetic = new GolombOps

  def config = agentRuntime.config.getConfig("genetic.golomb")

  def countOfMarks: Int = config.getInt("countOfMarks")
  def maxMarkSize: Int = config.getInt("maxMarkSize")

  class GolombOps extends GeneticOps[GolombOps] {

    type Solution = Ruler
    type Evaluation = Int

    // Mark '0' is illegal, mark '1' will be in ALL rulers, we need to permutate only n - 1 numbers
    val possibleMarks = (2 to maxMarkSize).toList

    def generate = {
      implicit val shuffler = randomData

      // TODO refactor this stuff
      // This method generates INDIRECT representation. INDIRECT representation is shorter than DIRECT by one
      // In next step we add mark '1' so we now must select n - 2 numbers
      val marks = possibleMarks.shuffled.take(countOfMarks - 2).toBuffer
      val position = random.nextInt(marks.size + 1)
      marks.insert(position, 1)
      IndirectRuler(marks)
    }

    def evaluate(solution: Solution) = - search(solution)

    val minimal = maxMarkSize * maxMarkSize / 2

    val ordering = Ordering[Int].reverse

    def transform(solution: Solution) = {
      mutationStrategy.mutateSolution(solution)
    }

    def transform(solution1: Solution, solution2: Solution) = {
      crossoverStrategy.recombine(solution1, solution2)
    }

  }

}
