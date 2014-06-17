package world

import scala.collection.immutable.ListSet
import scala.collection.IterableLike

trait ItemContainer {
  private var _contents = ListSet[Item]()

  def findNamedItem(name: String): List[Item] = {
    val nameParts = name.split("\\s+").toList

    if (!nameParts.isEmpty) {
      val adjectives = nameParts.init.toSet
      val noun = nameParts.last

      contents.filter { item => adjectives.subsetOf(item.adjectives) && item.noun == noun } toList
    } else List()
  }

  def removeItem(item: Item) {
    _contents = _contents - item

    assert(!_contents.contains(item))
  }

  def addItem(item: Item) {
    _contents= _contents + item

    assert(_contents.contains(item))
  }

  def addItems(items: ListSet[Item]) = items foreach addItem

  def hasItems = !contents.isEmpty

  def contents = _contents

}