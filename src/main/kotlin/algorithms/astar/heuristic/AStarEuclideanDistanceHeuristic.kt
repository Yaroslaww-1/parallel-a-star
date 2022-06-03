package algorithms.astar.heuristic

import graph.impl.PlanarVertex

public class AStarEuclideanDistanceHeuristic<V : PlanarVertex> : AStarAdmissibleHeuristic<V> {
    override fun getDistanceEstimate(source: V, destination: V): Int {
        return source.distance(destination).toInt()
    }
}