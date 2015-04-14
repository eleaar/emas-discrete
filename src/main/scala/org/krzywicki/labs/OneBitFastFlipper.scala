package org.krzywicki.labs

/**
 * Created by Daniel on 2015-04-14.
 */
case class OneBitFastFlipper(s: Array[Boolean]) {
   private[this] val size = s.size
   private[this] val computedProducts = Array.ofDim[Double](size - 1, size - 1)
   private[this] val correlations = Array.ofDim[Double](size - 1)

   for (i <- 0 until size - 1;
        j <- 0 until size - 1 - i) {
     computedProducts(i)(j) = if (s(j) != s(j + i + 1)) 1.0 else -1.0
     correlations(i) += computedProducts(i)(j)
   }

   def currentEnergy = size * size / (2.0 * correlations.map(x => x * x).sum)

   def energyWithFlipped(flipBitIndex: Int): Double = {
     val energies = for (k <- 0 until size - 1) yield {
       var correlation = correlations(k)
       if (k < size - flipBitIndex - 1) correlation -= 2.0 * computedProducts(k)(flipBitIndex)
       if (k < flipBitIndex) correlation -= 2.0 * computedProducts(k)(flipBitIndex - k - 1)
       correlation * correlation
     }
     size * size / (2.0 * energies.sum)
   }
 }
