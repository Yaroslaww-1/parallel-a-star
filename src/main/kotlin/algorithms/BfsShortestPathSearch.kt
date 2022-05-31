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
        val parent: HashMap<V, Pair<V, WeightedEdge>> = hashMapOf()

        val q: Queue<V> = LinkedList()
        q.add(source)
        visited.add(source)

        while (q.isNotEmpty()) {
            val v = q.remove()
            for (outgoingEdge in graph.outgoingEdgesOf(v)) {
                val to = outgoingEdge.first
                val edge = outgoingEdge.second
                if (!visited.contains(to)) {
                    visited.add(to)
                    q.add(to)
                    parent[to] = Pair(v, edge)
                }
                if (to == destination) break
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
}