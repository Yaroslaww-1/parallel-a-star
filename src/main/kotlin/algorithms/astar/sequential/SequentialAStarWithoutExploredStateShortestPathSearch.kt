package algorithms.astar.sequential

import algorithms.ShortestPath
import algorithms.ShortestPathSearch
import algorithms.astar.heuristic.AStarAdmissibleHeuristic
import graph.Graph
import graph.Vertex
import graph.impl.WeightedEdge
import java.util.*
import kotlin.collections.HashMap

public class SequentialAStarWithoutExploredStateShortestPathSearch<V : Vertex, E : WeightedEdge>(
    private val graph : Graph<V, E>,
    private val heuristic: AStarAdmissibleHeuristic<V>
) : ShortestPathSearch<V> {

    override fun search(source: V, destination: V): ShortestPath<V> {
        val distance: MutableMap<V, Int> = hashMapOf<V, Int>().withDefault { Int.MAX_VALUE }
        val distanceWithHeuristic: MutableMap<V, Int> = hashMapOf<V, Int>().withDefault { Int.MAX_VALUE }
        val parent: HashMap<V, V> = hashMapOf()

        distance[source] = 0
        distanceWithHeuristic[source] = heuristic.getDistanceEstimate(source, destination).toInt()

        val q: TreeSet<V> = TreeSet(VertexDistanceComparator(distance))
        q.add(source)

        while (q.isNotEmpty()) {
            val current = q.first()
            q.remove(current)

            if (current == destination) break

            for (neighbour in graph.outgoingNeighboursOf(current)) {
                val newDistanceToNeighbour = distance.getValue(current) + graph.getEdge(current, neighbour)!!.distance
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

        return ShortestPath.ofParentMapAndDestination(parent, destination, graph)
    }

    private class VertexDistanceComparator<V : Vertex>(private val distance: MutableMap<V, Int>) : Comparator<V> {
        override fun compare(v1: V, v2: V): Int {
            if (distance.getValue(v1) != distance.getValue(v2)) return distance.getValue(v1).compareTo(distance.getValue(v2))
            return v1.compareTo(v2)
        }
    }
}