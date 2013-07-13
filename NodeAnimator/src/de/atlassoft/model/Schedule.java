package de.atlassoft.model;

import java.util.HashMap;
import java.util.Map;

/**
 * A concrete schedule that can be used exactly once. The specified arrival
 * times are relative to the start of a specific simulation run. Instances of
 * this class can be generated from a {@link ScheduleScheme} which is a broader
 * definition for schedules.
 * 
 * @author Alexander Balogh
 * 
 */
public class Schedule {

	/**
	 * The scheme this schedule was created from.
	 */
	private ScheduleScheme scheme;
	
	/**
	 * The stations the train must stop at.
	 */
	private Node[] stations;
	
	/**
	 * The arrival time at the each station in milliseconds after the
	 * start of the simulation (in simulation time).
	 */
	private Map<Node, Integer> arrivalTimes;
   
	/**
	 * The idle times at each station in milliseconds (simulation time).
	 */
	private Map<Node, Integer> idleTimes;



	/**
	 * Creates a new Schedule. <code>stations</code>, </code>arrivalTimes</code>
	 * and </code>departureTimes</code> must have the same lengths.
	 * 
	 * @param scheme
	 *            {@link ScheduleScheme} of this schedule. Must not be null
	 * @param stations
	 *            Array of {@link Node} instances, the train stops at.Must not
	 *            be null
	 * @param arrivalTimes
	 *            Arrival time at each stop in milliseconds after the begin of
	 *            the simulation. Must not be null
	 * @param departureTimes
	 *            Arrival time at each stop in milliseconds after the begin of
	 *            the simulation. Must not be null
	 */
	public Schedule(ScheduleScheme scheme, Node[] stations) {
		if (scheme == null || stations == null) {
			throw new IllegalArgumentException("parameters must not be null");
		}

		this.scheme = scheme;
		this.stations = stations;
		this.arrivalTimes = new HashMap<>();
		this.idleTimes = new HashMap<>();
	}



	/**
	 * @return the scheme
	 */
	public ScheduleScheme getScheme() {
		return scheme;
	}

	/**
	 * @return the stations
	 */
	public Node[] getStations() {
		return stations;
	}

	/**
	 * Sets the idle time for the specified station.
	 * 
	 * @param station
	 *            {@link Node} where to wait
	 * @param idleTime
	 *            Time to wait in milliseconds
	 */
	public void addIdleTime(Node station, int idleTime) {
		idleTimes.put(station, idleTime);
	}
	
	/**
	 * Sets the arrival time for the specified station.
	 * 
	 * @param station
	 *            {@link Node} to reach
	 * @param idleTime
	 *            arrival time at <code>station</code> in milliseconds after the
	 *            start of the simulation
	 */
	public void addArrivalTime(Node station, int idleTime) {
		arrivalTimes.put(station, idleTime);
	}
	
	/**
	 * @return The idle time at the specified station in milliseconds
	 */
	public int getIdleTime(Node station) {
		return idleTimes.get(station);
	}
	
	/**
	 * @return The arrival time at the specified station in milliseconds
	 */
	public int getArrivalTime(Node station) {
		return arrivalTimes.get(station);
	}

}
