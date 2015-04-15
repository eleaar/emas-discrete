package org.krzywicki.localsearch

import pl.edu.agh.scalamas.app.AgentRuntimeComponent
import pl.edu.agh.scalamas.genetic.GeneticOps

/**
 * Created by Daniel on 2015-04-13.
 */
trait SteepestDescend extends LocalSearch {
  this: AgentRuntimeComponent =>

  def localSearchStrategy = SteepestDescendStrategy

  object SteepestDescendStrategy extends LocalSearchStrategy {

    def config = agentRuntime.config.getConfig("genetic.sd")

    val maxIterations = config.getInt("maxIterations")

    def search[C, G <: GeneticOps[G]](baseEvaluation: G#Evaluation, helper: LocalSearchHelper[C, G])(implicit ordering: Ordering[G#Evaluation]) = {
      def search0(iterationsLeft: Int,
                  baseEvaluation: G#Evaluation,
                  helper: LocalSearchHelper[C, G]): G#Evaluation = {
        if (iterationsLeft > 0) {
          val allChanges = helper.possibleChanges
          val evaluatedChanges = allChanges.map(c => (c, helper.evaluateChange(c)))

          if (evaluatedChanges.nonEmpty) {
            val (bestChange, bestChangeEvaluation) = evaluatedChanges.maxBy(_._2)(ordering)

            if (ordering.gt(bestChangeEvaluation, baseEvaluation)) {
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
