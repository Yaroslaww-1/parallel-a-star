package algorithms

import graph.Vertex

public interface ShortestPathSearch<V : Vertex> {
    public fun search(source: V, destination: V): ShortestPath<V>
}