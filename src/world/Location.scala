package world

import scala.collection.mutable.Map

case class Location(name: String, description: String, items: List[Item] = List(), exits: Map[String,Location] = Map()) {
  def hasItems = !items.isEmpty
  
  def hasExits = !exits.isEmpty

  def findNamedItem(name: String): List[Item] = {
    val nameParts = name.split("\\s+").toList

    if (!nameParts.isEmpty) {
      val adjectives = nameParts.init.toSet
      val noun = nameParts.last

      items.filter { item => adjectives.subsetOf(item.adjectives) && item.noun == noun }
    } else List()
  }
  
  def findNamedExit(name: String): Option[Location] = {
    exits.get(name)
  }
  
  def addExit(name: String, destination: Location) = exits += (name -> destination)
}