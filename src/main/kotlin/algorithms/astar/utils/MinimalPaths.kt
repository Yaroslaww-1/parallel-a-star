package algorithms.astar.utils

import java.util.concurrent.ConcurrentHashMap

internal typealias MinimalPaths<V> = ConcurrentHashMap<V, ExploredPath<V>>

