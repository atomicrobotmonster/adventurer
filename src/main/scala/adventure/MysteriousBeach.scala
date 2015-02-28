package adventure

import game.Adventurer
import world.Location
import world.{Item, Usable}

import scala.collection.immutable.ListSet

object MysteriousBeach {

  val displayExitsOnLook = true
  
  val oldBottle = new Item(
    label = "Old glass bottle",
    adjectives = Set("glass", "old"),
    noun = "bottle",
    description = "A clear blue-glass bottle, stoppered with a cork. There's a piece of paper inside.")

  val emptyBottle = new Item(
    label = "Empty glass whiskey bottle",
    adjectives = Set("empty", "glass", "whiskey"),
    noun = "bottle",
    description = "An empty bottle with a sun-bleached label indicating the bottle once contained whiskey.") with Usable {
    
    def use: Unit = {
      println("You try to drain the bottle but there's nothing left.")
    }
  }

  val tranquilCove = Location(
    name = "Tranquil Cove",
    description = "You are in a tranquil cove, the azure ocean laps gently against the warm sand.")  
    
  val mysteriousBeach = Location(
    name = "Mysterious Beach",
    description = "You are standing on a mysterious beach. To the north you can see a tranquil cove.",
    items = ListSet(oldBottle, emptyBottle))
  
  mysteriousBeach.addExit("north", tranquilCove)  
  tranquilCove.addExit("south", mysteriousBeach)
  
  val start = mysteriousBeach

  def begin(): Adventurer = {
    new Adventurer(
        currentLocation = start, 
        description = "You are wearing a loose-fitting blue flight suit that appears to have been bleached by prolongued exposure to sun and salt water.")
  }

}