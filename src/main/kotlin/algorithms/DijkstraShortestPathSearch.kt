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
        val parent: HashMap<V, V> = hashMapOf()

        distance[source] = 0.0

        val q: TreeSet<V> = TreeSet(VertexDistanceComparator(distance))
        q.add(source)

        while (q.isNotEmpty()) {
            val current = q.first()
            q.remove(current)

            for (neighbour in graph.outgoingNeighboursOf(current)) {
                val newDistanceToNeighbour = distance.getValue(current) + graph.getEdge(current, neighbour)!!.weight
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

    private class VertexDistanceComparator<V>(private val distance: MutableMap<V, Double>) : Comparator<V> {
        override fun compare(v1: V, v2: V): Int {
            return distance.getValue(v1).compareTo(distance.getValue(v2))
        }
    }
}