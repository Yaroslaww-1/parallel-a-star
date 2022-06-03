package algorithms

import graph.Graph
import graph.Vertex
import graph.impl.WeightedEdge
import java.util.LinkedList

public class ShortestPath<V : Vertex> private constructor(
    initialVertex: V
) {
    public var source: V = initialVertex
        internal set
    public var destination: V = initialVertex
        internal set

    public var totalWeight: Int = 0
        internal set

    internal val path: List<ShortestPathFragment<V>> = LinkedList()

    public val distanceInVertices: Int
        get() = path.size + 1

    public companion object {
        public fun <V : Vertex> empty(initialVertex: V): ShortestPath<V> {
            return ShortestPath(initialVertex)
        }



        public fun <V : Vertex, E : WeightedEdge> ofParentMapAndDestination(
            parent: HashMap<V, V>,
            destination: V,
            graph: Graph<V, E>
        ) : ShortestPath<V> {
            val path = empty(destination)
            var lastVertex = destination
            while (parent[lastVertex] != null) {
                path.prepend(parent[lastVertex]!!, graph.getEdge(parent[lastVertex]!!, lastVertex)!!)
                lastVertex = parent[lastVertex]!!
            }
            return path
        }
    }

    public fun isEmpty(): Boolean = path.isEmpty()
    public fun isNotEmpty(): Boolean = path.isNotEmpty()
    public fun containsEdge(source: V, destination: V, edge: WeightedEdge): Boolean = path.contains(
        ShortestPathFragment(source, destination, edge))

    public fun append(nextVertex: V, edgeToNextVertex: WeightedEdge) {
        path as LinkedList

        path.addLast(ShortestPathFragment(destination, nextVertex, edgeToNextVertex))
        destination = nextVertex
        totalWeight += edgeToNextVertex.distance
    }

    public fun appendAll(other: ShortestPath<V>) {
        if (other.source != destination) throw IllegalArgumentException("other.source should be equal to current.destination")
        other.path.forEach { frag -> append(frag.destination, frag.edge) }
    }

    public fun prepend(previousVertex: V, edgeFromPreviousVertex: WeightedEdge) {
        path as LinkedList

        path.addFirst(ShortestPathFragment(previousVertex, source, edgeFromPreviousVertex))
        source = previousVertex
        totalWeight += edgeFromPreviousVertex.distance
    }

    public fun reversed(): ShortestPath<V> {
        val reversed = empty(destination)
        for (frag in path.reversed()) {
            reversed.append(frag.source, frag.edge)
        }
        return reversed
    }

//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other !is ShortestPath<*>) return false
//
//        return source == other.source &&
//                destination == other.destination &&
//                totalWeight == other.totalWeight &&
//                path == other.path
//    }
//
//    override fun hashCode(): Int = listOf(source, destination, totalWeight, path).toTypedArray().contentHashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ShortestPath<*>) return false

        return source == other.source &&
                destination == other.destination &&
                totalWeight == other.totalWeight
    }

    override fun hashCode(): Int = listOf(source, destination, totalWeight).toTypedArray().contentHashCode()

    override fun toString(): String {
        return "$source -($totalWeight)-> $destination using $path"
//        return "$source -($totalWeight)-> $destination using $distanceInVertices vertices"
    }

//    public class OrderedShortestPath<V : Vertex>(
//        source: V,
//    ) : ShortestPath<V>() {
//        init {
//            this.source = source
//            this.destination = source
//        }
//
//
//    }
//
//    public class ReversedShortestPath<V : Vertex>(
//        destination: V,
//    ) : ShortestPath<V>() {
//        init {
//            this.source = destination
//            this.destination = destination
//        }
//
//
//    }
}