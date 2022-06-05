package planar.generator

import generators.VerticesGenerator
import graph.impl.PlanarVertex
import kotlin.random.Random

public class PlanarVerticesGenerator(
    private val verticesCount: Int,
    private val maximumAbsoluteCoordinate: Int
) : VerticesGenerator<PlanarVertex> {

    override fun generateVertices(): List<PlanarVertex> {
        val vertices: MutableList<PlanarVertex> = mutableListOf()
        for (i in (0..verticesCount)) {
            vertices.add(
                PlanarVertex(
                    Random.nextInt(-maximumAbsoluteCoordinate, maximumAbsoluteCoordinate),
                    Random.nextInt(-maximumAbsoluteCoordinate, maximumAbsoluteCoordinate),
                )
            )
        }
        return vertices
    }
}