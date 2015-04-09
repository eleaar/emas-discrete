package org.krzywicki

import org.krzywicki.golomb.GolombProblem
import pl.edu.agh.scalamas.app.SequentialStack
import pl.edu.agh.scalamas.emas.EmasLogic

import scala.concurrent.duration._
import net.ceedubs.ficus.Ficus._

/**
 * Created by krzywick on 2015-04-01.
 */
object SequentialApp extends SequentialStack
with EmasLogic
with GolombProblem {

  def main(args: Array[String]) {
    run(agentRuntime.config.as[FiniteDuration]("simulationDuration"))
  }

}
