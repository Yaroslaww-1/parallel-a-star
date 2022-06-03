package algorithms.astar.parallel.bidirectional

import algorithms.ShortestPath
import algorithms.ShortestPathSearch
import algorithms.astar.heuristic.AStarAdmissibleHeuristic
import algorithms.astar.utils.ExploredPath
import graph.Graph
import graph.Vertex
import graph.impl.WeightedEdge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

public class ParallelBidirectionalAStarShortestPathSearch<V : Vertex, E : WeightedEdge>(
    private val graph : Graph<V, E>,
    private val heuristic: AStarAdmissibleHeuristic<V>
) : ShortestPathSearch<V> {

    private val minimalPath: AtomicReference<Pair<V, Int>> = AtomicReference(null)
    private val visited: MutableSet<V> = ConcurrentHashMap.newKeySet()
    private val exploredPathForward: ConcurrentHashMap<V, ExploredPath<V>> = ConcurrentHashMap<V, ExploredPath<V>>()
    private val exploredPathBackward: ConcurrentHashMap<V, ExploredPath<V>> = ConcurrentHashMap<V, ExploredPath<V>>()
    private val finished = AtomicBoolean(false)
    private lateinit var source: V
    private lateinit var destination: V

    override fun search(source: V, destination: V): ShortestPath<V> {
        this.source = source
        this.destination = destination

        runBlocking {
            withContext(Dispatchers.Default) {
                val one = async { runSearchForward() }
                val two = async { runSearchBackward() }
                one.await()
                two.await()
            }
        }

        val sharedVertex = minimalPath.get()?.first ?: return ShortestPath.empty(source)
        val forwardPathPart: ShortestPath<V> = exploredPathForward[sharedVertex]!!.buildPath(graph)
        val backwardPathPart: ShortestPath<V> = exploredPathBackward[sharedVertex]!!.buildPath(graph)

        forwardPathPart.appendAll(backwardPathPart.reversed())
        return forwardPathPart
    }

    private fun runSearchForward() {
        ParallelBidirectionalAStarShortestPathSearchInOneDirection(
            graph, heuristic, minimalPath, visited, exploredPathForward, exploredPathBackward, finished
        ).search(source, destination)
    }

    private fun runSearchBackward() {
        ParallelBidirectionalAStarShortestPathSearchInOneDirection(
            graph, heuristic, minimalPath, visited, exploredPathBackward, exploredPathForward, finished
        ).search(destination, source)
    }
}