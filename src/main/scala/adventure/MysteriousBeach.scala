package adventure

import game.Adventurer
import world.Location
import world.{Location, LocationKey, Item, Usable, Adventure, World}

object MysteriousBeach extends Adventure {

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
    
    def use(): Unit = {
      println("You try to drain the bottle but there's nothing left.")
    }
  }

  val mysteriousBeachKey = new LocationKey("mysterious-beach")
  val tranquilCoveKey = new LocationKey("tranquil-cove")

  val tranquilCove = Location(
    name = "Tranquil Cove",
    description = "You are in a tranquil cove, the azure ocean laps gently against the warm sand.",
    exits =  Map("south" -> mysteriousBeachKey))
    
  val mysteriousBeach = Location(
    name = "Mysterious Beach",
    description = "You are standing on a mysterious beach. To the north you can see a tranquil cove.",
    items = List(oldBottle, emptyBottle),
    exits = Map("north" -> tranquilCoveKey))
  
  
  def begin: Adventurer = {

    val world = World(
      locations = Map(
        mysteriousBeachKey -> mysteriousBeach,
        tranquilCoveKey -> tranquilCove
    )) 

    Adventurer(
        world = world,
        currentLocationKey = mysteriousBeachKey, 
        description = "You are wearing a loose-fitting blue flight suit that appears to have been bleached by prolongued exposure to sun and salt water.")
  
  }
}