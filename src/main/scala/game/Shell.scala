package game

import adventure.MysteriousBeach
import world._

import scala.collection.immutable.ListSet
import scala.io.StdIn

import scala.util.{Try,Success,Failure}

object Shell extends App {
 
  class ShellParser(adventure: Adventure) {
    var adventurer = adventure.begin

    private var adventuring = false

    val parseCommand = parserOf(parserCommands(this):_*)
  
    startAdventuring
    do {
      adventurer = parseCommand(getInputTokens)
    } while (isAdventuring)

    private def parserOf(commands: Command*): CommandHandler = {
      val gameHandlers = commands map {_.handler}
      
      val baseHandlers = List[CommandHandler](
        { case List("help") | List("commands") => 
            println ((commands flatMap {_.verbs}) ++ List("help", "commands") mkString ", ")
            adventurer 
        },
        { case List("") => adventurer }, 
        { case _ => 
            println("You don't know how to do that.")
            adventurer 
        })

      (gameHandlers ++ baseHandlers) reduceLeft { _ orElse _ }
    }

    private def getInputTokens: List[String] = {
      print("\n> ")
      val playerInput = StdIn.readLine.trim.toLowerCase
      println

      playerInput.split("\\s+").toList
    }

    def startAdventuring(): Unit = {
      adventuring = true
      showLocation(adventurer)
    }

    def stopAdventuring(): Adventurer = {
      adventuring = false
      println("You'll never win that way.")
      adventurer
    }

    def isAdventuring: Boolean = adventuring

    def exits(): Adventurer = {
      if (adventurer.currentLocation.hasExits) {
        println("You can travel:")
        adventurer.currentLocation.exits foreach { 
          case (exitLabel: String, locationKey: LocationKey) => 
            val location = adventurer.world.locations(locationKey)
            println(s"* $exitLabel -> ${location.name}") 
        }
      } else {
        println("There are no apparent exits...")
      }
      adventurer
    }

    def showLocation(adventurer: Adventurer): Unit = {
      println(adventurer.currentLocation.description)
      if (adventurer.currentLocation.hasItems) {
        println("\nYou see the following items:")
        adventurer.currentLocation.items foreach { i => println(s"* ${i.label}") }
      }
    }

    def look(): Adventurer = {
      showLocation(adventurer)
      if (adventure.displayExitsOnLook) {
        println()
        exits
      }
      adventurer
    }

    def examine(itemNameParts: List[String]): Adventurer = { 
      itemNameParts match {
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
      adventurer
    }

    def move(exitNameParts: List[String]): Adventurer = {
      if (!exitNameParts.isEmpty) {
        adventurer.currentLocation.findNamedExit(exitNameParts.head) match {
          case Some(locationKey) => {
            adventurer.moveTo(locationKey) match {
              case Success(newAdventurer) =>
                showLocation(newAdventurer)
                newAdventurer
              case Failure(_) =>
                println("Something is wrong. Can't move to that location.")
                adventurer
            }
          }
          case _ => {
            invalidDestination
            adventurer
          }
        }
      } else {
        println("Where do you want to go?")
        adventurer
      }
    }

    def useExit(mightBeAnExitName: String): Adventurer = {
      adventurer.currentLocation.exits.get(mightBeAnExitName) match {
        case Some(destination) => {
          adventurer.moveTo(destination) match {
            case Success(newAdventurer) =>           
              showLocation(newAdventurer)
              newAdventurer
            case Failure(_) =>
              println("Something is wrong. Can't move to that location.")
              adventurer
          }
        }
        case _ => { 
          invalidDestination
          adventurer
        }
      }
    }

    def take(itemNameParts: List[String]): Adventurer = {
      itemNameParts match {
        case Nil => 
          println("What do you want to take?")
          adventurer
        case List(_*) => {
          val itemName = itemNameParts.mkString(" ")
            adventurer.currentLocation.findNamedItem(itemName) match {
            case List(foundItem) => {
              val modifiedAdventurer = adventurer.takeItem(foundItem)
              println(s"You take the $itemName.")
              modifiedAdventurer
            }
            case List(_, _*) => 
              println(s"There is more than one $itemName. You need to be more specific.")
              adventurer
            case _ => 
              println("You don't see that here.")
              adventurer
          }
        }
      }
    }

    def drop(itemNameParts: List[String]): Adventurer = {
      itemNameParts match {
        case Nil => 
          println("What do you want to drop?")
          adventurer
        case List(_*) => {
          val itemName = itemNameParts.mkString(" ")
          adventurer.findNamedItem(itemName) match {
            case List(foundItem) => {
              val modifiedAdventurer = adventurer.dropItem(foundItem)
              println(s"You drop the $itemName.")
              modifiedAdventurer
            }
            case List(_, _*) => 
              println(s"You are carrying than one $itemName. You need to be more specific.")
              adventurer
            case _ => println("You are not carrying that.")
              adventurer
          }
        }
      }
    }

    def useItem(itemNameParts: List[String]): Adventurer = {
      itemNameParts match {
        case Nil => println("What do you want to use?")
        case List(_*) => {
          val itemName = itemNameParts.mkString(" ")
            adventurer.findNamedItem(itemName) match {
            case List(usableItem: Usable) => usableItem.use
            case List(unusableItem) => println(s"You use the $itemName. Nothing happens")
            case List(_, _*) => println(s"You are carrying more than one $itemName. You need to be more specific.")
            case _ => println("You aren't carrying that.")
          }
        }
      }
      adventurer
    }

    def listInventory(): Adventurer = {
      println("You are carrying:")
      adventurer.items foreach { item => println(s"* ${item.label}") }
      adventurer
    }

    def invalidDestination(): Adventurer = {
      println("You can't go that way.")
      adventurer
    }
  }
  
  
  type CommandHandler = PartialFunction[List[String],Adventurer]

  case class Command(verbs: List[String], handler: CommandHandler)
  
  private def parserCommands(avatar: ShellParser): List[Command] = {
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


  new ShellParser(MysteriousBeach)

}