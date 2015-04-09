package org.krzywicki

import net.ceedubs.ficus.Ficus._
import org.krzywicki.golomb.GolombProblem
import pl.edu.agh.scalamas.app.{ConcurrentStack, SynchronousEnvironment}
import pl.edu.agh.scalamas.emas.EmasLogic

import scala.concurrent.duration._

/**
 * Created by krzywick on 2015-04-01.
 */
object GolombApp extends ConcurrentStack("golomb") with SynchronousEnvironment
with EmasLogic
with GolombProblem {

  def main(args: Array[String]) {
    run(agentRuntime.config.as[FiniteDuration]("simulationDuration"))
  }

}
