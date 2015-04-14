package org.krzywicki.localsearch

import org.krzywicki.problem.IncrementalGeneticProblem
import pl.edu.agh.scalamas.app.AgentRuntimeComponent

/**
 * Created by Daniel on 2015-04-13.
 */
trait SteepestDescend extends LocalSearch {
  this: AgentRuntimeComponent with IncrementalGeneticProblem  =>

  def localSearchStrategy = SteepestDescendStrategy

  object SteepestDescendStrategy extends LocalSearchStrategy {

    def config = agentRuntime.config.getConfig("genetic.sd")

    val maxIterations = config.getInt("maxIterations")

    def search(solution: Genetic#Solution) = {
      def search0(iterationsLeft: Int,
                  bestSolution: Genetic#Solution,
                  bestEvaluation: Genetic#Evaluation): (Genetic#Solution, Genetic#Evaluation) = {
        if (iterationsLeft > 0) {
          val allChanges = genetic.possibleChanges(bestSolution)
          val evaluatedChanges = allChanges.map(c => (c, genetic.evaluateChange(bestSolution, c)))

          if (evaluatedChanges.nonEmpty) {
            val (bestChange, bestChangeEvaluation) = evaluatedChanges.maxBy(_._2)(genetic.ordering)

            if (genetic.ordering.gt(bestChangeEvaluation, bestEvaluation)) {
              val newBestSolution = genetic.applyChange(bestSolution, bestChange)
              return search0(iterationsLeft - 1, newBestSolution, bestChangeEvaluation)
            }
          }
        }
        return (bestSolution, bestEvaluation)
      }

      search0(maxIterations,
        solution,
        genetic.evaluate(solution))
    }

  }

}
