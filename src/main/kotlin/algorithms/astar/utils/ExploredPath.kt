package algorithms.astar.utils

import algorithms.ShortestPath
import graph.Graph
import graph.Vertex
import graph.impl.WeightedEdge
import java.util.Comparator

internal class ExploredPath<V : Vertex> private constructor(
    val lastVertex: V,
    val travelledDistance: Int,
    val estimatedDistance: Int,
    private val prev: ExploredPath<V>?
) {
    val totalDistance = travelledDistance + estimatedDistance

    fun expand(neighbour: V, edge: WeightedEdge, newEstimatedDistanceToDestination: Int): ExploredPath<V> {
        return ExploredPath(
            neighbour, travelledDistance + edge.distance, newEstimatedDistanceToDestination, this)
    }

    fun canBeBetterThanExistingPath(minimalPaths: MinimalPaths<V>): Boolean {
        return minimalPaths[this.lastVertex] == null || this.travelledDistance < minimalPaths[this.lastVertex]!!.travelledDistance
    }

    fun <E : WeightedEdge> buildPath(graph: Graph<V, E>): ShortestPath<V> {
        var cur = this
        val path = ShortestPath.empty(cur.lastVertex)
        while (cur.prev != null) {
            path.prepend(cur.prev!!.lastVertex, graph.getEdge(cur.prev!!.lastVertex, cur.lastVertex)!!)
            cur = cur.prev!!
        }
        return path
    }

    companion object {
        fun <V : Vertex> initial(source: V, expectedDistanceToDestination: Int): ExploredPath<V> =
            ExploredPath(source, 0, expectedDistanceToDestination, null)
    }
}

internal class ExploredPathComparator<V : Vertex> : Comparator<ExploredPath<V>> {
    override fun compare(s1: ExploredPath<V>, s2: ExploredPath<V>): Int {
        if (s1.totalDistance != s2.totalDistance) return (s1.totalDistance).compareTo(s2.totalDistance)
        if (s1.travelledDistance != s2.travelledDistance) return (s1.travelledDistance).compareTo(s2.travelledDistance)
        if (s1.estimatedDistance != s2.estimatedDistance) return (s1.estimatedDistance).compareTo(s2.estimatedDistance)
        return s1.lastVertex.compareTo(s2.lastVertex)
    }
}