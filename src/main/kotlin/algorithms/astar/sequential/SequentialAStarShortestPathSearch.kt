package algorithms.astar.sequential

import algorithms.ShortestPath
import algorithms.ShortestPathSearch
import algorithms.astar.heuristic.AStarAdmissibleHeuristic
import algorithms.astar.utils.ExploredPath
import algorithms.astar.utils.ExploredPathComparator
import graph.Graph
import graph.Vertex
import graph.impl.WeightedEdge
import java.util.*

public class SequentialAStarShortestPathSearch<V : Vertex, E : WeightedEdge>(
    private val graph : Graph<V, E>,
    private val heuristic: AStarAdmissibleHeuristic<V>
) : ShortestPathSearch<V> {

    override fun search(source: V, destination: V): ShortestPath<V> {
        val distance: MutableMap<V, ExploredPath<V>> = mutableMapOf()
        val visited: MutableSet<V> = mutableSetOf()
        val q: TreeSet<ExploredPath<V>> = TreeSet(ExploredPathComparator())

        distance[source] = ExploredPath.initial(source, heuristic.getDistanceEstimate(source, destination))
        q.add(distance[source]!!)

        while (q.isNotEmpty()) {
            val currentState = q.pollFirst()!!
            val currentVertex = currentState.lastVertex

            if (currentVertex == destination) return currentState.buildPath(graph)
            if (visited.contains(currentVertex)) continue
            visited.add(currentVertex)

            for (neighbour in graph.outgoingNeighboursOf(currentVertex)) {
                if (visited.contains(neighbour)) continue

                val neighbourState = currentState.expand(
                    neighbour,
                    graph.getEdge(currentVertex, neighbour)!!,
                    heuristic.getDistanceEstimate(neighbour, destination))

                val neighbourNotVisitedYet = !distance.containsKey(neighbour)
                if (neighbourNotVisitedYet) {
                    q.add(neighbourState)
                    distance[neighbour] = neighbourState
                    continue
                }

                val newNeighbourStateBetterThanExisting = neighbourState.totalDistance < distance[neighbour]!!.totalDistance
                if (newNeighbourStateBetterThanExisting) {
                    q.remove(distance[neighbour])
                    q.add(neighbourState)
                    distance[neighbour] = neighbourState
                }
            }
        }

        return ShortestPath.empty(source)
    }
}