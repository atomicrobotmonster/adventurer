package world

/**
 * An active instance of the game world for an adventure.
 */
case class World(
	locations: Map[LocationKey,Location]
) {
	def hasLocation(location: LocationKey): Boolean = locations.contains(location)

	def addItemToLocation(locationKey: LocationKey, item: Item): World = {
		val modifiedLocation = locations(locationKey).addItem(item)

		copy(locations = locations + (locationKey -> modifiedLocation))
	}

	def removeItemFromLocation(locationKey: LocationKey, item: Item): World = {
		val modifiedLocation = locations(locationKey).removeItem(item)

		copy(locations = locations + (locationKey -> modifiedLocation))
	}
}