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

    val marksNumber: Int = config.getInt("marksNumber")
    val maxMarkDistance: Int = config.getInt("maxMarkDistance")

    def random = GolombProblem.this.random

    def randomData = GolombProblem.this.randomData

    def localSearchStrategy = GolombProblem.this.localSearchStrategy
  }

}
