package org.krzywicki.golomb

import org.krzywicki.golomb.problem.Ruler
import pl.edu.agh.scalamas.app.AgentRuntimeComponent
import pl.edu.agh.scalamas.genetic.{GeneticOps, GeneticProblem}
import pl.edu.agh.scalamas.random.RandomGeneratorComponent

trait GolombOps extends GeneticOps[GolombOps] {

  type Solution = Ruler
  type Evaluation = Int

}

trait GolombProblem extends GeneticProblem with GolombLocalSearchComponent with GolombTransformerComponent {
  this: AgentRuntimeComponent with RandomGeneratorComponent =>

  type Genetic = GolombOps

  def genetic = Ops

  def config = agentRuntime.config.getConfig("genetic.golomb")

  lazy val countOfMarks: Int = config.getInt("countOfMarks")
  lazy val maxMarkSize: Int = config.getInt("maxMarkSize")
  lazy val maxIterationCount = agentRuntime.config.getInt("genetic.golomb.iterationCount")

  object Ops extends GolombOps with TabooSearch with GolombTransformer

}
