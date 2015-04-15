package org.krzywicki.golomb

import org.apache.commons.math3.random.{RandomDataGenerator, RandomGenerator}
import pl.edu.agh.scalamas.genetic.GeneticEvaluator
import pl.edu.agh.scalamas.util.Util._

import scala.collection.mutable.ArrayBuffer
import scala.math.Ordering

trait GolombEvaluator extends GeneticEvaluator[GolombOps] {

  def marksNumber: Int

  def maxMarkDistance: Int

  def randomData: RandomDataGenerator

  def random: RandomGenerator

  // Mark '0' is illegal, mark '1' will be in ALL rulers, we need to permutate only n - 1 numbers
  lazy val possibleMarks = (2 to maxMarkDistance).toList

  def generate = {
    implicit val shuffler = randomData

    // TODO refactor this stuff
    // This method generates INDIRECT representation. INDIRECT representation is shorter than DIRECT by one
    // In next step we add mark '1' so we now must select n - 2 numbers
    val marks = ArrayBuffer() ++ possibleMarks.shuffled.take(marksNumber - 2)
    val position = random.nextInt(marks.size + 1)
    marks.insert(position, 1)
    marks.toArray
  }

  lazy val minimal = maxMarkDistance * maxMarkDistance / 2

  lazy val ordering = Ordering[Int].reverse

  def evaluate(solution: GolombOps#Solution) = {
    val marks = GolombEvaluator.decode(solution).toArray
    val violations = GolombEvaluator.violations(marks)
    GolombEvaluator.evaluateWithViolations(marks, violations)
  }
}

object GolombEvaluator {

  /**
   * Translate an indirect ruler (sequence of mark distances) into a direct ruler (sequence of mark positions).
   */
  def decode(solution: GolombOps#Solution) = solution.scanLeft(0)(_ + _)

  def evaluateWithViolations(marks: IndexedSeq[Int], violations: Int) = {
    marks.last + 375 * violations
  }

  def violations(marks: IndexedSeq[Int]) = {
    val distances = distancesMap(marks)
    val violations = distanceViolations(distances)
    violations
  }

  /**
   * The performance of this method is REALLY IMPORTANT, because it is used in fitness computing. The implementation uses array instead of Map, because it is
   * more than two times faster. Implementation also uses while loops because for loops in Scala uses iterable or range, which needs more time than simple
   * incrementation of variable.
   *
   * This method computes the number of constraint violation, i.e. the number of repeated mark distances.
   */
  def distanceViolations(distances: Array[Int]): Int = {
    var violationCount = 0;
    var i = 0
    while (i < distances.length) {
      if (distances(i) > 1) {
        violationCount += distances(i) - 1
      }
      i += 1
    }
    violationCount
  }

  /**
   * Compute all distances in the ruler and return an array-based map with the number of each distance in the ruler.
   */
  def distancesMap(marks: IndexedSeq[Int]): Array[Int] = {
    val size = marks.length

    // For efficiency, we use an array to store the distances
    val rulerLength = marks(size - 1)
    val distances = new Array[Int](rulerLength + 1)

    // We compute the distance between each pair of marks and increment the corresponding element
    var i = 0
    while (i < size - 1) {
      var j = i + 1
      while (j < size) {
        // marks are sorted so this is safe
        val idx = marks(j) - marks(i)
        distances(idx) += 1
        j += 1
      }
      i += 1
    }

    distances
  }

  /**
   * Computes the new distances after a single change of a mark at position 'index' to a value 'newMark', if the previous distances were known.
   * More efficient than recomputing the entire distances map.
   *
   * The new value should not break the representation, i.e. marks(index - 1) < newMark < marks(index + 1)
   */
  def distancesMapAfterFlip(marks: IndexedSeq[Int], distances: Array[Int], index: Int, newMark: Int): Array[Int] = {
    val oldMark = marks(index)
    val marksDistance = oldMark - newMark // always positive

    val newDistances = distances.clone()
    var i = 0
    while (i < index) {
      val idx = marks(index) - marks(i)
      newDistances(idx) -= 1
      newDistances(idx - marksDistance) += 1
      i += 1
    }
    // i == index
    i += 1
    while (i < marks.size) {
      val idx = marks(i) - marks(index)
      newDistances(idx) -= 1
      newDistances(idx + marksDistance) += 1
      i += 1
    }

    newDistances
  }

}
