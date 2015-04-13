package org.krzywicki.problem

import pl.edu.agh.scalamas.genetic.GeneticOps

/**
 * Created by Daniel on 2015-04-13.
 */
trait IncrementalGeneticOps[G <: IncrementalGeneticOps[G]] extends GeneticOps[G] with IncrementalGeneticEvaluator[G]

