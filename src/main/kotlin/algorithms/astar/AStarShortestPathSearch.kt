package algorithms.astar

import algorithms.ShortestPath
import algorithms.ShortestPathSearch
import graph.Graph
import graph.Vertex
import graph.impl.WeightedEdge
import java.util.*
import kotlin.collections.HashMap

public class AStarShortestPathSearch<V : Vertex>(
    private val graph : Graph<V, WeightedEdge>,
    private val heuristic: AStarAdmissibleHeuristic<V>
) : ShortestPathSearch<V> {

    override fun search(source: V, destination: V): ShortestPath<V> {
        val distance: MutableMap<V, Double> = hashMapOf<V, Double>().withDefault { Double.MAX_VALUE }
        val distanceWithHeuristic: MutableMap<V, Double> = hashMapOf<V, Double>().withDefault { Double.MAX_VALUE }
        val parent: HashMap<V, V> = hashMapOf()

        distance[source] = 0.0
        distanceWithHeuristic[source] = heuristic.getDistanceEstimate(source, destination)

        val q: TreeSet<V> = TreeSet(VertexDistanceComparator(distance))
        q.add(source)

        while (q.isNotEmpty()) {
            val current = q.first()
            q.remove(current)

            if (current == destination) break

            for (outgoingEdge in graph.outgoingEdgesOf(current)) {
                val neighbour = outgoingEdge.first
                val edge = outgoingEdge.second
                val newDistanceToNeighbour = distance.getValue(current) + edge.weight
                if (newDistanceToNeighbour < distance.getValue(neighbour)) {
                    q.remove(neighbour)
                    distance[neighbour] = newDistanceToNeighbour
                    distanceWithHeuristic[neighbour] = newDistanceToNeighbour +
                            heuristic.getDistanceEstimate(neighbour, destination)
                    parent[neighbour] = current
                    q.add(neighbour)
                }
            }
        }

        val path = ShortestPath.reversed(destination)
        var lastVertex = destination
        while (parent[lastVertex] != null && graph.getEdge(parent[lastVertex]!!, lastVertex) != null) {
            path.prepend(parent[lastVertex]!!, graph.getEdge(parent[lastVertex]!!, lastVertex)!!)
            lastVertex = parent[lastVertex]!!
        }
        return path
    }

    private class VertexDistanceComparator<V>(private val distance: MutableMap<V, Double>) : Comparator<V> {
        override fun compare(v1: V, v2: V): Int {
            return distance.getValue(v1).compareTo(distance.getValue(v2))
        }
    }
}