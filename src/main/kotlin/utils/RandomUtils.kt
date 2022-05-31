package utils

import kotlin.random.Random

public fun Random.nextUUID(): String {
    return java.util.UUID.randomUUID().toString().substring(0, 8)
}
