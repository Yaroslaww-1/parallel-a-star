package algorithms

import graph.impl.IdVertex
import graph.impl.WeightedEdge
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal class AbstractShortestPathTest {
    private val v1: IdVertex = IdVertex()
    private val v2: IdVertex = IdVertex()
    private val v3: IdVertex = IdVertex()

    private val e1: WeightedEdge = WeightedEdge(1.0)
    private val e2: WeightedEdge = WeightedEdge(2.0)

    @Test
    fun testEquals() {
        val orderedPath = ShortestPath.ordered(v1)
        orderedPath.append(v2, e1)
        orderedPath.append(v3, e2)

        val reversedPath = ShortestPath.reversed(v3)
        reversedPath.prepend(v2, e2)
        reversedPath.prepend(v1, e1)

        assertTrue(orderedPath == reversedPath)
        assertTrue(reversedPath == orderedPath)
        assertTrue(orderedPath == orderedPath)

        val path1 = ShortestPath.reversed(v3)
        assertFalse(path1 == orderedPath)
        assertFalse(path1 == reversedPath)

        val path2 = ShortestPath.ordered(v3)
        path2.append(v1, e1)
        path2.append(v2, e2)
        assertFalse(path2 == orderedPath)
        assertFalse(path2 == reversedPath)
        assertFalse(path2 == path1)
    }
}