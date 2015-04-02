package org.krzywicki.golomb.problem

object RulerEvaluator {

  def evaluate(ruler: Ruler) = {
    val marks = decode(ruler).toArray
    val rulerLength = marks.last
    rulerLength + 375 * distanceViolations(marks)
  }

  /**
   * Translate an indirect ruler (sequence of mark distances) into a direct ruler (sequence of mark positions).
   */
  def decode(ruler: Ruler) = ruler.indirectRepresentation.scanLeft(0)(_ + _)


  /**
   * The performance of this method is REALLY IMPORTANT, because it is used in fitness computing. The implementation uses array instead of Map, because it is
   * more than two times faster. Implementation also uses while loops because for loops in Scala uses iterable or range, which needs more time than simple
   * incrementation of variable.
   * 
   * This method computes the number of constraint violation, i.e. the number of repeated mark distances.
   */
  def distanceViolations(marks: Array[Int]): Int = {
    val size = marks.length

    // For efficiency, we use an array to store the distances
    val rulerLength = marks(size - 1)
    val distances = new Array[Int](rulerLength + 1)

    // We compute the distance between each pair of marks and increment the corresponding element
    {
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
    }

    var violationCount = 0;
    {
      var i = 0
      while (i < distances.length) {
        if (distances(i) > 1) {
          violationCount += distances(i) - 1
        }
        i += 1
      }
    }

    violationCount
  }

}
