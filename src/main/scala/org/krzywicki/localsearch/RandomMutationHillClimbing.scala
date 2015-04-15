package org.krzywicki.localsearch

import pl.edu.agh.scalamas.app.AgentRuntimeComponent
import pl.edu.agh.scalamas.genetic.GeneticProblem
import pl.edu.agh.scalamas.random.RandomGeneratorComponent

/**
 * Created by Daniel on 2015-04-13.
 */
trait RandomMutationHillClimbing extends LocalSearch {
  this: AgentRuntimeComponent with GeneticProblem with RandomGeneratorComponent =>

  def localSearchStrategy = RandomMutationHillClimbingStrategy

  object RandomMutationHillClimbingStrategy extends LocalSearchStrategy[Genetic] {

    def config = agentRuntime.config.getConfig("genetic.rmhc")

    val maxIterations = config.getInt("maxIterations")

    def evaluationOrdering = genetic.ordering

    def search[C](baseEvaluation: Genetic#Evaluation, helper: LocalSearchHelper[C,Genetic]) = {
      def search0(iterationsLeft: Int,
                  baseEvaluation: Genetic#Evaluation,
                  helper: LocalSearchHelper[C,Genetic],
                  possibleChanges: IndexedSeq[C]): Genetic#Evaluation = {
        if (iterationsLeft > 0 && possibleChanges.nonEmpty) {
          val idx = random.nextInt(possibleChanges.size)
          val change = possibleChanges(idx)
          val newEvaluation = helper.evaluateChange(change)

          if (evaluationOrdering.gt(newEvaluation, baseEvaluation)) {
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
