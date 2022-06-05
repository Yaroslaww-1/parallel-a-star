package algorithms.astar.parallel.workers

import algorithms.astar.heuristic.AStarEuclideanDistanceHeuristic
import algorithms.dijkstra.DijkstraShortestPathSearch
import graph.Graph
import graph.impl.PlanarVertex
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import planar.PlanarEdge
import planar.PlanarGraph
import planar.generator.PlanarGraphGenerator
import planar.generator.PlanarKNearestEdgesGenerator
import planar.generator.PlanarVerticesGenerator

internal class ParallelWorkersAStarShortestPathSearchTest {
    private lateinit var generator: PlanarGraphGenerator
    private lateinit var graph: Graph<PlanarVertex, PlanarEdge>

    @BeforeEach
    internal fun setUp() {
        val verticesGenerator = PlanarVerticesGenerator(100, 10000)
        val edgesGenerator = PlanarKNearestEdgesGenerator(10)
        generator = PlanarGraphGenerator(verticesGenerator, edgesGenerator, 10)
        graph = generator.generateGraph()
    }

    @Test
    @RepeatedTest(10)
    fun search() {
        val (source, destination) = generator.generateSourceDestination(graph)
        val expectedPath = DijkstraShortestPathSearch(graph).search(source, destination)
        val actualPath = ParallelWorkersAStarShortestPathSearch(graph, AStarEuclideanDistanceHeuristic(), 4).search(source, destination)
        Assertions.assertEquals(expectedPath, actualPath)
    }
}