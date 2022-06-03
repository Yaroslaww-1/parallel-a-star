package algorithms.astar.heuristic

import graph.impl.PlanarVertex
import java.lang.Math.*

public class AStarManhattanDistanceHeuristic<V : PlanarVertex> : AStarAdmissibleHeuristic<V> {
    override fun getDistanceEstimate(source: V, destination: V): Int {
        return abs(source.x - destination.x) + abs(source.y - destination.y)
    }
}