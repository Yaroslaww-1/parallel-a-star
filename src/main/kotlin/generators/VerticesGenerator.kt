package generators

import graph.Vertex

public interface VerticesGenerator<V : Vertex> {
    public fun generateVertices(): List<V>
}