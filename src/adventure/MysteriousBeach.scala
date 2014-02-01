package adventure

import game.Adventurer
import world.Location
import world.Item

object MysteriousBeach {

  val displayExitsOnLook = false
  
  val oldBottle = Item(
    label = "Old glass bottle",
    adjectives = Set("glass", "old"),
    noun = "bottle",
    description = "A clear blue-glass bottle, stoppered with a cork. There's a piece of paper inside.")

  val emptyBottle = Item(
    label = "Empty glass whiskey bottle",
    adjectives = Set("empty", "glass", "whiskey"),
    noun = "bottle",
    description = "An empty bottle with a sun-bleached label indicating the bottle once contained whiskey.")

  val tranquilCove = Location(
    name = "Tranquil Cove",
    description = "You are in a tranquil cove, the azure ocean laps gently against the warm sand.")  
    
  val mysteriousBeach = Location(
    name = "Mysterious Beach",
    description = "You are standing on a mysterious beach.",
    items = List(oldBottle, emptyBottle),
    exits = Map( "north" -> tranquilCove))
  
  val start = mysteriousBeach

  def begin(): Adventurer = {
    new Adventurer(currentLocation = start, items = List())
  }

}