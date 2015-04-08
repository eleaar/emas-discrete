package org.krzywicki.golomb.problem

object RulerEvaluator {

  def evaluate(ruler: Ruler) = {
    val marks = decode(ruler).toArray
    val rulerLength = marks.last

    rulerLength + 375 * violations(marks)
  }

  /**
   * Translate an indirect ruler (sequence of mark distances) into a direct ruler (sequence of mark positions).
   */
  def decode(ruler: Ruler) = ruler.indirectRepresentation.scanLeft(0)(_ + _)

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
