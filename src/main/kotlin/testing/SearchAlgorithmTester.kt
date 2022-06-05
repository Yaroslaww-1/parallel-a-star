package testing

import algorithms.ShortestPath
import algorithms.ShortestPathSearch
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import graph.Vertex
import utils.getCurrentDateTime
import utils.toString
import kotlin.system.measureTimeMillis

public class SearchAlgorithmTester {
    public fun <V : Vertex> test(
        algorithm: ShortestPathSearch<V>,
        source: V,
        destination: V,
    ): SearchAlgorithmTestResult {
        var path: ShortestPath<Vertex>?
        val executionTime = measureTimeMillis {
            path = algorithm.search(source, destination) as ShortestPath<Vertex>
        }
        return SearchAlgorithmTestResult(executionTime, path!!)
    }
}

public data class SearchAlgorithmTestResult(
    val timeInMs: Long,
    val path: ShortestPath<Vertex>
) {
    public fun print() {
        val timeInMsPrettified = timeInMs.toString().padStart(2, '0')
        println("time: $timeInMsPrettified ms, path length: ${path.distanceInVertices}")
    }
}

public class SearchAlgorithmResultSaver(private val params: List<Pair<String, String>>) {
    private val outputFileName = "${getCurrentDateTime().toString("yyyy-MM-dd---HH-mm")}---$params.csv"

    private val writer = csvWriter().openAndGetRawWriter(outputFileName)

    public fun saveResult(result: SearchAlgorithmTestResult): Unit = writer.writeRow(listOf(result.timeInMs, result.path.distanceInVertices))

    public fun finish(): Unit = writer.close()
}