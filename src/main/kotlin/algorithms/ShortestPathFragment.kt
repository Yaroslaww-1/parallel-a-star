package algorithms

import graph.Vertex
import graph.impl.WeightedEdge

public data class ShortestPathFragment<V : Vertex>(
    public val source: V,
    public val destination: V,
    public val edge: WeightedEdge,
) {
}