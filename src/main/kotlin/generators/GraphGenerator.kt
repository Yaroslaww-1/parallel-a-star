package generators

import graph.Edge
import graph.Graph
import graph.Vertex

public interface GraphGenerator<V : Vertex, E : Edge> {
    public fun generateGraph(): Graph<V, E>
    public fun generateSourceDestination(graph: Graph<V, E>): Pair<V, V>
    public fun generateGraphWithSourceAndDestination(): Triple<Graph<V, E>, V, V>
}