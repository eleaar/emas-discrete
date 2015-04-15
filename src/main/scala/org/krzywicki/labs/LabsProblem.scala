package org.krzywicki.labs

import org.krzywicki.localsearch.LocalSearch
import pl.edu.agh.scalamas.app.AgentRuntimeComponent
import pl.edu.agh.scalamas.genetic.GeneticProblem
import pl.edu.agh.scalamas.random.RandomGeneratorComponent

/**
 * Created by Daniel on 2015-04-14.
 */
trait LabsProblem extends GeneticProblem {
  this: AgentRuntimeComponent with RandomGeneratorComponent with LocalSearch =>

  type Genetic = LabsOps

  def genetic = new LabsOps with LabsEvaluator with LabsTransformer with LabsLocalSearch {
    def config = agentRuntime.config.getConfig("genetic.labs")

    val problemSize = config.getInt("problemSize")
    val mutationChance = config.getDouble("mutationChance")
    val mutationRate = config.getDouble("mutationRate")

    def random = LabsProblem.this.random

    def localSearchStrategy = LabsProblem.this.localSearchStrategy
  }
}
