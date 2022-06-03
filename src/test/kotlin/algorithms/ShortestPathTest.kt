package algorithms

import graph.impl.IdVertex
import graph.impl.StandardGraph
import graph.impl.WeightedEdge
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ShortestPathTest {
    private val v1: IdVertex = IdVertex()
    private val v2: IdVertex = IdVertex()
    private val v3: IdVertex = IdVertex()
    private val v4: IdVertex = IdVertex()

    private val e12: WeightedEdge = WeightedEdge(10)
    private val e23: WeightedEdge = WeightedEdge(20)
    private val e34: WeightedEdge = WeightedEdge(5)

    @Test
    fun testEquals() {
        val orderedPath1 = ShortestPath.empty(v1)
        orderedPath1.append(v2, e12)
        orderedPath1.append(v3, e23)
        orderedPath1.append(v4, e34)

        val reversedPath1 = ShortestPath.empty(v4)
        reversedPath1.prepend(v3, e34)
        reversedPath1.prepend(v2, e23)
        reversedPath1.prepend(v1, e12)

        val reversedPath2 = ShortestPath.empty(v3)

        val orderedPath3 = ShortestPath.empty(v3)
        orderedPath3.append(v1, e12)
        orderedPath3.append(v2, e23)

        assertTrue(orderedPath1 == reversedPath1)
        assertTrue(orderedPath1 != reversedPath2)
        assertTrue(orderedPath1 != orderedPath3)

        assertTrue(reversedPath1 == orderedPath1)
        assertTrue(reversedPath1 != reversedPath2)
        assertTrue(reversedPath1 != orderedPath3)

        assertTrue(reversedPath2 != orderedPath1)
        assertTrue(reversedPath2 != reversedPath1)
        assertTrue(reversedPath2 != orderedPath3)

        assertTrue(orderedPath3 != orderedPath1)
        assertTrue(orderedPath3 != reversedPath1)
        assertTrue(orderedPath3 != reversedPath2)
    }

    @Test
    fun ofParentMapAndDestination() {
        val graph = StandardGraph<IdVertex, WeightedEdge>(true)
        graph.addVertices(listOf(v1, v2, v3, v4))
        graph.addEdge(v1, v2, e12)
        graph.addEdge(v2, v3, e23)
        graph.addEdge(v3, v4, e34)

        val parent: HashMap<IdVertex, IdVertex> = hashMapOf()
        parent[v4] = v3
        parent[v3] = v2
        parent[v2] = v1
        val actualPath = ShortestPath.ofParentMapAndDestination(parent, v4, graph)

        val expectedPath = ShortestPath.empty(v1)
        expectedPath.append(v2, e12)
        expectedPath.append(v3, e23)
        expectedPath.append(v4, e34)

        assertTrue(expectedPath == actualPath)
    }

    @Test
    fun distanceInVertices() {
        val path = ShortestPath.empty(v1)
        path.append(v2, e12)
        path.append(v3, e23)
        path.append(v4, e34)
        assertEquals(4, path.distanceInVertices)
    }

    @Test
    fun getTotalWeight() {
        val path = ShortestPath.empty(v2)
        path.prepend(v1, e12)
        path.append(v3, e23)
        path.append(v4, e34)
        assertEquals(e12.distance + e23.distance + e34.distance, path.totalWeight)
    }

    @Test
    fun getSource() {
        val path = ShortestPath.empty(v2)
        path.prepend(v1, e12)
        path.append(v3, e23)
        path.append(v4, e34)
        assertEquals(v1, path.source)
    }

    @Test
    fun getDestination() {
        val path = ShortestPath.empty(v2)
        path.prepend(v1, e12)
        path.append(v3, e23)
        path.append(v4, e34)
        assertEquals(v4, path.destination)
    }

    @Test
    fun containsEdge() {
        val path = ShortestPath.empty(v1)
        path.append(v2, e12)
        path.append(v3, e23)
        path.append(v4, e34)
        assertTrue(path.containsEdge(v1, v2, e12))
        assertTrue(path.containsEdge(v2, v3, e23))
        assertTrue(path.containsEdge(v3, v4, e34))
    }
}