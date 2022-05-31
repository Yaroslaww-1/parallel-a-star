package algorithms

import graph.Graph
import graph.Vertex
import graph.impl.WeightedEdge
import java.util.*

public class DijkstraShortestPathSearch<V : Vertex>(
    private val graph : Graph<V, WeightedEdge>
) : ShortestPathSearch<V> {
    override fun search(source: V, destination: V): ShortestPath<V> {
        val distance: MutableMap<V, Double> = hashMapOf<V, Double>().withDefault { Double.MAX_VALUE }
        val parent: HashMap<V, Pair<V, WeightedEdge>> = hashMapOf()

        distance[source] = 0.0

        val q: TreeSet<V> = TreeSet(VertexDistanceComparator(distance))
        q.add(source)

        while (q.isNotEmpty()) {
            val v = q.first()
            q.remove(v)

            for (outgoingEdge in graph.outgoingEdgesOf(v)) {
                val to = outgoingEdge.first
                val edge = outgoingEdge.second
                if (distance.getValue(v) + edge.weight < distance.getValue(to)) {
                    q.remove(to)
                    distance[to] = distance.getValue(v) + edge.weight
                    parent[to] = Pair(v, edge)
                    q.add(to)
                }
            }
        }

        val path = ShortestPath.reversed(destination)
        var lastVertex = destination
        while (parent[lastVertex] != null) {
            path.prepend(parent[lastVertex]!!.first, parent[lastVertex]!!.second)
            lastVertex = parent[lastVertex]!!.first
        }
        return path
    }

    private class VertexDistanceComparator<V>(private val distance: MutableMap<V, Double>) : Comparator<V> {
        override fun compare(v1: V, v2: V): Int {
            return distance.getValue(v1).compareTo(distance.getValue(v2))
        }
    }
}