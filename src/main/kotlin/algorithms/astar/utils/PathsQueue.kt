package algorithms.astar.utils

import graph.Vertex
import java.util.*

internal class PathsQueue<V : Vertex>() {
    private val q: TreeSet<ExploredPath<V>> = TreeSet(ExploredPathComparator())

    fun isNotEmpty() = q.isNotEmpty()

    fun add(path: ExploredPath<V>) = q.add(path)

    fun pollFirst(): Pair<ExploredPath<V>, V> {
        val first = q.pollFirst()!!
        return Pair(first, first.lastVertex)
    }

    fun remove(path: ExploredPath<V>?) {
        if (path != null) q.remove(path)
    }
}

