package game

import world._

import scala.util.{Try,Success,Failure}

case class Adventurer(
	world: World,
	currentLocationKey: LocationKey, 
	items: List[Item] = Nil, 
	description: String) extends ItemContainer {
  
  def moveTo(newLocation: LocationKey): Try[Adventurer] = Try {
  	if (world.hasLocation(newLocation))
  		copy(currentLocationKey = newLocation)
  	else
  		throw new UnknownLocationException(newLocation)
  }
  
  def takeItem(item: Item): Adventurer = {
	copy(
		items = item :: items,
		world = world.removeItemFromLocation(currentLocationKey, item)
	)
  }
          
  def dropItem(item: Item): Adventurer = {
  	copy(
  		items = items.filterNot { _ == item },
  		world = world.addItemToLocation(currentLocationKey, item)
  	)
  }

  def currentLocation: Location = {
  	world.locations(currentLocationKey) //will always return a location
  }

}
