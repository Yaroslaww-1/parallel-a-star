package graph.impl

import graph.Edge
import graph.Graph
import graph.Vertex

public open class StandardGraph<V : Vertex, E : Edge>(override val isDirected: Boolean) : Graph<V, E> {
    private val vertexConnections: HashMap<V, HashMap<V, E>> = hashMapOf()

    // Vertices
    override val vertices: Set<V>
        get() = vertexConnections.keys.union(
            vertexConnections.values.flatMap { connections -> connections.keys })

    override fun addVertex(vertex: V) {
        if (!containsVertex(vertex)) {
            vertexConnections[vertex] = hashMapOf()
        }
    }

    override fun addVertices(vertices: List<V>): Unit = vertices.forEach(::addVertex)

    override fun containsVertex(vertex: V): Boolean = vertices.contains(vertex)

    // Edges
    override val edges: Set<E>
        get() = vertexConnections.values.flatMap { connections -> connections.values }.toSet()

    override fun addEdge(vertex1: V, vertex2: V, edge: E) {
        if (!containsVertex(vertex1) || !containsVertex(vertex2)) {
            throw IllegalArgumentException("Vertices provided should already be in graph")
        }

        vertexConnections[vertex1]!![vertex2] = edge
        if (!isDirected) vertexConnections[vertex2]!![vertex1] = edge
    }

    override fun getEdge(vertex1: V, vertex2: V): E? = vertexConnections[vertex1]?.get(vertex2)

    override fun containsEdge(vertex1: V, vertex2: V): Boolean {
        val hasDirectEdge = vertexConnections[vertex1]?.containsKey(vertex2) == true
        val hasReversedEdge = vertexConnections[vertex2]?.containsKey(vertex1) == true

        return if (isDirected) hasDirectEdge
        else hasDirectEdge || hasReversedEdge
    }

    // Other
    override fun outgoingNeighboursOf(vertex: V): Set<V> {
        return vertexConnections[vertex]?.map { (vertex, edge) -> vertex }?.toSet() ?: emptySet()
    }

    override fun degreeOf(vertex: V): Int = outgoingNeighboursOf(vertex).size
}