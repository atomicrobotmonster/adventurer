package world

case class Location(name: String, description: String, items: List[Item]) {
  def hasItems = !items.isEmpty

  def findNamedItem(name: String): List[Item] = {
    val nameParts = name.split("\\s+").toList

    if (!nameParts.isEmpty) {
      val adjectives = nameParts.init.toSet
      val noun = nameParts.last

      items.filter { item => adjectives.subsetOf(item.adjectives) && item.noun == noun }
    } else List()
  }
}