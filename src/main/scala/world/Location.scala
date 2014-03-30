package world

import scala.collection.mutable.Map
import scala.collection.immutable.ListSet

case class Location(name: String, description: String, var items: ListSet[Item] = ListSet(), exits: Map[String,Location] = Map()) {
  def hasItems = !items.isEmpty
  
  def hasExits = !exits.isEmpty

  def findNamedItem(name: String): List[Item] = {
    Item.findNamedItem(name, items.toList)
  }
  
  def removeItem(item: Item) {
    items = items - item

    assert(!items.contains(item))
  }

  def addItem(item: Item) {
    items = items + item

    assert(items.contains(item))
  }

  def findNamedExit(name: String): Option[Location] = {
    exits.get(name)
  }
  
  def addExit(name: String, destination: Location) = exits += (name -> destination)
}