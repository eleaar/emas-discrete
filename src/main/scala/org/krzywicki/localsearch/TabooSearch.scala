package org.krzywicki.localsearch

import pl.edu.agh.scalamas.app.AgentRuntimeComponent
import pl.edu.agh.scalamas.genetic.{GeneticOps, GeneticProblem}

import scala.collection.mutable

/**
 * Created by Daniel on 2015-04-08.
 */
trait TabooSearch extends LocalSearch {

  this: AgentRuntimeComponent with GeneticProblem =>

  def localSearchStrategy = TabooSearchStrategy

  object TabooSearchStrategy extends LocalSearchStrategy {

    def config = agentRuntime.config.getConfig("genetic.taboo")

    val maxIterations = config.getInt("maxIterations")

    val tabooSize = config.getInt("tabooSize")

    def search[C, G <: GeneticOps[G]](baseEvaluation: G#Evaluation, helper: LocalSearchHelper[C,G])(implicit ordering: Ordering[G#Evaluation]) = {
      def search0(iterationsLeft: Int,
                  baseEvaluation: G#Evaluation,
                  helper: LocalSearchHelper[C,G],
                  tabooList: mutable.LinkedHashSet[C]): G#Evaluation = {
        if (iterationsLeft > 0) {
          val allChanges = helper.possibleChanges
          val allowedChanges = allChanges.filter(!tabooList.contains(_))
          val evaluatedChanges = allowedChanges.map(c => (c, helper.evaluateChange(c)))

          if (evaluatedChanges.nonEmpty) {
            val (bestChange, bestChangeEvaluation) = evaluatedChanges.maxBy(_._2)(ordering)

            tabooList += bestChange
            if (tabooList.size > tabooSize) {
              // More efficient than tail, which will creates a new collection
              tabooList -= tabooList.head
            }

            if (ordering.gt(bestChangeEvaluation, baseEvaluation)) {
              val newHelper = helper.applyChange(bestChange)
              return search0(iterationsLeft - 1, bestChangeEvaluation, newHelper, tabooList)
            }
          }
        }
        return baseEvaluation
      }
      search0(maxIterations,
        baseEvaluation,
        helper,
        new mutable.LinkedHashSet[C]()
      )
    }
  }

}


