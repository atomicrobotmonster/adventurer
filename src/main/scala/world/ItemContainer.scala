package world

import scala.language.postfixOps

/**
 * Immutable container of items.
 */
trait ItemContainer {
  def items: List[Item]

  def findNamedItem(name: String): List[Item] = {
    val nameParts = name.split("\\s+").toList

    if (!nameParts.isEmpty) {
      val adjectives = nameParts.init.toSet
      val noun = nameParts.last

      items.filter { item => adjectives.subsetOf(item.adjectives) && item.noun == noun } toList
    } else Nil
  }

  /**
   * Determines whether the container holds any items.
   * @return true when the container holds at least one item
   */
  def hasItems: Boolean = !items.isEmpty

}