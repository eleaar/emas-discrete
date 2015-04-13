package org.krzywicki.localsearch.taboo

import org.krzywicki.problem.IncrementalGeneticProblem
import pl.edu.agh.scalamas.app.AgentRuntimeComponent

import scala.collection.mutable

/**
 * Created by Daniel on 2015-04-08.
 */
trait TabooSearch {

  this: AgentRuntimeComponent with IncrementalGeneticProblem =>

  def localSearchStrategy = TabooSearchStrategy

  object TabooSearchStrategy {

    def config = agentRuntime.config.getConfig("genetic.taboo")

    val maxIterations = config.getInt("maxIterations")

    val tabooSize = config.getInt("tabooSize")

    def search(solution: Genetic#Solution) = {
      def search0(iterationsLeft: Int,
                  bestSolution: Genetic#Solution,
                  bestEvaluation: Genetic#Evaluation,
                  tabooList: mutable.LinkedHashSet[Genetic#Change]): (Genetic#Solution, Genetic#Evaluation) = {
        if (iterationsLeft > 0) {
          val allChanges = genetic.possibleChanges(bestSolution)
          val allowedChanges = allChanges.filter(!tabooList.contains(_))
          val evaluatedChanges = allowedChanges.map(c => (c, genetic.evaluateChange(bestSolution, c)))

          if (evaluatedChanges.nonEmpty) {
            val (bestChange, bestChangeEvaluation) = evaluatedChanges.maxBy(_._2)(genetic.ordering)

            tabooList += bestChange
            if (tabooList.size > tabooSize) {
              // More efficient than tail, which will creates a new collection
              tabooList -= tabooList.head
            }

            val (newBestSolution, newBestEvaluation) =
              if (genetic.ordering.gt(bestChangeEvaluation, bestEvaluation)) {
                (genetic.applyChange(bestSolution, bestChange), bestChangeEvaluation)
              } else {
                (bestSolution, bestEvaluation)
              }
            return search0(iterationsLeft - 1, newBestSolution, newBestEvaluation, tabooList)
          }
          return (bestSolution, bestEvaluation)
        }
        search0(maxIterations,
          solution,
          genetic.evaluate(solution),
          new mutable.LinkedHashSet[Genetic#Change]()
        )
      }
    }
  }

}


