package game

import world.Location
import world.Item

case class Adventurer(currentLocation: Location, items: List[Item]) {
  def moveTo(newLocation: Location) = copy(currentLocation = newLocation)
  
}
