package world

import scala.collection.mutable.Map
import scala.collection.immutable.ListSet

case class Location(
  name: String, 
  description: String, 
  items: ListSet[Item] = ListSet(), 
  exits: Map[String,Location] = Map()) extends ItemContainer { 
  
  addItems(items)

  def hasExits = !exits.isEmpty

  def findNamedExit(name: String): Option[Location] = {
    exits.get(name)
  }
  
  def addExit(name: String, destination: Location) = exits += (name -> destination)
}