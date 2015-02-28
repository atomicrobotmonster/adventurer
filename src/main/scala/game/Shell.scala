package game

import adventure.MysteriousBeach
import world.Location
import world.Item

import scala.collection.immutable.ListSet
import scala.io.StdIn

object Shell extends App {
  private val adventure = MysteriousBeach

  class ShellAdventurer(var adventurer: Adventurer) {

    private var adventuring = false

    def startAdventuring: Unit = {
      adventuring = true
      showLocation
    }

    def stopAdventuring: Unit = {
      adventuring = false
      println("You'll never win that way.")
    }

    def isAdventuring: Boolean = adventuring

    def exits: Unit = {
      if (adventurer.currentLocation.hasExits) {
        println("You can travel:")
        adventurer.currentLocation.exits foreach { case (exitLabel: String, location: Location) => println(s"* $exitLabel -> ${location.name}") }
      } else {
        println("There are no apparent exits...")
      }
    }

    def showLocation: Unit = {
      println(adventurer.currentLocation.description)
      if (adventurer.currentLocation.hasItems) {
        println("\nYou see the following items:")
        adventurer.currentLocation.contents foreach { i => println(s"* ${i.label}") }
      }
    }

    def look: Unit = {
      showLocation
      if (adventure.displayExitsOnLook) {
        println()
        exits
      }
    }

    def examine(itemNameParts: List[String]): Unit = itemNameParts match {
      case List("me") => println(adventurer.description)
      case List() =>
        println("What would you like to examine?")
      case List(_*) => {
        val itemName = itemNameParts.mkString(" ")
          Item.findNamedItem(itemName, adventurer.currentLocation.items ++ adventurer.items) match {
          case List(foundItem) => println(foundItem.description)
          case List(_, _*) => println(s"There is more than one $itemName. You need to be more specific.")
          case _ => println("I don't know what that is.")
        }
      }
    }

    def move(exitNameParts: List[String]): Unit = {
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

    def useExit(mightBeAnExitName: String): Unit = {
      adventurer.currentLocation.exits.get(mightBeAnExitName) match {
        case Some(destination) => {
          adventurer.moveTo(destination)
          showLocation
        }
        case _ => invalidDestination
      }
    }

    def take(itemNameParts: List[String]): Unit = {
      itemNameParts match {
        case Nil => println("What do you want to take?")
        case List(_*) => {
          val itemName = itemNameParts.mkString(" ")
            adventurer.currentLocation.findNamedItem(itemName) match {
            case List(foundItem) => {
              adventurer.currentLocation.removeItem(foundItem)
              adventurer.addItem(foundItem)
              println(s"You take the $itemName.")
            }
            case List(_, _*) => println(s"There is more than one $itemName. You need to be more specific.")
            case _ => println("You don't see that here.")
          }
        }
      }
    }

    def drop(itemNameParts: List[String]): Unit = {
      itemNameParts match {
        case Nil => println("What do you want to drop?")
        case List(_*) => {
          val itemName = itemNameParts.mkString(" ")
            adventurer.findNamedItem(itemName) match {
            case List(foundItem) => {
              adventurer.currentLocation.addItem(foundItem)
              adventurer.removeItem(foundItem)
              println(s"You drop the $itemName.")
            }
            case List(_, _*) => println(s"You are carrying than one $itemName. You need to be more specific.")
            case _ => println("You are not carrying that.")
          }
        }
      }
    }

    def useItem(itemNameParts: List[String]): Unit = {
      itemNameParts match {
        case Nil => println("What do you want to use?")
        case List(_*) => {
          val itemName = itemNameParts.mkString(" ")
            adventurer.findNamedItem(itemName) match {
            case List(foundItem) => {
              println(s"You use the $itemName. Nothing happens")
            }
            case List(_, _*) => println(s"You are carrying more than one $itemName. You need to be more specific.")
            case _ => println("You aren't carrying that.")
          }
        }
      }
    }

    def listInventory: Unit = {
        println("You are carrying:")
        adventurer.contents foreach { item => println(s"* ${item.label}") }
    }

    def invalidDestination: Unit = println("You can't go that way.")
  }
  
  

  type CommandHandler = PartialFunction[List[String],Unit]

  case class Command(verbs: List[String], handler: CommandHandler)
  
  private def parserCommands(avatar: ShellAdventurer): List[Command] = {
    val quit = Command(
      verbs = List("quit","exit"),
      handler = { case List("quit") | List("exit") => avatar.stopAdventuring }
    ) 

    val look = Command(
      verbs = List("look"),
      handler = { case List("look") => avatar.look } 
    )

    val examine = Command(
      verbs = List("examine"),
      handler = { case "examine" :: itemNameParts => avatar.examine(itemNameParts) }
    )

    val go = Command(
      verbs = List("go"),
      handler = { case "go" :: exitNameParts => avatar.move(exitNameParts) }
    )

    val walk = Command(
      verbs = List("walk"),
      handler = { case "walk" :: exitNameParts => avatar.move(exitNameParts) }
    )

    val exits = Command(
      verbs = List("exits"),
      handler = { case List("exits") => avatar.exits }
    )

   val compassRose = Command(
      verbs = List("north","south","east","west"),
      handler = { case token @ (List("north") | List("south") | List("east") | List("west")) => avatar.useExit(token.head) }
    )

    val take = Command( 
      verbs = List("take"),
      handler = { case "take" :: itemNameParts => avatar.take(itemNameParts) }
    )

    val drop = Command(
      verbs = List("drop"),
      handler = { case "drop" :: itemNameParts => avatar.drop(itemNameParts) }
    )

    val inventory = Command(
      verbs = List("inventory"),
      handler = { case List("inventory") => avatar.listInventory }
    )

    val use = Command(
      verbs = List("use"),
      handler = { case "use" :: itemNameParts => avatar.useItem(itemNameParts) }
    )
    
    List(quit, look, examine, go, walk, exits, compassRose, take, drop, inventory, use)  
  }

  private def parserOf(commands: Command*): CommandHandler = {
    val gameHandlers = commands map {_.handler}
    
    val baseHandlers = List[CommandHandler](
      { case List("help") | List("commands") => println ((commands flatMap {_.verbs}) ++ List("help", "commands") mkString ", ") },
      { case List("") => Unit }, 
      { case _ => println("You don't know how to do that.") })

    (gameHandlers ++ baseHandlers) reduceLeft { _ orElse _ }
  }

  private def getInputTokens: List[String] = {
    print("\n> ")
    val playerInput = StdIn.readLine.trim.toLowerCase
    println

    playerInput.split("\\s+").toList
  }

  val avatar = new ShellAdventurer(adventure.begin)
  val parseCommand = parserOf(parserCommands(avatar):_*)
  
  avatar.startAdventuring
  do parseCommand(getInputTokens) while (avatar.isAdventuring)
}