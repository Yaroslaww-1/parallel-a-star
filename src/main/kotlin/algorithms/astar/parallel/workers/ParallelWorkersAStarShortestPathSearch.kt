package algorithms.astar.parallel.workers

import algorithms.ShortestPath
import algorithms.ShortestPathSearch
import algorithms.astar.heuristic.AStarAdmissibleHeuristic
import algorithms.astar.utils.ExpandedVertices
import algorithms.astar.utils.ExploredPath
import algorithms.astar.utils.ExploredPathComparator
import algorithms.astar.utils.MinimalPaths
import graph.Graph
import graph.Vertex
import graph.impl.WeightedEdge
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

public class ParallelWorkersAStarShortestPathSearch<V : Vertex, E : WeightedEdge>(
    private val graph : Graph<V, E>,
    private val heuristic: AStarAdmissibleHeuristic<V>,
    private val workersCount : Int
) : ShortestPathSearch<V> {

    private val minimalPaths: MinimalPaths<V> = MinimalPaths()
    private val minimalPathToDestination: AtomicReference<ExploredPath<V>> = AtomicReference(null)
    private val expandedVertices: ExpandedVertices<V> = ConcurrentHashMap.newKeySet()
    private val workers: List<Worker<V>> = Collections.synchronizedList(mutableListOf())
    private lateinit var source: V
    private lateinit var destination: V

    override fun search(source: V, destination: V): ShortestPath<V> {
        this.source = source
        this.destination = destination

        for (i in (0..workersCount)) {
            workers as MutableList
            workers.add(Worker())
        }

        minimalPaths[source] = ExploredPath.initial(source, heuristic.getDistanceEstimate(source, destination))
        workers[getWorkerForVertex(source)].add(minimalPaths[source]!!)

        while (true) {
            workers.parallelStream().forEach { worker ->
                if (!worker.hasVerticesToProcess) return@forEach

                val currentPath = worker.pollShortestPath()
                val currentVertex = currentPath.lastVertex

                if (currentVertex == destination) {
                    val oldMinimalPath = minimalPathToDestination.get()
                    if (oldMinimalPath == null || currentPath.totalDistance < oldMinimalPath.totalDistance) {
                        minimalPathToDestination.compareAndSet(oldMinimalPath, currentPath)
                    }
                    return@forEach
                }

//                synchronized(expandedVertices) {
//                    if (expandedVertices.contains(currentVertex)) return@forEach
//                    expandedVertices.add(currentVertex)
//                }

//                val currentStateCanBeBetterThanMin = minimalPathToDestination.get() == null || currentPath.totalDistance < minimalPathToDestination.get().totalDistance
//                if (!currentStateCanBeBetterThanMin) return@forEach

                for (neighbour in graph.outgoingNeighboursOf(currentVertex)) {
//                    synchronized(expandedVertices) {
//                        if (expandedVertices.contains(neighbour)) continue
//                    }

                    val pathToNeighbour = currentPath.expand(
                        neighbour,
                        graph.getEdge(currentVertex, neighbour)!!,
                        heuristic.getDistanceEstimate(neighbour, destination))

                    if (pathToNeighbour.canBeBetterThanExistingPath(minimalPaths)) {
                        workers[getWorkerForVertex(neighbour)].remove(minimalPaths[neighbour])
                        minimalPaths[neighbour] = pathToNeighbour
                        workers[getWorkerForVertex(neighbour)].add(minimalPaths[neighbour]!!)
                    }
                }
            }

            if (minimalPathToDestination.get() != null) {
//                return minimalPathToDestination.get().buildPath(graph)
                val minTotalDistanceFromWorkers = workers.minOf { worker -> worker.minimalTotalDistance }
                val allWorkersHaveBiggerMinimalPathsThanCurrentOne = minTotalDistanceFromWorkers > minimalPathToDestination.get().totalDistance
                if (allWorkersHaveBiggerMinimalPathsThanCurrentOne) {
                    return minimalPathToDestination.get().buildPath(graph)
                }
            }
        }
    }

    private fun getWorkerForVertex(vertex: V) : Int = (vertex.hashCode() % workersCount + workersCount) % workersCount
}