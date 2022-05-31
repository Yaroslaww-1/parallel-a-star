package planar

import algorithms.DijkstraShortestPathSearch
import algorithms.astar.AStarEuclideanDistanceHeuristic
import algorithms.astar.AStarShortestPathSearch
import graph.impl.EmptyEdge
import graph.impl.IdVertex
import graph.impl.StandardGraph
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import planar.generator.PlanarGraphGenerator

internal class PlanarGraphGeneratorTest {
    @Test
    @RepeatedTest(10)
    fun dijkstraAndAStarEqual() {
        val (graph, source, destination) = PlanarGraphGenerator().generateGraphWithSourceAndDestination(
            10000, 1000, 4, 50)
        val dijkstraPath = DijkstraShortestPathSearch(graph).search(source, destination)
        val aStarPath = AStarShortestPathSearch(graph, AStarEuclideanDistanceHeuristic()).search(source, destination)
        assertEquals(dijkstraPath, aStarPath)
    }
}