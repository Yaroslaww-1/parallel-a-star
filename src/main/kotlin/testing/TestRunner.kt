package testing

import algorithms.astar.heuristic.AStarEuclideanCPUIntenseDistanceHeuristic
import algorithms.astar.heuristic.AStarEuclideanDistanceHeuristic
import algorithms.astar.parallel.bidirectional.ParallelBidirectionalAStarShortestPathSearch
import algorithms.astar.parallel.workers.ParallelWorkersAStarShortestPathSearch
import algorithms.astar.sequential.SequentialAStarShortestPathSearch
import planar.generator.PlanarGraphGenerator
import planar.generator.PlanarKNearestEdgesGenerator
import planar.generator.PlanarVerticesGenerator

public class TestRunner {
    public companion object {
        public const val DIFFERENT_GRAPHS_GENERATE: Int = 10
        public const val DIFFERENT_SOURCE_DESTINATIONS_GENERATE_ON_THE_SAME_GRAPH: Int = 10
    }

    public fun run() {
        val V = 8000
        val K = 50
        val M = 20
        val P = 8
//        val params = listOf<Pair<String, String>>(
//            Pair("V", V.toString()),
//            Pair("K", K.toString()),
//            Pair("M", M.toString()),
//            Pair("Name", "ParallelWorkers"),
//            Pair("P", P.toString()),
//        )
        val params = listOf(
            Pair("V", V.toString()),
            Pair("K", K.toString()),
            Pair("M", M.toString()),
            Pair("Name", "ParallelWorkers"),
            Pair("Heuristic", "CpuIntense"),
            Pair("P", P.toString()),
        )
//        val params = Params(V = 2000, K = 50, M = 20, ALG_NAME = "Parallel Workers")
//        println("testing for M = ${params.M}, K = ${params.K}, V = ${params.V}")
//        println("testing for M = $M, K = $K, V = ${params.V}")

        val verticesGenerator = PlanarVerticesGenerator(V, 10000)
        val edgesGenerator = PlanarKNearestEdgesGenerator(K)
        val graphGenerator = PlanarGraphGenerator(verticesGenerator, edgesGenerator, M)

        val tester = SearchAlgorithmTester()
        val saver = SearchAlgorithmResultSaver(params)

        for (graphIt in (0..DIFFERENT_GRAPHS_GENERATE)) {
            val graph = graphGenerator.generateGraph()
            for (sourceDestinationIt in (0..DIFFERENT_SOURCE_DESTINATIONS_GENERATE_ON_THE_SAME_GRAPH)) {
                val (source, destination) = graphGenerator.generateSourceDestination(graph)

//                val algorithm = SequentialAStarShortestPathSearch(graph, AStarEuclideanCPUIntenseDistanceHeuristic())
//                val testResult = tester.test(algorithm, source, destination)

//                val algorithm = ParallelBidirectionalAStarShortestPathSearch(graph, AStarEuclideanDistanceHeuristic())
//                val testResult = tester.test(algorithm, source, destination)

                val algorithm = ParallelWorkersAStarShortestPathSearch(graph, AStarEuclideanCPUIntenseDistanceHeuristic(), P)
                val testResult = tester.test(algorithm, source, destination)

                saver.saveResult(testResult)
            }
        }

        saver.finish()
    }
}