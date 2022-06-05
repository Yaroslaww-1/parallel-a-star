package planar.generator

import generators.EdgesGenerator
import graph.Graph
import graph.impl.PlanarVertex
import planar.PlanarEdge
import utils.euclideanDistance
import kotlin.random.Random

public class PlanarKNearestEdgesGenerator(
    private val k: Int
) : EdgesGenerator<PlanarVertex, PlanarEdge> {

    override fun addEdgesToGraph(graph: Graph<PlanarVertex, PlanarEdge>) {
        val verticesCopy = graph.vertices.toMutableList()

        for (vertex in graph.vertices) {
            if (graph.degreeOf(vertex) >= k) continue

            verticesCopy.sortBy { neighbour -> euclideanDistance(vertex.x, vertex.y, neighbour.x, neighbour.y) }

            for (i in (0..k - graph.degreeOf(vertex))) {
                val newNeighbour = verticesCopy[i]
                if (newNeighbour != vertex && !graph.containsEdge(vertex, newNeighbour)) {
                    graph.addEdge(
                        vertex,
                        newNeighbour,
                        generateEdge(vertex, newNeighbour))
                }
            }

        }
    }

    private fun generateEdge(source: PlanarVertex, destination: PlanarVertex): PlanarEdge {
        val euclideanDistance = euclideanDistance(source.x, source.y, destination.x, destination.y).toInt()
        val randomAddition = Random.nextInt(1, 10000)
        return PlanarEdge(euclideanDistance + randomAddition)
    }
}