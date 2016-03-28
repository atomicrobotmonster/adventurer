package world

/** 
 * Thrown when a location key cannot be resolved to a known location.
 * @param locationKey the location key for which no location is known to exist
 */
class UnknownLocationException(locationKey: LocationKey) extends Exception(s"Unknown location $locationKey.")