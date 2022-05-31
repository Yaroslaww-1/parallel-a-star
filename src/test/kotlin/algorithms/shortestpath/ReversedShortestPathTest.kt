package algorithms.shortestpath

import algorithms.ShortestPath
import algorithms.ShortestPathFragment
import graph.impl.IdVertex
import graph.impl.WeightedEdge
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ReversedShortestPathTest {
    private val v1: IdVertex = IdVertex()
    private val v2: IdVertex = IdVertex()
    private val v3: IdVertex = IdVertex()

    private val e1: WeightedEdge = WeightedEdge(1.0)
    private val e2: WeightedEdge = WeightedEdge(2.0)

    private lateinit var path : ShortestPath.ReversedShortestPath<IdVertex>

    @BeforeEach
    internal fun setUp() {
        path = ShortestPath.reversed(v3)
        path.prepend(v2, e2)
        path.prepend(v1, e1)
    }

    @Test
    fun getSource() {
        assertEquals(v1, path.source)
    }

    @Test
    fun getDestination() {
        assertEquals(v3, path.destination)
    }

    @Test
    fun getTotalWeight() {
        assertEquals(e1.weight + e2.weight, path.totalWeight)
    }

    @Test
    fun getPath() {
        assertEquals(listOf(
            ShortestPathFragment(v1, v2, e1),
            ShortestPathFragment(v2, v3, e2)
        ), path.path)
    }
}