package algorithms.astar.sequential

import algorithms.astar.heuristic.AStarEuclideanDistanceHeuristic
import algorithms.dijkstra.DijkstraShortestPathSearch
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import planar.PlanarGraph
import planar.generator.PlanarGraphGenerator

internal class SequentialAStarWithoutExploredStateShortestPathSearchTest {
    private lateinit var generator: PlanarGraphGenerator
    private lateinit var graph: PlanarGraph

    @BeforeEach
    internal fun setUp() {
        generator = PlanarGraphGenerator()
        graph = generator.generateGraphCorrect(100, 10)
    }

    @Test
    @RepeatedTest(50)
    fun search() {
        val (source, destination) = generator.generateSourceDestination(graph, 10)
        val expectedPath = DijkstraShortestPathSearch(graph).search(source, destination)
        val actualPath = SequentialAStarWithoutExploredStateShortestPathSearch(graph, AStarEuclideanDistanceHeuristic()).search(source, destination)
        Assertions.assertEquals(expectedPath, actualPath)
    }
}