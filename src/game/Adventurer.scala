package game

import world.Location
import world.Item

case class Adventurer(var currentLocation: Location, items: List[Item], description: String) {
  def moveTo(newLocation: Location) = { currentLocation = newLocation }
  
}
