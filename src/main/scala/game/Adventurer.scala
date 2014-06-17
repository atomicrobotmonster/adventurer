package game

import scala.collection.immutable.ListSet

import world.{ Location, Item, ItemContainer }


case class Adventurer(
	var currentLocation: Location, 
	items: ListSet[Item] = ListSet[Item](), 
	description: String) extends ItemContainer {
  
  addItems(items)

  def moveTo(newLocation: Location) = { currentLocation = newLocation }
  
}
