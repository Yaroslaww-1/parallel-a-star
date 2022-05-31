package graph.impl

import graph.Vertex
import utils.nextString
import kotlin.random.Random

public data class IdVertex(
    val id: String = Random.nextString()
) : Vertex