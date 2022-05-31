package graph

public abstract class EndpointPair<V : Vertex> private constructor(
    public val source: V,
    public val destination: V
) {
    internal abstract val isDirected: Boolean

    public companion object {
        public fun <V : Vertex> directed(source: V, destination : V) : EndpointPair<V> =
            Directed(source, destination)

        public fun <V : Vertex> unDirected(source: V, destination : V) : EndpointPair<V> =
            UnDirected(source, destination)
    }

    private class Directed<V : Vertex>(
        source: V,
        destination: V
    ) : EndpointPair<V>(source, destination) {

        override val isDirected: Boolean = true

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is EndpointPair<*>) return false

            if (isDirected != other.isDirected) return false

            return source == other.source && destination == other.destination
        }

        override fun hashCode(): Int = listOf(source as Any, destination as Any).toTypedArray().contentHashCode()
    }

    private class UnDirected<V : Vertex>(
        source: V,
        destination: V
    ) : EndpointPair<V>(source, destination) {

        override val isDirected: Boolean = false

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is EndpointPair<*>) return false

            if (isDirected != other.isDirected) return false

            return (source == other.source && destination == other.destination) ||
                    (source == other.destination && destination == other.source)
        }

        override fun hashCode(): Int = source.hashCode() + destination.hashCode()
    }
}