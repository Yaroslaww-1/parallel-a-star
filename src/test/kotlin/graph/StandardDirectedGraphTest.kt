package graph

import graph.impl.EmptyEdge
import graph.impl.IdVertex
import graph.impl.StandardGraph
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

internal class StandardDirectedGraphTest {
    private val v1: IdVertex = IdVertex()
    private val v2: IdVertex = IdVertex()
    private val v3: IdVertex = IdVertex()
    private val v4: IdVertex = IdVertex()

    private lateinit var graph : StandardGraph<IdVertex, EmptyEdge>

    @BeforeEach
    internal fun setUp() {
        graph = StandardGraph(true)
        graph.addVertices(listOf(v1, v2, v3, v4))
        graph.addEdge(v1, v2, EmptyEdge())
        graph.addEdge(v2, v3, EmptyEdge())
        graph.addEdge(v3, v4, EmptyEdge())
        graph.addEdge(v4, v1, EmptyEdge())
    }

    @Test
    fun getVertices() {
        assertEquals(setOf(v1, v2, v3, v4), graph.vertices)
    }

    @Test
    fun addVertex() {
        val v5 = IdVertex()
        graph.addVertex(v5)
        assertEquals(setOf(v1, v2, v3, v4, v5), graph.vertices)
    }

    @Test
    fun addVertices() {
        val v5 = IdVertex()
        val v6 = IdVertex()
        graph.addVertices(listOf(v5, v6))
        assertEquals(setOf(v1, v2, v3, v4, v5, v6), graph.vertices)
    }

    @Test
    fun containsVertex() {
        assertTrue(graph.containsVertex(v1))
    }

    @Test
    fun getEdges() {
        assertEquals(4, graph.edges.size)
        assertTrue(graph.containsEdge(v1, v2))
        assertTrue(graph.containsEdge(v2, v3))
        assertTrue(graph.containsEdge(v3, v4))
        assertTrue(graph.containsEdge(v4, v1))
    }

    @Test
    fun addEdge() {
        graph.addEdge(v1, v3, EmptyEdge())
        assertEquals(5, graph.edges.size)
        assertTrue(graph.containsEdge(v1, v3))
        assertFalse(graph.containsEdge(v3, v1))
    }

    @Test
    fun getEdge() {
        assertNotNull(graph.getEdge(v1, v2))
    }

    @Test
    fun containsEdge() {
        assertTrue(graph.containsEdge(v1, v2))
    }

    @Test
    fun isDirected() {
        assertTrue(graph.isDirected)
    }

    @Test
    fun outgoingNeighboursOf() {
        assertEquals(setOf(v2), graph.outgoingNeighboursOf(v1))
    }

    @Test
    fun degreeOf() {
        assertEquals(1, graph.degreeOf(v1))
    }
}