package utils

import kotlin.math.pow
import kotlin.math.sqrt

public fun euclideanDistance(x1: Int, y1: Int, x2: Int, y2: Int): Double {
    return sqrt((x1 - x2).toDouble().pow(2) + (y1 - y2).toDouble().pow(2))
}