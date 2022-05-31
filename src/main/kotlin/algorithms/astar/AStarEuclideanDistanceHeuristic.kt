package algorithms.astar

import graph.impl.PlanarVertex

public class AStarEuclideanDistanceHeuristic<V : PlanarVertex> : AStarAdmissibleHeuristic<V> {
    override fun getDistanceEstimate(source: V, destination: V): Double {
        return source.distance(destination)
    }
}