package game

import adventure.MysteriousBeach
import world.Location
import world.Item

import scala.collection.immutable.ListSet

object Shell extends App {
  private var adventuring = true
  private val adventure = MysteriousBeach

  class ShellAdventurer(var adventurer: Adventurer) {

    var inventory: ListSet[Item] = ListSet()

    def exits {
      if (adventurer.currentLocation.hasExits) {
        println("You can travel:")
        adventurer.currentLocation.exits foreach { case (exitLabel: String, location: Location) => println(s"* $exitLabel -> ${location.name}") }
      } else {
        println("There are no apparent exits...")
      }
    }

    def showLocation {
      println(adventurer.currentLocation.description)
      if (adventurer.currentLocation.hasItems) {
        println("\nYou see the following items:")
        adventurer.currentLocation.items foreach { i => println(s"* ${i.label}") }
      }
    }

    def look {
      showLocation
      if (adventure.displayExitsOnLook) {
        println()
        exits
      }
    }

    def stopAdventuring = adventuring = false

    def examine(itemNameParts: List[String]) = itemNameParts match {
      case List("me") => println(adventurer.description)
      case List() =>
        println("What would you like to examine?")
      case List(_*) => {
        val itemName = itemNameParts.mkString(" ")
          Item.findNamedItem(itemName, adventurer.currentLocation.items ++ inventory) match {
          case List(foundItem) => println(foundItem.description)
          case List(_, _*) => println(s"There is more than one $itemName. You need to be more specific.")
          case _ => println("I don't know what that is.")
        }
      }
    }

    def move(exitNameParts: List[String]) {
      if (!exitNameParts.isEmpty) {

        adventurer.currentLocation.findNamedExit(exitNameParts.head) match {
          case Some(location) => {
            adventurer.moveTo(location)
            showLocation
          }
          case _ => invalidDestination
        }
      } else println("Where do you want to go?")
    }

    def useExit(mightBeAnExitName: String) {
      adventurer.currentLocation.exits.get(mightBeAnExitName) match {
        case Some(destination) => {
          adventurer.moveTo(destination)
          showLocation
        }
        case _ => invalidDestination
      }
    }

    def take(itemNameParts: List[String]) {
      itemNameParts match {
        case Nil => println("What do you want to take?")
        case List(_*) => {
          val itemName = itemNameParts.mkString(" ")
            adventurer.currentLocation.findNamedItem(itemName) match {
            case List(foundItem) => {
              adventurer.currentLocation.removeItem(foundItem)
              inventory += foundItem
              println(s"You take the $itemName.")
            }
            case List(_, _*) => println(s"There is more than one $itemName. You need to be more specific.")
            case _ => println("You don't see that here.")
          }
        }
      }
    }

    def drop(itemNameParts: List[String]) {
      itemNameParts match {
        case Nil => println("What do you want to take?")
        case List(_*) => {
          val itemName = itemNameParts.mkString(" ")
            Item.findNamedItem(itemName, inventory.toList) match {
            case List(foundItem) => {
              adventurer.currentLocation.addItem(foundItem)
              inventory = inventory - foundItem
              println(s"You drop the $itemName.")
            }
            case List(_, _*) => println(s"You are carrying than one $itemName. You need to be more specific.")
            case _ => println("You are not carrying that.")
          }
        }
      }
    }

    def listInventory {
        println("You are carrying:")
        inventory foreach { item => println(s"* ${item.label}") }
    }

    def invalidAction = println("You don't know how to do that.")

    def invalidDestination = println("You can't go that way.")

  }
  private val avatar = new ShellAdventurer(adventure.begin)

  avatar.showLocation

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
      case "walk" :: exitNameParts => avatar.move(exitNameParts)
      case List("exits") => avatar.exits
      case List("") => Unit
      case List("help") => showCommands
      case List("commands") => showCommands
      case List("north") => avatar.useExit("north")
      case List("south") => avatar.useExit("south")
      case List("east") => avatar.useExit("east")
      case List("west") => avatar.useExit("west")
      case "take" :: itemNameParts => avatar.take(itemNameParts)
      case "drop" :: itemNameParts => avatar.drop(itemNameParts)
      case List("inventory") => avatar.listInventory
      case _ => avatar.invalidAction
    }

  } while (adventuring)

  println("You'll never win that way.")

  def showCommands {
    println("quit, look, examine, go, walk, north, south, exits, help, commands")
  }

}