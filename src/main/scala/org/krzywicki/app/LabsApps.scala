package org.krzywicki.app

import net.ceedubs.ficus.Ficus._
import org.krzywicki.app.LabsConfigs.{ConcurrentConfig, HybridConfig, SequentialConfig}
import org.krzywicki.labs.LabsProblem
import org.krzywicki.localsearch._
import pl.edu.agh.scalamas.app._
import pl.edu.agh.scalamas.emas.EmasLogic
import pl.edu.agh.scalamas.random.RandomGeneratorComponent
import pl.edu.agh.scalamas.stats.StatsFactoryComponent

import scala.concurrent.duration.FiniteDuration

object LabsConfigs {

  trait EmasLabs extends EmasLogic with LabsProblem {
    this: AgentRuntimeComponent with RandomGeneratorComponent with StatsFactoryComponent with LocalSearch =>

    def run(duration: FiniteDuration)

    def main(args: Array[String]) {
      run(agentRuntime.config.as[FiniteDuration]("simulationDuration"))
    }
  }

  abstract class SequentialConfig extends SequentialStack with EmasLabs {
    this: LocalSearch =>
  }

  abstract class HybridConfig extends ConcurrentStack("labs") with SynchronousEnvironment with EmasLabs {
    this: LocalSearch =>
  }

  abstract class ConcurrentConfig extends ConcurrentStack("labs") with AsynchronousEnvironment with EmasLabs {
    this: LocalSearch =>
  }
}

object SequentialNoLocal extends SequentialConfig with NoLocalSearch
object SequentialTaboo extends SequentialConfig with TabooSearch
object SequentialRMHC extends SequentialConfig with RandomMutationHillClimbing
object SequentialSDLS extends SequentialConfig with SteepestDescend

object HybridNoLocal extends HybridConfig with NoLocalSearch
object HybridTaboo extends HybridConfig with TabooSearch
object HybridRMHC extends HybridConfig with RandomMutationHillClimbing
object HybridSDLS extends HybridConfig with SteepestDescend

object ConcurrentNoLocal extends ConcurrentConfig with NoLocalSearch
object ConcurrentTaboo extends ConcurrentConfig with TabooSearch
object ConcurrentRMHC extends ConcurrentConfig with RandomMutationHillClimbing
object ConcurrentSDLS extends ConcurrentConfig with SteepestDescend

