package org.krzywicki

import org.krzywicki.golomb.GolombProblem
import pl.edu.agh.scalamas.app.SequentialStack
import pl.edu.agh.scalamas.emas.EmasLogic

/**
 * Created by krzywick on 2015-04-01.
 */
object SequentialApp extends SequentialStack
with EmasLogic
with GolombProblem {

  def main(args: Array[String]) {
//    run(10 seconds)


    val a = Array(1,2,3).toArray
    val b = a.toArray

    println(a)
    println(b)
    println(a == b)
    println(a.equals(b))

    a(0) = 10
    println()
    println(a)
    println(b)
  }

}
