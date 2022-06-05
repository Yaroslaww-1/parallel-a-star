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
    private val expandedVertices: MutableSet<V>,
    private val minimalPathsForward: MutableMap<V, ExploredPath<V>>,
    private val minimalPathsBackward: MutableMap<V, ExploredPath<V>>,
    private val finished: AtomicBoolean,
) {

    fun search(source: V, destination: V) {
        val q: TreeSet<ExploredPath<V>> = TreeSet(ExploredPathComparator())

        minimalPathsForward[source] = ExploredPath.initial(source, heuristic.getDistanceEstimate(source, destination))
        q.add(minimalPathsForward[source]!!)

        while (!finished.get()) {
            if (q.isEmpty()) {
                finished.set(true)
                break
            }

            val currentPath = q.pollFirst()!!
            val currentVertex = currentPath.lastVertex
            if (expandedVertices.contains(currentVertex)) continue
            expandedVertices.add(currentVertex)

            val currentStateCanBeBetterThanMin = minimalPath.get() == null || currentPath.totalDistance < minimalPath.get().second
            if (!currentStateCanBeBetterThanMin) continue

            for (neighbour in graph.outgoingNeighboursOf(currentVertex)) {
                if (expandedVertices.contains(neighbour)) continue

                val pathToNeighbour = currentPath.expand(
                    neighbour,
                    graph.getEdge(currentVertex, neighbour)!!,
                    heuristic.getDistanceEstimate(neighbour, destination))

                val neighbourNotVisitedYet = !minimalPathsForward.containsKey(neighbour)
                if (neighbourNotVisitedYet) {
                    q.add(pathToNeighbour)

                    minimalPathsForward[neighbour] = pathToNeighbour

                    tryUpdateMinimalPath(pathToNeighbour)
                    continue
                }

                val newNeighbourStateBetterThanExisting = pathToNeighbour.travelledDistance < minimalPathsForward[neighbour]!!.travelledDistance
                if (newNeighbourStateBetterThanExisting) {
                    q.remove(minimalPathsForward[neighbour])
                    q.add(pathToNeighbour)

                    minimalPathsForward[neighbour] = pathToNeighbour

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

            if (!minimalPathsBackward.containsKey(sharedVertex)) return

            val backwardPathPart = minimalPathsBackward[sharedVertex]!!

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