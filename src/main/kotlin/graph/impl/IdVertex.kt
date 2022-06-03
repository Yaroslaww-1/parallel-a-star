package graph.impl

import graph.Vertex
import utils.nextString
import kotlin.random.Random

public data class IdVertex(
    val id: String = Random.nextString()
) : Vertex {
    override fun compareTo(other: Vertex): Int {
        if (other !is IdVertex) throw IllegalArgumentException("compareTo accepts only IdVertex")
        return id.compareTo(other.id)
    }
}