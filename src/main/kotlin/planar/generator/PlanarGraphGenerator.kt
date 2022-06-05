package planar.generator

import algorithms.astar.heuristic.AStarEuclideanDistanceHeuristic
import algorithms.astar.sequential.SequentialAStarWithoutExploredStateShortestPathSearch
import generators.EdgesGenerator
import generators.GraphGenerator
import generators.VerticesGenerator
import graph.Graph
import graph.impl.PlanarVertex
import planar.PlanarEdge
import planar.PlanarGraph
import utils.euclideanDistance
import kotlin.random.Random

public class PlanarGraphGenerator(
    private val verticesGenerator: VerticesGenerator<PlanarVertex>,
    private val edgesGenerator: EdgesGenerator<PlanarVertex, PlanarEdge>,
    private val minimalAllowedPathLength: Int
) : GraphGenerator<PlanarVertex, PlanarEdge> {

    override fun generateGraph(): Graph<PlanarVertex, PlanarEdge> {
        val vertices = verticesGenerator.generateVertices()
        val graph = PlanarGraph()
        graph.addVertices(vertices)
        edgesGenerator.addEdgesToGraph(graph)
        return graph;
    }

    override fun generateGraphWithSourceAndDestination(): Triple<Graph<PlanarVertex, PlanarEdge>, PlanarVertex, PlanarVertex> {
        val graph = generateGraph()
        val sourceDestination = generateSourceDestination(graph)
        return Triple(graph, sourceDestination.first, sourceDestination.second)
    }

    override fun generateSourceDestination(
        graph: Graph<PlanarVertex, PlanarEdge>
    ): Pair<PlanarVertex, PlanarVertex> {
        while (true) {
            val source = graph.vertices.random()
            val destination = graph.vertices.random()

            if (source == destination) continue

            val path = SequentialAStarWithoutExploredStateShortestPathSearch(graph, AStarEuclideanDistanceHeuristic()).search(source, destination)

            if (path.isNotEmpty() && path.distanceInVertices >= minimalAllowedPathLength) {
                return Pair(source, destination)
            }
        }
    }
}