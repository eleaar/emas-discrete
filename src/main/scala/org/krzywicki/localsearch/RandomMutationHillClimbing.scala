package org.krzywicki.localsearch

import org.krzywicki.problem.IncrementalGeneticProblem
import pl.edu.agh.scalamas.app.AgentRuntimeComponent
import pl.edu.agh.scalamas.random.RandomGeneratorComponent

/**
 * Created by Daniel on 2015-04-13.
 */
trait RandomMutationHillClimbing extends LocalSearch {
  this: AgentRuntimeComponent with IncrementalGeneticProblem with RandomGeneratorComponent =>

  def localSearchStrategy = RandomMutationHillClimbingStrategy

  object RandomMutationHillClimbingStrategy extends LocalSearchStrategy[Genetic] {

    def config = agentRuntime.config.getConfig("genetic.rmhc")

    val maxIterations = config.getInt("maxIterations")

    def search(solution: Genetic#Solution, evaluation: Genetic#Evaluation) = {
      def search0(iterationsLeft: Int,
                  possibleChanges: IndexedSeq[Genetic#Change],
                  bestSolution: Genetic#Solution,
                  bestEvaluation: Genetic#Evaluation): (Genetic#Solution, Genetic#Evaluation) = {
        if (iterationsLeft > 0 && possibleChanges.nonEmpty) {
          val idx = random.nextInt(possibleChanges.size)
          val change = possibleChanges(idx)
          val changeEvaluation = genetic.evaluateChange(bestSolution, change)

          if (genetic.ordering.gt(changeEvaluation, bestEvaluation)) {
            val newBestSolution = genetic.applyChange(bestSolution, change)
            val newPossibleChanges = genetic.possibleChanges(newBestSolution).toIndexedSeq
            search0(iterationsLeft - 1, newPossibleChanges, newBestSolution, changeEvaluation)
          } else {
            val newPossibleChanges = possibleChanges.take(idx) ++ possibleChanges.drop(idx + 1)
            search0(iterationsLeft - 1, newPossibleChanges, bestSolution, bestEvaluation)
          }
        }
        (bestSolution, bestEvaluation)
      }

      search0(maxIterations,
        genetic.possibleChanges(solution).toIndexedSeq,
        solution,
        evaluation)
    }

  }

}
