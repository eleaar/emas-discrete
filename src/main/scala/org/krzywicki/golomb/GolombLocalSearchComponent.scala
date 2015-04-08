package org.krzywicki.golomb

import org.apache.commons.math3.random.RandomDataGenerator
import pl.edu.agh.scalamas.random.RandomGeneratorComponent

import scala.collection.mutable.HashMap

/**
 * Created by Daniel on 2015-04-08.
 */
trait GolombLocalSearchComponent extends GolombEvaluatorComponent {
  this: RandomGeneratorComponent =>

  def maxIterationCount: Int

  trait TabooSearch extends GolombEvaluator {

    case class MarkChange(index: Int, mark: Int, violation: Int)

    override def evaluate(solution: GolombOps#Solution) = {
      val filter = new TabooFilter[MarkChange](randomData)
      var bestRuler = GolombEvaluator.decode(solution)
      var bestRulerViolation = GolombEvaluator.violations(bestRuler)
      var iteration = 0
      var shouldStop = false

      while (iteration <= maxIterationCount && bestRulerViolation > 0 && !shouldStop) {
        val change = findChange(bestRuler, filter)
        change match {
          case Some(tabuItem) =>
            filter.setTaboo(tabuItem)
            if (tabuItem.violation < bestRulerViolation) {
              bestRuler = bestRuler.clone()
              bestRuler(tabuItem.index) = tabuItem.mark
              bestRulerViolation = tabuItem.violation
            }

          case None => shouldStop = true
        }
        filter.flush()
        iteration += 1
      }

      GolombEvaluator.evaluateWithViolations(bestRuler, bestRulerViolation)
    }

    private def findChange(marks: IndexedSeq[Int], filter: TabooFilter[MarkChange]): Option[MarkChange] = {
      var bestChange: Option[MarkChange] = None
      val size = marks.length
      val distances = GolombEvaluator.distancesMap(marks)

      // Changes to '0' mark moves the ruler
      // Changing from 1st to (n - 1)th mark
      var i = 1
      while (i < size - 1) {
        var j = marks(i - 1) + 1
        val maxIdx = marks(i + 1) - 1
        while (j <= maxIdx) {
          val currentChange = createChange(marks, distances, i, j)
          bestChange = chooseBetterChange(currentChange, bestChange, filter)
          j += 1
        }
        i += 1
      }
      // Changing last mark
      i = marks(size - 2) + 1
      val maxIdx = marks(size - 1) - 1
      while (i <= maxIdx) {
        val currentChange = createChange(marks, distances, size - 1, i)
        bestChange = chooseBetterChange(currentChange, bestChange, filter)
        i += 1
      }

      return bestChange
    }

    private def createChange(marks: IndexedSeq[Int], distances: Array[Int], index: Int, newMark: Int): MarkChange = {
      val newDistances = GolombEvaluator.distancesMapAfterFlip(marks, distances, index, newMark)
      MarkChange(index, newMark, GolombEvaluator.distanceViolations(newDistances))
    }

    private def chooseBetterChange(currentChange: MarkChange, bestChange: Option[MarkChange], filter: TabooFilter[MarkChange]): Option[MarkChange] = {
      if (filter.isTaboo(currentChange)) {
        bestChange
      } else {
        bestChange match {
          case None => Some(currentChange)
          case Some(previousChange) if currentChange.violation < previousChange.violation => Some(currentChange)
          case Some(previousChange) if currentChange.violation >= previousChange.violation => bestChange
        }
      }
    }
  }

}

class TabooFilter[G](randomData: RandomDataGenerator) {

  private var iteration = 0
  private val map = new HashMap[G, Int]

  def setTaboo(item: G) {
    val duration = randomData.nextInt(4, 100)
    map.put(item, iteration + duration)
  }

  def isTaboo(item: G) = {
    map.get(item) match {
      case None => false
      case Some(limit) if iteration > limit => false
      case Some(limit) if iteration <= limit => true
    }
  }

  def flush(): Unit = {
    iteration += 1
  }
}
