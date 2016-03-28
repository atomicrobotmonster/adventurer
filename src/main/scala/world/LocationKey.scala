package world

/**
 * Value class to represent the key for a location. Keys are used to avoid
 * complex dependency ordering during initialization and allow &quot;mutation&quot; of
 * immutable locations without having to make a deep-copy of locations referenced in exits
 */
class LocationKey(val underlying: String) extends AnyVal {
	override def toString = underlying
}