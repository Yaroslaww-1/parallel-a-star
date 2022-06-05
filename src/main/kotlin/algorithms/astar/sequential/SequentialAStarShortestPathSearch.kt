package algorithms.astar.sequential

import algorithms.ShortestPath
import algorithms.ShortestPathSearch
import algorithms.astar.heuristic.AStarAdmissibleHeuristic
import algorithms.astar.utils.*
import algorithms.astar.utils.ExpandedVertices
import algorithms.astar.utils.ExploredPath
import algorithms.astar.utils.MinimalPaths
import algorithms.astar.utils.PathsQueue
import graph.Graph
import graph.Vertex
import graph.impl.WeightedEdge
import java.util.*

public class SequentialAStarShortestPathSearch<V : Vertex, E : WeightedEdge>(
    private val graph : Graph<V, E>,
    private val heuristic: AStarAdmissibleHeuristic<V>
) : ShortestPathSearch<V> {

    override fun search(source: V, destination: V): ShortestPath<V> {
        val minimalPaths = MinimalPaths<V>()
        val expandedVertices: ExpandedVertices<V> = mutableSetOf()
        val q = PathsQueue<V>()

        minimalPaths[source] = ExploredPath.initial(source, heuristic.getDistanceEstimate(source, destination))
        q.add(minimalPaths[source]!!)

        while (q.isNotEmpty()) {
            val (path, vertex) = q.pollFirst()

            if (vertex == destination) return path.buildPath(graph)

            if (expandedVertices.contains(vertex)) continue
            expandedVertices.add(vertex)

            for (neighbour in graph.outgoingNeighboursOf(vertex)) {
                if (expandedVertices.contains(neighbour)) continue

                val pathToNeighbour = path.expand(
                    neighbour,
                    graph.getEdge(vertex, neighbour)!!,
                    heuristic.getDistanceEstimate(neighbour, destination))

                if (pathToNeighbour.canBeBetterThanExistingPath(minimalPaths)) {
                    q.remove(minimalPaths[neighbour])
                    minimalPaths[neighbour] = pathToNeighbour
                    q.add(pathToNeighbour)
                }
            }
        }

        return ShortestPath.empty(source)
    }
}