package org.krzywicki

import pl.edu.agh.scalamas.app.SequentialStack
import pl.edu.agh.scalamas.emas.EmasLogic
import pl.edu.agh.scalamas.genetic.RastriginProblem

import scala.concurrent.duration._

/**
 * Created by krzywick on 2015-04-01.
 */
object SequentialApp extends SequentialStack
with EmasLogic
with RastriginProblem {

  def main(args: Array[String]) {
    run(5 seconds)
  }

}
