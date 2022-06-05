package generators

import graph.Edge
import graph.Graph
import graph.Vertex

public interface EdgesGenerator<V : Vertex, E : Edge> {
    public fun addEdgesToGraph(graph: Graph<V, E>)
}