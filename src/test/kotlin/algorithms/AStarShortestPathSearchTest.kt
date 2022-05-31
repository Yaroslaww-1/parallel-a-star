package algorithms

import algorithms.astar.AStarAdmissibleHeuristic
import algorithms.astar.AStarEuclideanDistanceHeuristic
import algorithms.astar.AStarShortestPathSearch
import graph.impl.PlanarVertex
import graph.impl.StandardGraph
import graph.impl.WeightedEdge
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class AStarShortestPathSearchTest {
    private val v1: PlanarVertex = PlanarVertex(0, 0)
    private val v2: PlanarVertex = PlanarVertex(2, 2)
    private val v3: PlanarVertex = PlanarVertex(2, -2)
    private val v4: PlanarVertex = PlanarVertex(4, 0)

    private val e12: WeightedEdge = WeightedEdge(1.0)
    private val e13: WeightedEdge = WeightedEdge(0.5)
    private val e14: WeightedEdge = WeightedEdge(4.0)
    private val e24: WeightedEdge = WeightedEdge(2.0)
    private val e34: WeightedEdge = WeightedEdge(3.0)

    private lateinit var graph : StandardGraph<PlanarVertex, WeightedEdge>
    private val heuristic: AStarAdmissibleHeuristic<PlanarVertex> = AStarEuclideanDistanceHeuristic()

    @BeforeEach
    internal fun setUp() {
        graph = StandardGraph(false)
        graph.addVertices(listOf(v1, v2, v3, v4))
        graph.addEdge(v1, v2, e12)
        graph.addEdge(v1, v3, e13)
        graph.addEdge(v1, v4, e14)
        graph.addEdge(v2, v4, e24)
        graph.addEdge(v3, v4, e34)
    }

    @Test
    fun search() {
        val searcher = AStarShortestPathSearch(graph, heuristic)
        assertEquals(DijkstraShortestPathSearch(graph).search(v1, v4), searcher.search(v1, v4))
        assertEquals(DijkstraShortestPathSearch(graph).search(v2, v3), searcher.search(v2, v3))
    }

    @Test
    fun searchEmpty() {
        val graphEmpty : StandardGraph<PlanarVertex, WeightedEdge> = StandardGraph(false)
        val searcher = AStarShortestPathSearch(graphEmpty, heuristic)
        assertTrue(searcher.search(v1, v4).isEmpty())
    }
}