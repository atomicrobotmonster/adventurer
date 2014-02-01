package game

import adventure.MysteriousBeach
import world.Location

object Shell extends App {
  private var adventuring = true
  private val adventure = MysteriousBeach

  class ShellAdventurer(var adventurer: Adventurer) {

    def exits {
      if (adventurer.currentLocation.hasExits) {
        println("You can travel:")
        adventurer.currentLocation.exits foreach { case (exitLabel: String, location: Location) => println(s"* $exitLabel -> ${location.name}") }
      } else {
        println("There are no apparent exits...")
      }
    }

    def look {
      println(adventurer.currentLocation.description)
      if (adventurer.currentLocation.hasItems) {
        println("\nYou see the following items:")
        adventurer.currentLocation.items foreach { i => println(s"* ${i.label}") }
      }
      if (adventure.displayExitsOnLook) exits
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
          case _ => println("I don't know what that is.")
        }
      }
    }

    def move(exitNameParts: List[String]) {
      if (!exitNameParts.isEmpty) {

        adventurer.currentLocation.findNamedExit(exitNameParts .head) match {
          case Some(location) => {
            adventurer = adventurer.moveTo(location)
            look
          }
          case _ => println("You can't go that way.")
        }
      } else println("Where do you want to go?")
    }
  }

  private val avatar = new ShellAdventurer(adventure.begin)

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
      case "go" :: exitNameParts => avatar.move(exitNameParts)
      case List("exits") => avatar.exits
      case List("") => Unit
      case List("help") => showCommands
      case List("commands") => showCommands
      case _ => println("You don't know how to do that.")
    }

  } while (adventuring)

  println("You'll never win that way.")
  
  def showCommands {
    println("quit, look, examine, go, exits, help, commands")
  }

}