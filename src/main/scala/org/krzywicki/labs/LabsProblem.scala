package org.krzywicki.labs

import org.krzywicki.localsearch.LocalSearch
import org.krzywicki.problem.IncrementalGeneticProblem
import pl.edu.agh.scalamas.app.AgentRuntimeComponent
import pl.edu.agh.scalamas.random.RandomGeneratorComponent

/**
 * Created by Daniel on 2015-04-14.
 */
trait LabsProblem extends IncrementalGeneticProblem {
  this: AgentRuntimeComponent with RandomGeneratorComponent with LocalSearch =>

  type Genetic = LabsOps

  def genetic = new LabsOps with LabsIncrementalEvaluator with LabsTransformer {
    def config = agentRuntime.config.getConfig("genetic.labs")

    val problemSize = config.getInt("problemSize")
    val mutationChance = config.getDouble("mutationChance")
    val mutationRate = config.getDouble("mutationRate")

    def random = LabsProblem.this.random

    def localSearchStrategy = LabsProblem.this.localSearchStrategy
  }
}
