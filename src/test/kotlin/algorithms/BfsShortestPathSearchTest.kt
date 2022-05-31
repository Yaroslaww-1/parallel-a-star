package algorithms

import graph.impl.IdVertex
import graph.impl.StandardGraph
import graph.impl.WeightedEdge
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class BfsShortestPathSearchTest {
    private val v1: IdVertex = IdVertex()
    private val v2: IdVertex = IdVertex()
    private val v3: IdVertex = IdVertex()
    private val v4: IdVertex = IdVertex()

    private val e12: WeightedEdge = WeightedEdge(1.0)
    private val e13: WeightedEdge = WeightedEdge(1.0)
    private val e14: WeightedEdge = WeightedEdge(1.0)
    private val e24: WeightedEdge = WeightedEdge(1.0)
    private val e34: WeightedEdge = WeightedEdge(1.0)

    private lateinit var graphDirected : StandardGraph<IdVertex, WeightedEdge>

    private lateinit var graphUnDirected : StandardGraph<IdVertex, WeightedEdge>

    @BeforeEach
    internal fun setUp() {
        graphDirected = StandardGraph(true)
        graphDirected.addVertices(listOf(v1, v2, v3, v4))
        graphDirected.addEdge(v1, v2, e12)
        graphDirected.addEdge(v1, v3, e13)
        graphDirected.addEdge(v1, v4, e14)
        graphDirected.addEdge(v2, v4, e24)
        graphDirected.addEdge(v3, v4, e34)

        graphUnDirected = StandardGraph(false)
        graphUnDirected.addVertices(listOf(v1, v2, v3, v4))
        graphUnDirected.addEdge(v1, v2, e12)
        graphUnDirected.addEdge(v1, v3, e13)
        graphUnDirected.addEdge(v1, v4, e14)
        graphUnDirected.addEdge(v2, v4, e24)
        graphUnDirected.addEdge(v3, v4, e34)
    }

    @Test
    fun searchDirected() {
        val searcher = BfsShortestPathSearch(graphDirected)
        assertEquals(DijkstraShortestPathSearch(graphDirected).search(v1, v4), searcher.search(v1, v4))
        assertEquals(DijkstraShortestPathSearch(graphDirected).search(v2, v3), searcher.search(v2, v3))
    }

    @Test
    fun searchUnDirected() {
        val searcher = BfsShortestPathSearch(graphUnDirected)
        assertEquals(DijkstraShortestPathSearch(graphUnDirected).search(v1, v4), searcher.search(v1, v4))
        assertEquals(DijkstraShortestPathSearch(graphUnDirected).search(v2, v3), searcher.search(v2, v3))
    }

    @Test
    fun searchEmpty() {
        val graphEmpty : StandardGraph<IdVertex, WeightedEdge> = StandardGraph(false)
        val searcher = BfsShortestPathSearch(graphEmpty)
        assertTrue(searcher.search(v1, v4).isEmpty())
    }
}