package graph.impl

import graph.Vertex
import utils.nextUUID
import kotlin.random.Random

public data class IdVertex(
    val id: String = Random.nextUUID()
) : Vertex {
}
