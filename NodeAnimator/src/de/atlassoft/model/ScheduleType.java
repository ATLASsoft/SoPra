package de.atlassoft.model;

/**
 * Defines all possible types of {@link ScheduleScheme ScheduleSchemes}.
 * 
 * @author Alexander Balogh
 *
 */
public enum ScheduleType {
	
	/**
	 * New Trains start at pre-defined intervals between a given
	 * start and end time.
	 */
	INTERVALL,
	
	/**
	 * The train will drive once a day at the specified time.
	 */
	SINGLE_RIDE,
}
