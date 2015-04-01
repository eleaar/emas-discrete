/*
 * This file is a part of ogronage project.
 * (c) Copyright 2012, Specter
 * All rights reserved. Check the documentation for licensing terms.
 */
/*
 * File: Ruler.scala
 * Created: 2012-12-13
 * Author: Specter
 */

package org.krzywicki.golomb.problem

/**
 * Methods that returns ruler's representation actually returns reference to stored array!
 *
 * @author Specter
 */
class Ruler(marks: Array[Int], directed: Boolean) {

  def this(marks: Array[Int]) = this(marks, true)

  private var directRepresentation: Array[Int] = _

  private var indirectRepresentation: Array[Int] = _

  if (directed) {
    fillFromDirect(marks)
  } else {
    fillFromIndirect(marks)
  }

  // var wrapper = new RulerWrapper(this)

  def length() = directRepresentation(directRepresentation.length - 1)

  def marksCount() = directRepresentation.length

  def getRepresentation() = indirectRepresentation

  def getDirectRepresentation() = directRepresentation

  def setRepresentation(indirectRepresentation: Array[Int]) {
    fillFromIndirect(indirectRepresentation)
  }

  private def fillFromIndirect(marks: Array[Int]) {
    val size = marks.length
    indirectRepresentation = marks.clone
    directRepresentation = new Array[Int](size + 1)
    for (i <- 0 until size) {
      directRepresentation(i + 1) = marks(i) + directRepresentation(i)
    }
    // wrapper = new RulerWrapper(this)
  }

  def setDirectRepresentation(directRepresentation: Array[Int]) {
    fillFromDirect(directRepresentation)
  }

  private def fillFromDirect(marks: Array[Int]) {
    val size = marks.length
    directRepresentation = marks.clone
    indirectRepresentation = new Array[Int](size - 1)
    for (i <- 1 until size) {
      indirectRepresentation(i - 1) = marks(i) - marks(i - 1)
    }
    // wrapper = new RulerWrapper(this)
  }

}
