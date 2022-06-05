package algorithms.astar.heuristic

import graph.Vertex

public interface AStarAdmissibleHeuristic<V : Vertex> {
    public fun getDistanceEstimate(source: V, destination: V): Int
}