package utils

import kotlin.random.Random

public fun Random.nextString(): String {
    return java.util.UUID.randomUUID().toString().substring(0, 8)
}
