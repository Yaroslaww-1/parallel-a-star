package planar.generator

import algorithms.BfsShortestPathSearch
import graph.Graph
import graph.Vertex
import graph.impl.PlanarVertex
import graph.impl.StandardGraph
import planar.PlanarEdge
import planar.PlanarGraph
import utils.euclideanDistance
import kotlin.random.Random

public class PlanarGraphGenerator {

    private fun generateGraph(maximumAbsoluteCoordinate: Int, verticesCount: Int, kNearest: Int) : PlanarGraph {
        val vertices: MutableList<PlanarVertex> = mutableListOf()
        for (i in (0..verticesCount)) {
            vertices.add(
                PlanarVertex(
                    Random.nextInt(-maximumAbsoluteCoordinate, maximumAbsoluteCoordinate),
                    Random.nextInt(-maximumAbsoluteCoordinate, maximumAbsoluteCoordinate),
            ))
        }

        val graph = PlanarGraph()

        graph.addVertices(vertices)

        val verticesCopy = vertices.toMutableList()

        for (vertex in vertices) {
            if (graph.degreeOf(vertex) >= kNearest) continue

            verticesCopy.sortBy { neighbour -> euclideanDistance(vertex.x, vertex.y, neighbour.x, neighbour.y) }

            for (i in (0..kNearest - graph.degreeOf(vertex))) {
                val newNeighbour = verticesCopy[i]
                if (newNeighbour != vertex && !graph.containsEdge(vertex, newNeighbour)) {
                    val euclideanDistance = euclideanDistance(vertex.x, vertex.y, newNeighbour.x, newNeighbour.y)
                    val randomAddition = Random.nextDouble(0.1, maximumAbsoluteCoordinate.toDouble())
                    graph.addEdge(
                        vertex,
                        newNeighbour,
                        PlanarEdge(euclideanDistance + randomAddition))
                }
            }

        }

        return graph
    }

    public fun generateGraphWithSourceAndDestination(
        maximumAbsoluteCoordinate: Int,
        verticesCount: Int,
        kNearest: Int,
        minimalAllowedDistanceInVerticesBetweenSourceAndDestination: Int
    ) : Triple<PlanarGraph, PlanarVertex, PlanarVertex> {
        while (true) {
            val graph = generateGraph(maximumAbsoluteCoordinate, verticesCount, kNearest)

            for (i in (0..100)) {
                val source = graph.vertices.random()
                val destination = graph.vertices.random()

                if (source == destination) continue

                val path = BfsShortestPathSearch(graph).search(source, destination)

                if (path.isNotEmpty() && path.distanceInVertices >= minimalAllowedDistanceInVerticesBetweenSourceAndDestination) {
                    return Triple(graph, source, destination)
                }
            }
        }
    }
}