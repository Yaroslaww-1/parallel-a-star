package algorithms.astar.parallel.bidirectional

import algorithms.astar.heuristic.AStarAdmissibleHeuristic
import algorithms.astar.utils.ExploredPath
import algorithms.astar.utils.ExploredPathComparator
import graph.Graph
import graph.Vertex
import graph.impl.WeightedEdge
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

internal class ParallelBidirectionalAStarShortestPathSearchInOneDirection<V : Vertex, E : WeightedEdge>(
    private val graph : Graph<V, E>,
    private val heuristic: AStarAdmissibleHeuristic<V>,

    private val minimalPath: AtomicReference<Pair<V, Int>>,
    private val visitedVertices: MutableSet<V>,
    private val exploredPathForward: MutableMap<V, ExploredPath<V>>,
    private val exploredPathBackward: MutableMap<V, ExploredPath<V>>,
    private val finished: AtomicBoolean,
) {

    fun search(source: V, destination: V) {
        val q: TreeSet<ExploredPath<V>> = TreeSet(ExploredPathComparator())

        exploredPathForward[source] = ExploredPath.initial(source, heuristic.getDistanceEstimate(source, destination))
        q.add(exploredPathForward[source]!!)

        while (!finished.get()) {
            if (q.isEmpty()) {
                finished.set(true)
                break
            }

            val currentPath = q.pollFirst()!!
            val currentVertex = currentPath.lastVertex
            if (visitedVertices.contains(currentVertex)) continue
            visitedVertices.add(currentVertex)

            val currentStateCanBeBetterThanMin = minimalPath.get() == null || currentPath.totalDistance < minimalPath.get().second
            if (!currentStateCanBeBetterThanMin) continue

            for (neighbour in graph.outgoingNeighboursOf(currentVertex)) {
                if (visitedVertices.contains(neighbour)) continue

                val pathToNeighbour = currentPath.expand(
                    neighbour,
                    graph.getEdge(currentVertex, neighbour)!!,
                    heuristic.getDistanceEstimate(neighbour, destination))

                val neighbourNotVisitedYet = !exploredPathForward.containsKey(neighbour)
                if (neighbourNotVisitedYet) {
                    q.add(pathToNeighbour)

                    exploredPathForward[neighbour] = pathToNeighbour

                    tryUpdateMinimalPath(pathToNeighbour)
                    continue
                }

                val newNeighbourStateBetterThanExisting = pathToNeighbour.travelledDistance < exploredPathForward[neighbour]!!.travelledDistance
                if (newNeighbourStateBetterThanExisting) {
                    q.remove(exploredPathForward[neighbour])
                    q.add(pathToNeighbour)

                    exploredPathForward[neighbour] = pathToNeighbour

                    tryUpdateMinimalPath(pathToNeighbour)
                }
            }
        }
    }

    private fun tryUpdateMinimalPath(forwardPathPart: ExploredPath<V>) {
        var successfullyUpdated = false

        while (!successfullyUpdated) {
            val oldMinimalPath = minimalPath.get()

            val sharedVertex = forwardPathPart.lastVertex

            if (!exploredPathBackward.containsKey(sharedVertex)) return

            val backwardPathPart = exploredPathBackward[sharedVertex]!!

            val newDistance = forwardPathPart.travelledDistance + backwardPathPart.travelledDistance
            val oldDistance = oldMinimalPath?.second ?: Int.MAX_VALUE

            if (newDistance < oldDistance) {
                successfullyUpdated = minimalPath.compareAndSet(oldMinimalPath, Pair(sharedVertex, newDistance))
            } else {
                break
            }
        }
    }
}