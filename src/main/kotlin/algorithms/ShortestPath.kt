package algorithms

import graph.Edge
import graph.Graph
import graph.Vertex
import graph.impl.WeightedEdge
import java.util.LinkedList

public abstract class ShortestPath<V : Vertex> private constructor() {
    public lateinit var source: V
        internal set
    public lateinit var destination: V
        internal set

    public var totalWeight: Double = 0.0
        internal set

    public val path: List<ShortestPathFragment<V>> = LinkedList()

    public companion object {
        public fun <V : Vertex> ordered(source: V): OrderedShortestPath<V> {
            return OrderedShortestPath(source)
        }
        public fun <V : Vertex> reversed(destination: V): ReversedShortestPath<V> {
            return ReversedShortestPath(destination)
        }

        public fun <V : Vertex> ofParentMapAndDestination(parent: HashMap<V, V>, destination: V, graph: Graph<V, WeightedEdge>)
                : ReversedShortestPath<V> {
            val path = reversed(destination)
            var lastVertex = destination
            while (parent[lastVertex] != null && graph.getEdge(parent[lastVertex]!!, lastVertex) != null) {
                path.prepend(parent[lastVertex]!!, graph.getEdge(parent[lastVertex]!!, lastVertex)!!)
                lastVertex = parent[lastVertex]!!
            }
            return path
        }
    }

    public fun isEmpty(): Boolean = path.isEmpty()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ShortestPath<*>) return false

        return source == other.source &&
                destination == other.destination &&
                totalWeight == other.totalWeight &&
                path == other.path
    }

    override fun hashCode(): Int = listOf(source, destination, totalWeight, path).toTypedArray().contentHashCode()

    override fun toString(): String {
        return "$source -($totalWeight)-> $destination using $path"
    }

    public class OrderedShortestPath<V : Vertex>(
        source: V,
    ) : ShortestPath<V>() {
        init {
            this.source = source
            this.destination = source
        }

        public fun append(nextVertex: V, edgeToNextVertex: WeightedEdge) {
            path as LinkedList

            path.addLast(ShortestPathFragment(destination, nextVertex, edgeToNextVertex))
            destination = nextVertex
            totalWeight += edgeToNextVertex.weight
        }
    }

    public class ReversedShortestPath<V : Vertex>(
        destination: V,
    ) : ShortestPath<V>() {
        init {
            this.source = destination
            this.destination = destination
        }

        public fun prepend(previousVertex: V, edgeFromPreviousVertex: WeightedEdge) {
            path as LinkedList

            path.addFirst(ShortestPathFragment(previousVertex, source, edgeFromPreviousVertex))
            source = previousVertex
            totalWeight += edgeFromPreviousVertex.weight
        }
    }
}