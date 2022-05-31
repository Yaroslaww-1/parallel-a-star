package algorithms.shortestpath

import algorithms.ShortestPath
import algorithms.ShortestPathFragment
import graph.impl.IdVertex
import graph.impl.WeightedEdge
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class OrderedShortestPathTest {
    private val v1: IdVertex = IdVertex()
    private val v2: IdVertex = IdVertex()
    private val v3: IdVertex = IdVertex()

    private val e1: WeightedEdge = WeightedEdge(1.0)
    private val e2: WeightedEdge = WeightedEdge(2.0)

    private lateinit var path : ShortestPath.OrderedShortestPath<IdVertex>

    @BeforeEach
    internal fun setUp() {
        path = ShortestPath.ordered(v1)
        path.append(v2, e1)
        path.append(v3, e2)
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