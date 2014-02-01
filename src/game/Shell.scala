package game

import adventure.MysteriousBeach

object Shell extends App {
  private var adventuring = true
  val adventure = MysteriousBeach
  private var adventurer = adventure.begin

  class ShellAdventurer(adventurer: Adventurer) {

    def look {
      println(adventurer.currentLocation.description)
      if (adventurer.currentLocation.hasItems) {
        println("\nYou see the following items:")
        adventurer.currentLocation.items.foreach { i => println(s"* ${i.label}") }
      }
    }

    def stopAdventuring = adventuring = false

    def examine(itemNameParts: List[String]) {
      if (itemNameParts.isEmpty) {
        println("What would you like to examine?")
      } else {
        val itemName = itemNameParts.mkString(" ")
        adventurer.currentLocation.findNamedItem(itemName) match {
          case List(foundItem) => println(foundItem.description)
          case List(_, _*) => println(s"Which $itemName did you mean?")
          case List() => println("I don't know what that is.")
        }
      }
    }
  }

  private val avatar = new ShellAdventurer(adventurer)

  avatar.look

  do {

    Console.print("\n> ")
    val playerInput = Console.readLine.trim.toLowerCase()
    Console.println

    val inputTokens: List[String] = playerInput.split("\\s+").toList

    inputTokens match {
      case List("quit") => avatar.stopAdventuring
      case List("look") => avatar.look
      case "examine" :: itemNameParts => avatar.examine(itemNameParts)
      case _ => println("I don't understand.")
    }

  } while (adventuring)

  println("You'll never win that way.")

}