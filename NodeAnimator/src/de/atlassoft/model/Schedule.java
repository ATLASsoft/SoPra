package de.atlassoft.model;

import java.util.Calendar;
//TODO: besser kommentieren
public class Schedule {

	private ScheduleScheme scheme;
	private Node[] stations;
	private Calendar[] arrivalTimes;
	private int[] idleTimes;



	/**
	 * Creates a new Schedule. Stations, arrivalTimes and idleTimes must have
	 * the same lengths.
	 * 
	 * @param scheme
	 *            {@link ScheduleScheme} of this schedule. Must not be null
	 * @param stations
	 *            {@link Node} the train stops at. . Must not be null
	 * @param arrivalTimes
	 *            Arrival time at each stop. Must not be null
	 * @param idleTimes
	 *            Idle time at each stop. Must not be null
	 */
	public Schedule(ScheduleScheme scheme, Node[] stations,
			Calendar[] arrivalTimes, int[] idleTimes) {
		if (scheme == null || stations == null || arrivalTimes == null
				|| idleTimes == null) {
			throw new IllegalArgumentException("parameters must not be null");
		}
		if (stations.length != arrivalTimes.length
				|| stations.length != idleTimes.length) {
			throw new IllegalArgumentException(
					"arrays must have the same length");
		}

		this.scheme = scheme;
		this.stations = stations;
		this.arrivalTimes = arrivalTimes;
		this.idleTimes = idleTimes;
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
	 * @return the arrivalTimes
	 */
	public Calendar[] getArrivalTimes() {
		return arrivalTimes;
	}

	/**
	 * @return the idleTimes
	 */
	public int[] getIdleTimes() {
		return idleTimes;
	}

}
