package algorithms.astar.heuristic

import graph.impl.PlanarVertex
import java.lang.Math.*

public class AStarEuclideanCPUIntenseDistanceHeuristic<V : PlanarVertex> : AStarAdmissibleHeuristic<V> {
    override fun getDistanceEstimate(source: V, destination: V): Int {
        cbrt(tan(atan(tan(atan(tan(atan(tan(atan(tan(atan(123456789.123456789))))))))))) // simulate CPU intense operation
        return source.distance(destination).toInt()
    }
}