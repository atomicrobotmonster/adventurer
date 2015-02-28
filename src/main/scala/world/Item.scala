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

/**
 * Mixin for items to usable in isolation.
 */
trait Usable {
  self: Item =>
  def use: Unit
}

/**
 * Mixin for items usable only in conjunction with another item.
 */
trait UsableWith {
  self: Item =>
  def useWith(otherItem: Item): Unit
}

class Item(val label: String, val adjectives: Set[String], val noun: String, val description: String)