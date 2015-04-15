package org.krzywicki.golomb

import org.krzywicki.localsearch.LocalSearch
import pl.edu.agh.scalamas.app.AgentRuntimeComponent
import pl.edu.agh.scalamas.genetic.GeneticProblem
import pl.edu.agh.scalamas.random.RandomGeneratorComponent



trait GolombProblem extends GeneticProblem {
  this: AgentRuntimeComponent with RandomGeneratorComponent with LocalSearch =>

  type Genetic = GolombOps

  def genetic = new GolombOps with GolombEvaluator with GolombTransformer with GolombLocalSearch {
    def config = agentRuntime.config.getConfig("genetic.golomb")

    val countOfMarks: Int = config.getInt("countOfMarks")
    val maxMarkSize: Int = config.getInt("maxMarkSize")
    val maxIterationCount = agentRuntime.config.getInt("genetic.golomb.iterationCount")

    def random = GolombProblem.this.random
    def randomData = GolombProblem.this.randomData

    def localSearchStrategy = GolombProblem.this.localSearchStrategy
  }



}
