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

    override fun compareTo(other: Vertex): Int {
        if (other !is PlanarVertex) throw IllegalArgumentException("compareTo accepts only PlanarVertex")
        return if (x == other.x) y.compareTo(other.y)
        else x.compareTo(other.x)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PlanarVertex) return false

        return x == other.x && y == other.y
    }

    override fun hashCode(): Int {
        val prime = 67867 // Constant can vary, but should be prime
        return prime * x + y
    }

//    override fun hashCode(): Int = listOf(x, y).toTypedArray().contentHashCode()

    override fun toString(): String = "($x-$y)"
}