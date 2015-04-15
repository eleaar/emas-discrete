package org.krzywicki.localsearch

import pl.edu.agh.scalamas.app.AgentRuntimeComponent
import pl.edu.agh.scalamas.genetic.GeneticProblem

import scala.collection.mutable

/**
 * Created by Daniel on 2015-04-08.
 */
trait TabooSearch extends LocalSearch {

  this: AgentRuntimeComponent with GeneticProblem =>

  def localSearchStrategy = TabooSearchStrategy

  object TabooSearchStrategy extends LocalSearchStrategy[Genetic] {

    def config = agentRuntime.config.getConfig("genetic.taboo")

    val maxIterations = config.getInt("maxIterations")

    val tabooSize = config.getInt("tabooSize")

    def evaluationOrdering = genetic.ordering

    def search[C](baseEvaluation: Genetic#Evaluation, helper: LocalSearchHelper[C,Genetic]) = {
      def search0(iterationsLeft: Int,
                  baseEvaluation: Genetic#Evaluation,
                  helper: LocalSearchHelper[C,Genetic],
                  tabooList: mutable.LinkedHashSet[C]): Genetic#Evaluation = {
        if (iterationsLeft > 0) {
          val allChanges = helper.possibleChanges
          val allowedChanges = allChanges.filter(!tabooList.contains(_))
          val evaluatedChanges = allowedChanges.map(c => (c, helper.evaluateChange(c)))

          if (evaluatedChanges.nonEmpty) {
            val (bestChange, bestChangeEvaluation) = evaluatedChanges.maxBy(_._2)(evaluationOrdering)

            tabooList += bestChange
            if (tabooList.size > tabooSize) {
              // More efficient than tail, which will creates a new collection
              tabooList -= tabooList.head
            }

            if (evaluationOrdering.gt(bestChangeEvaluation, baseEvaluation)) {
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


