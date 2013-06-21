package de.atlassoft.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

//TODO: Klasse testen
/**
 * This class is an abstraction of the <code>Schedule</code> class. An
 * <code>ScheduleScheme</code> determines the train type, the stations and the
 * relative arrival times and some other properties. Out of one
 * <code>ScheduleScheme</code> can be many concrete <code>Schedules</code>
 * created, depending on the repetition type (<code>ScheduleType</code>) of this
 * scheme.
 * 
 * @author Alexander Balogh
 * 
 */
public class ScheduleScheme {

	private String railSysID;
	
	/**
	 * The train type of this schedule. Must not be null.
	 */
	private TrainType trainType;

	/**
	 * The schedule type of this schedule. Must not be null.
	 */
	private ScheduleType scheduleType;

	/**
	 * List of all stations to be visited. The nodes will be visited in the
	 * order of the list. Must not be null but may be empty. Must have the same
	 * size as arrival.
	 */
	private List<Node> stations;

	/**
	 * Specifies the time of arrival for each node in stations in seconds after
	 * the start at the first station. Therefore the first entry should be 0 and
	 * the nth entry should be arrival.get(n-1) + idleTime.get(n-1) + x where x
	 * is the time in seconds the train holds to drive from station n-1 to
	 * station n. The entry at the position i in entry belongs to the node at
	 * the position i in stations. Must not be null but may be empty. Must have
	 * the same size as stations.
	 */
	private List<Long> arrivalTimes;

	/**
	 * Specifies the idle time for each station in seconds where the element at
	 * position i belongs to the station stations.get(i).
	 */
	private List<Long> idleTimes;

	/**
	 * Specifies the start of the first ride. Only the HOUR and
	 * MINUTE property contain meaningful information.
	 */
	private Calendar firstRide;

	/**
	 * Specifies the start of the last ride. Only the HOUR and
	 * MINUTE property contain meaningful information.
	 */
	private Calendar lastRide;

	/**
	 * Specifies the time period, between the start of one train and the next in
	 * minutes. Value of this property matters only if scheduleType equals
	 * ScheduleType.INTERVAL.
	 */
	private int interval;

	/**
	 * Specifies the days when this schedule is active. Contains the constants
	 * of the Calendar class (Calendar.MONDAY, ...). Must not be null but can be
	 * empty.
	 */
	private List<Integer> days;

	/**
	 * Creates a new instance of this class. Throws an IllegalArgumentException
	 * if at least one parameter is null or invalid.
	 * 
	 * @param scheduleType
	 *            Schedule type of this <code>ScheduleScheme</code>
	 * @param trainType
	 *            Train type of the trains using this
	 *            <code>ScheduleScheme</code>
	 * @param days
	 *            Days when this <code>ScheduleScheme</code> is active. Must
	 *            contain only the java.util.Calendar DAY_OF_THE_WEEK constants.
	 * @param firstRide
	 *            Specifies the start of the first ride every day. Only the
	 *            DAY_OF_THE_WEEK, HOUR and MINUTE property the given Calendar
	 *            object matters
	 * @param railSysID
	 *            ID of the {@link RailwaySystem} this object belongs to. Must
	 *            not be null or empty
	 * @throws IllegalArgumentException
	 *             if at least one parameter is null or days contains illegal
	 *             entries
	 */
	public ScheduleScheme(ScheduleType scheduleType, TrainType trainType,
			List<Integer> days, Calendar firstRide, String railSysID) {
		if (scheduleType == null) {
			throw new IllegalArgumentException("scheduleType must not be null");
		}
		if (trainType == null) {
			throw new IllegalArgumentException("trainType must not be null");
		}
		if (days == null) {
			throw new IllegalArgumentException("days must not be null");
		}
		if (firstRide == null) {
			throw new IllegalArgumentException("firstRide must not be null");
		}
		if (railSysID == null || railSysID.isEmpty()) {
			throw new IllegalArgumentException("railSysID must not be null or empty");
		}
		for (int d : days) {
			if (d != Calendar.MONDAY && d != Calendar.TUESDAY
					&& d != Calendar.WEDNESDAY && d != Calendar.THURSDAY
					&& d != Calendar.FRIDAY && d != Calendar.SATURDAY
					&& d != Calendar.SUNDAY) {
				throw new IllegalArgumentException(
						"days must onlay contain Calendar.DAY_OF_THE_WEEK constants");
			}
		}

		this.scheduleType = scheduleType;
		this.trainType = trainType;
		this.days = days;
		this.firstRide = firstRide;
		this.railSysID = railSysID;

		stations = new ArrayList<Node>();
		arrivalTimes = new ArrayList<Long>();
		idleTimes = new ArrayList<Long>();
	}

	/**
	 * Adds a new stop to this schedule scheme. arrivalTime of the first stop
	 * must be 0. The arrival time for every other stop must be later than the
	 * arrival time at the last top + the idle time at the last stop meaning
	 * there had to be a negative traveling time in order to reach this stop in
	 * time.
	 * 
	 * @param station
	 *            of this stop, must not be null
	 * @param arrival
	 *            point in time when to arrive at station in seconds after the
	 *            start at the first station
	 * @param idleTime
	 *            time the train should wait at the station.
	 * @throws IllegalArgumentException
	 *             if station is null</br> if idleTime is a negative number</br>
	 *             if arrival is too early.
	 */
	public void addStop(Node station, long arrival, long idleTime) {
		// check constraints
		if (station == null) {
			throw new IllegalArgumentException("station must not be null");
		}
		if (idleTime < 0) {
			throw new IllegalArgumentException("idleTime must not be negative");
		}
		// special constraints if first stop is added
		if (arrivalTimes.size() == 0) {
			if (arrival != 0) {
				throw new IllegalArgumentException(
						"arrival must be 0 at the first stop");
			}
		}
		// special constraints if added stop is not the first
		else {
			if (arrival < (arrivalTimes.get(arrivalTimes.size() - 1) + idleTimes
					.get(arrivalTimes.size() - 1))) {
				throw new IllegalArgumentException(
						"arrival to early to be possible");
			}
		}

		// set values
		stations.add(station);
		arrivalTimes.add(arrival);
		idleTimes.add(idleTime);
	}

	/**
	 * Sets the lastRide property. lastRide specifies the start of the last ride
	 * every day. Only the DAY_OF_THE_WEEK, HOUR and MINUTE property the given
	 * Calendar object matters. If lastRide is null, an
	 * <code>IllegalArgumentException</code> is thrown.
	 * 
	 * @param lastRide
	 *            last ride to be set
	 * @throws IllegalArgumentException
	 *             if lastRide is null
	 */
	public void setLastRide(Calendar lastRide) {
		if (lastRide == null) {
			throw new IllegalArgumentException("lastRide must not be null");
		}
		this.lastRide = lastRide;
	}

	/**
	 * Sets the interval property of this object. If interval is a negative
	 * number or zero an <code>IllegalArgumentExcetion</code> is thrown.
	 * 
	 * @param interval
	 *            interval to be set
	 * @throws IllegalArgumentException
	 *             if interval is negative or zero
	 */
	public void setInterval(int interval) {
		if (interval <= 0) {
			throw new IllegalArgumentException(
					"intervall must be a positive number");
		}
		this.interval = interval;
	}

	/**
	 * Returns the trainType property.
	 * 
	 * @return the train type
	 */
	public TrainType getTrainType() {
		return trainType;
	}

	/**
	 * Returns the scheduleType property.
	 * 
	 * @return the schedule type
	 */
	public ScheduleType getScheduleType() {
		return scheduleType;
	}

	/**
	 * Returns a list containing all stations to be approached. The nodes should
	 * be visited in the order of the list.
	 * 
	 * @return list of nodes
	 */
	public List<Node> getStations() {
		return Collections.unmodifiableList(stations);
	}

	/**
	 * Returns a list containing the arrival time for every station in in
	 * seconds after the start at the first. Therefore the first entry is 0 and
	 * the nth entry is the value of the (n-1)th entry + idle time at station
	 * n-1 + the time in seconds the train holds to drive from station n-1 to
	 * station n. The entry at the position i in entry belongs to the node at
	 * the position i in station property.
	 * 
	 * @return list of arrival times
	 */
	public List<Long> getArrivalTimes() {
		return Collections.unmodifiableList(arrivalTimes);
	}

	/**
	 * Returns a list containing the idle time for every station in seconds. The
	 * element at position i belongs to the ith element of the station property.
	 * 
	 * @return list of idle times
	 */
	public List<Long> getIdleTimes() {
		return Collections.unmodifiableList(idleTimes);
	}

	/**
	 * Returns the firstRide property. firstRide specifies the start of the
	 * first ride every day. Only the DAY_OF_THE_WEEK, HOUR and MINUTE property
	 * of the returned Calendar object contain meaningful information.
	 * 
	 * @return the first ride
	 */
	public Calendar getFirstRide() {
		return firstRide;
	}

	/**
	 * Returns the lastRide property. lastRide specifies the start of the last
	 * ride every day. Only the DAY_OF_THE_WEEK, HOUR and MINUTE property of the
	 * returned Calendar object contain meaningful information.
	 * 
	 * @return the last ride
	 */
	public Calendar getLastRide() {
		return lastRide;
	}

	/**
	 * Returns the interval in which new trains start in minutes. Value of this
	 * property is only meaningful if scheduleType equals ScheduleType.INTERVAL.
	 * 
	 * @return the interval
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * Returns a list containing all days for which this schedules is active.
	 * Contains the java.util.Calendar DAY_OF_THE_WEEK constants.
	 * 
	 * @return list of days.
	 */
	public List<Integer> getDays() {
		return Collections.unmodifiableList(days);
	}
	
	/**
	 * Returns the ID of the {@link RailwaySystem} this objects belongs to.
	 * 
	 * @return the id
	 */
	public String getRailSysID() {
		return railSysID;
	}

}
