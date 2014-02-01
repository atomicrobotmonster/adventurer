package adventure

import game.Adventurer
import world.Location
import world.Item

object MysteriousBeach {

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

  val mysteriousBeach = Location(
    "Mysterious Beach",
    "You are standing on a mysterious beach.",
    List(oldBottle, emptyBottle))

  val start = mysteriousBeach

  def begin(): Adventurer = {
    new Adventurer(start)
  }

}