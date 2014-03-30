package world

import scala.collection.immutable.ListSet

object Item {
  def findNamedItem(name: String, items: ListSet[Item]): List[Item] = 
    findNamedItem(name, items.toList)

  def findNamedItem(name: String, items: List[Item]): List[Item] = {
    val nameParts = name.split("\\s+").toList

    if (!nameParts.isEmpty) {
      val adjectives = nameParts.init.toSet
      val noun = nameParts.last

      items.filter { item => adjectives.subsetOf(item.adjectives) && item.noun == noun } 
    } else List()
  }
}

case class Item(label: String, adjectives: Set[String], noun: String, description: String) {


}