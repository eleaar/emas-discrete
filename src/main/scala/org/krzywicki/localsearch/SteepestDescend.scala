package org.krzywicki.localsearch

import pl.edu.agh.scalamas.app.AgentRuntimeComponent
import pl.edu.agh.scalamas.genetic.GeneticProblem

/**
 * Created by Daniel on 2015-04-13.
 */
trait SteepestDescend extends LocalSearch {
  this: AgentRuntimeComponent with GeneticProblem  =>

  def localSearchStrategy = SteepestDescendStrategy

  object SteepestDescendStrategy extends LocalSearchStrategy[Genetic] {

    def config = agentRuntime.config.getConfig("genetic.sd")

    val maxIterations = config.getInt("maxIterations")

    def evaluationOrdering = genetic.ordering

    def search[C](baseEvaluation: Genetic#Evaluation, helper: LocalSearchHelper[C,Genetic]) = {
      def search0(iterationsLeft: Int,
                  baseEvaluation: Genetic#Evaluation,
                  helper: LocalSearchHelper[C,Genetic]): Genetic#Evaluation = {
        if (iterationsLeft > 0) {
          val allChanges = helper.possibleChanges
          val evaluatedChanges = allChanges.map(c => (c, helper.evaluateChange(c)))

          if (evaluatedChanges.nonEmpty) {
            val (bestChange, bestChangeEvaluation) = evaluatedChanges.maxBy(_._2)(evaluationOrdering)

            if (evaluationOrdering.gt(bestChangeEvaluation, baseEvaluation)) {
              val newHelper = helper.applyChange(bestChange)
              return search0(iterationsLeft - 1, bestChangeEvaluation, newHelper)
            }
          }
        }
        return baseEvaluation
      }
      search0(maxIterations,
        baseEvaluation,
        helper)
    }

  }

}
