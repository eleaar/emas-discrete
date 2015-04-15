package org.krzywicki

import net.ceedubs.ficus.Ficus._
import org.krzywicki.golomb.GolombProblem
import org.krzywicki.localsearch.TabooSearch
import pl.edu.agh.scalamas.app.SequentialStack
import pl.edu.agh.scalamas.emas.EmasLogic

import scala.concurrent.duration._

/**
 * Created by krzywick on 2015-04-01.
 */
object GolombApp extends SequentialStack
with EmasLogic
with GolombProblem with TabooSearch {

  def main(args: Array[String]) {
    run(agentRuntime.config.as[FiniteDuration]("simulationDuration"))
  }

}
