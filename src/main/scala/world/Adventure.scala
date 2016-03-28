package world

import game.Adventurer

/**
 * Implement the game here.  
 */
trait Adventure {
	/**
	 * Create a new game world and an active adventurer. The Adventurer is created here
	 * because description, starting inventory and starting location are provided by the adventure.
	 * @return Adventurer with a newly initialized game world
	 */
	def begin: Adventurer

	/**
	 * Determines whether exits should be displayed automatically when the Adventurer looks in a location.
	 * @return true when exits are displayed as part of a look command
	 */
	def displayExitsOnLook: Boolean
}