package algorithms.astar.parallel.workers

import algorithms.astar.utils.ExploredPath
import algorithms.astar.utils.ExploredPathComparator
import graph.Vertex
import java.util.*

internal class Worker<V : Vertex> {
    private val q = Collections.synchronizedSortedSet(TreeSet(ExploredPathComparator<V>()))

    val minimalTotalDistance: Int
        get() {
            synchronized(q) {
                if (q.isNotEmpty()) return q.first().totalDistance
                else return Int.MAX_VALUE
            }
        }

    val hasVerticesToProcess: Boolean
        get() {
            synchronized(q) {
                return q.isNotEmpty()
            }
        }

    fun add(path: ExploredPath<V>) {
        synchronized(q) {
            q.add(path)
        }
    }

    fun remove(path: ExploredPath<V>?) {
        synchronized(q) {
            if (path != null && q.contains(path)) q.remove(path)
        }
    }

    fun pollShortestPath(): ExploredPath<V> {
        synchronized(q) {
            val first = q.first()
            q.remove(first)
            return first
        }
    }
}