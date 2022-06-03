package graph.impl

import graph.Edge

public open class WeightedEdge(public val distance: Int) : Edge {
    override fun toString(): String = "$distance"
}
