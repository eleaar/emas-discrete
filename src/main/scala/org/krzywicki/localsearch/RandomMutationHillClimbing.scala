package org.krzywicki.localsearch

import pl.edu.agh.scalamas.app.AgentRuntimeComponent
import pl.edu.agh.scalamas.genetic.GeneticOps
import pl.edu.agh.scalamas.random.RandomGeneratorComponent

/**
 * Created by Daniel on 2015-04-13.
 */
trait RandomMutationHillClimbing extends LocalSearch {
  this: AgentRuntimeComponent with RandomGeneratorComponent =>

  def localSearchStrategy = RandomMutationHillClimbingStrategy

  object RandomMutationHillClimbingStrategy extends LocalSearchStrategy {

    def config = agentRuntime.config.getConfig("genetic.rmhc")

    val maxIterations = config.getInt("maxIterations")

    def search[C, G <: GeneticOps[G]](baseEvaluation: G#Evaluation, helper: LocalSearchHelper[C, G])(implicit ordering: Ordering[G#Evaluation]) = {
      def search0(iterationsLeft: Int,
                  baseEvaluation: G#Evaluation,
                  helper: LocalSearchHelper[C, G],
                  possibleChanges: IndexedSeq[C]): G#Evaluation = {
        if (iterationsLeft > 0 && possibleChanges.nonEmpty) {
          val idx = random.nextInt(possibleChanges.size)
          val change = possibleChanges(idx)
          val newEvaluation = helper.evaluateChange(change)

          if (ordering.gt(newEvaluation, baseEvaluation)) {
            val newHelper = helper.applyChange(change)
            val newPossibleChanges = newHelper.possibleChanges.toIndexedSeq
            return search0(iterationsLeft - 1, newEvaluation, newHelper, newPossibleChanges)
          } else {
            val newPossibleChanges = possibleChanges.take(idx) ++ possibleChanges.drop(idx + 1)
            return search0(iterationsLeft - 1, baseEvaluation, helper, newPossibleChanges)
          }
        }
        return baseEvaluation
      }
      search0(maxIterations,
        baseEvaluation,
        helper,
        helper.possibleChanges.toIndexedSeq)
    }

  }

}
