package org.krzywicki.app

import net.ceedubs.ficus.Ficus._
import org.krzywicki.labs.LabsProblem
import org.krzywicki.localsearch.RandomMutationHillClimbing
import pl.edu.agh.scalamas.app.SequentialStack
import pl.edu.agh.scalamas.emas.EmasLogic

import scala.concurrent.duration.FiniteDuration

/**
 * Created by Daniel on 2015-04-14.
 */
object LabsApp extends SequentialStack
with EmasLogic
with LabsProblem with RandomMutationHillClimbing {

  def main(args: Array[String]) {
    run(agentRuntime.config.as[FiniteDuration]("simulationDuration"))
  }

}
