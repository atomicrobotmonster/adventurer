package world

/**
 * A location in the world. Has items and exits.
 */ 
case class Location(
  name: String, 
  description: String, 
  items: List[Item] = Nil, 
  exits: Map[String,LocationKey] = Map.empty) extends ItemContainer with Thing { 
  
  def addItem(item: Item) = copy(items = item :: items )

  def removeItem(item: Item) = copy(items = items.filterNot { _ == item })

  def hasExits = !exits.isEmpty

  /**
   * Search for the location key for a named exist.
   * @param name the name of the exit
   * @return option of location key; some when exit name is found; none otherwise 
   */
  def findNamedExit(name: String): Option[LocationKey] = {
    exits.get(name)
  }
}