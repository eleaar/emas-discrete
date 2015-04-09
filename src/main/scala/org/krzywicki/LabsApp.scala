package org.krzywicki

import net.ceedubs.ficus.Ficus._
import pl.edu.agh.scalamas.app.{ConcurrentStack, SynchronousEnvironment}
import pl.edu.agh.scalamas.emas.EmasLogic
import pl.edu.agh.scalamas.genetic.LabsProblem

import scala.concurrent.duration._

/**
 * Created by Daniel on 2015-04-09.
 */
object LabsApp extends ConcurrentStack("golomb") with SynchronousEnvironment
with EmasLogic
with LabsProblem {

  def main(args: Array[String]) {
    run(agentRuntime.config.as[FiniteDuration]("simulationDuration"))
  }

}
