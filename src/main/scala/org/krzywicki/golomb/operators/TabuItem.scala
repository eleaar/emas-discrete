/*
 * This file is a part of ogronage project.
 * (c) Copyright 2012, Specter
 * All rights reserved. Check the documentation for licensing terms.
 */
/*
 * File: TabuItem.scala
 * Created: 2012-12-18
 * Author: Specter
 */

package org.krzywicki.golomb.operators

/**
 *
 * @author Specter
 */
class TabuItem(val index: Int, val newMark: Int, val newViolation: Int) extends Comparable[TabuItem] {

  override def equals(obj: Any): Boolean = {
    if (!obj.isInstanceOf[TabuItem]) {
      return false
    }
    val that = obj.asInstanceOf[TabuItem]
    return index == that.index && newMark == that.newMark && newViolation == that.newViolation
  }

  private val hash = 1000000 * index + 1000 * newMark + newViolation

  override def hashCode(): Int = hash

  override def toString(): String = "TabuItem[%d, %d, %d]".format(index, newMark, newViolation)

  override def compareTo(other: TabuItem): Int = {
    if (newViolation == other.newViolation) {
      return 0
    } else if (newViolation < other.newViolation) {
      return -1
    } else {
      return 1
    }
  }

}
