package graph

public interface Graph<V : Vertex, E : Edge> {
    public val isDirected: Boolean

    public val vertices: Set<V>
    public val edges: Set<E>

    public fun addVertex(vertex: V)
    public fun addVertices(vertices: List<V>)

    public fun addEdge(vertex1: V, vertex2: V, edge: E)
//    public fun addEdge(endpoints: EndpointPair<V>, edge: E)
    public fun getEdge(vertex1: V, vertex2: V): E?

    public fun containsVertex(vertex: V): Boolean
    public fun containsEdge(vertex1: V, vertex2: V): Boolean

    public fun outgoingNeighboursOf(vertex: V): Set<V>
}