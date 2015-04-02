package org.krzywicki.golomb.problem

import scala.collection.mutable.ArrayBuffer


trait Ruler {

  def directRepresentation: Seq[Int]

  def indirectRepresentation: Seq[Int]

  def length: Int

  def order: Int
}


case class IndirectRuler(diffs: Seq[Int]) extends Ruler {

  val indirectRepresentation = diffs
  lazy val directRepresentation = 0 +: diffs.scanLeft(0)(_ + _)

  val length = indirectRepresentation.length + 1
  lazy val order = directRepresentation.last
}

case class DirectRuler(positions: Seq[Int]) extends Ruler {

  val directRepresentation = positions
  lazy val indirectRepresentation = {
    val size = positions.length
    val tmp = new ArrayBuffer[Int](size - 1)
    for (i <- 1 until size) {
      tmp(i - 1) = positions(i) - positions(i - 1)
    }
    tmp
  }

  val length = directRepresentation.length
  lazy val order = directRepresentation.last
}

