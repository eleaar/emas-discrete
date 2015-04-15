package org.krzywicki.app

import net.ceedubs.ficus.Ficus._
import pl.edu.agh.scalamas.app._
import pl.edu.agh.scalamas.emas.EmasLogic
import pl.edu.agh.scalamas.genetic.GeneticProblem

import scala.concurrent.duration.FiniteDuration

object Configs {

  trait Main {
    this: AgentRuntimeComponent =>

    def run(duration: FiniteDuration)

    def main(args: Array[String]) {
      run(agentRuntime.config.as[FiniteDuration]("simulationDuration"))
    }
  }

  abstract class SequentialConfig extends SequentialStack with EmasLogic with Main {
    this: GeneticProblem =>
  }

  abstract class HybridConfig extends ConcurrentStack("emas") with SynchronousEnvironment with EmasLogic with Main {
    this: GeneticProblem =>
  }

  abstract class ConcurrentConfig extends ConcurrentStack("emas") with AsynchronousEnvironment with EmasLogic with Main {
    this: GeneticProblem =>
  }

}
