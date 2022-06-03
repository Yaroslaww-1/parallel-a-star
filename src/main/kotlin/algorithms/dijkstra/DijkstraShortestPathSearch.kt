package algorithms.dijkstra

import algorithms.ShortestPath
import algorithms.ShortestPathSearch
import graph.Graph
import graph.Vertex
import graph.impl.WeightedEdge
import java.util.*

public class DijkstraShortestPathSearch<V : Vertex, E : WeightedEdge>(
    private val graph : Graph<V, E>
) : ShortestPathSearch<V> {

    override fun search(source: V, destination: V): ShortestPath<V> {
        val distance: MutableMap<V, Double> = hashMapOf<V, Double>().withDefault { 10000000.0 }
        val parent: HashMap<V, V> = hashMapOf()

        distance[source] = 0.0

        val q: TreeSet<V> = TreeSet(VertexDistanceComparator(distance))
        q.add(source)

        while (q.isNotEmpty()) {
            val current = q.first()
            q.remove(current)

            for (neighbour in graph.outgoingNeighboursOf(current)) {
                val newDistanceToNeighbour = distance.getValue(current) + graph.getEdge(current, neighbour)!!.distance
                if (newDistanceToNeighbour < distance.getValue(neighbour)) {
                    q.remove(neighbour)
                    distance[neighbour] = newDistanceToNeighbour
                    parent[neighbour] = current
                    q.add(neighbour)
                }
            }
        }

        return ShortestPath.ofParentMapAndDestination(parent, destination, graph)
    }

    private class VertexDistanceComparator<V : Comparable<V>>(private val distance: MutableMap<V, Double>) : Comparator<V> {
        override fun compare(v1: V, v2: V): Int {
            val distance1 = distance.getValue(v1)
            val distance2 = distance.getValue(v2)
            return if (distance1 == distance2) v1.compareTo(v2)
            else distance1.compareTo(distance2)
        }
    }
}