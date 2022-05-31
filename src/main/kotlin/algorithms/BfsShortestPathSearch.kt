package algorithms

import graph.Graph
import graph.Vertex
import graph.impl.WeightedEdge
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

public class BfsShortestPathSearch<V : Vertex>(
    private val graph : Graph<V, WeightedEdge>
) : ShortestPathSearch<V> {

    override fun search(source: V, destination: V): ShortestPath<V> {
        val visited: HashSet<V> = hashSetOf()
        val parent: HashMap<V, V> = hashMapOf()

        val q: Queue<V> = LinkedList()
        q.add(source)
        visited.add(source)

        while (q.isNotEmpty()) {
            val current = q.remove()
            if (current == destination) break
            for (neighbour in graph.outgoingNeighboursOf(current)) {
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour)
                    q.add(neighbour)
                    parent[neighbour] = current
                }
            }
        }

        return ShortestPath.ofParentMapAndDestination(parent, destination, graph)
    }
}