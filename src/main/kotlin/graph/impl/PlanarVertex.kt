package graph.impl

import graph.Vertex
import utils.euclideanDistance

public open class PlanarVertex(
    public val x: Int,
    public val y: Int
) : Vertex {
    public fun distance(to: PlanarVertex): Double {
        return euclideanDistance(x, y, to.x, to.y)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PlanarVertex) return false

        return x == other.x && y == other.y
    }

    override fun hashCode(): Int = listOf(x, y).toTypedArray().contentHashCode()
}