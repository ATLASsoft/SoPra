package de.atlassoft.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import de.atlassoft.model.Node;
import de.atlassoft.model.Schedule;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.ScheduleType;

public abstract class ScheduleFactory {
	//TODO: wenn methode mehrmals in der selben sim minute aufgerufen wrid werden pläne mehrfach erstellt
	public static List<Schedule> createSchedules(
			List<ScheduleScheme> schemes,
			Calendar begin,
			Calendar end) {
		Calendar departureTime;
		int interval;
		List<Schedule> schedules = new ArrayList<>();
		for (ScheduleScheme scheme : schemes) {
			departureTime = (Calendar) scheme.getFirstRide().clone();
			if (scheme.getScheduleType().equals(ScheduleType.INTERVALL)
					&& departureTime.get(Calendar.DAY_OF_WEEK) >= begin.get(Calendar.DAY_OF_WEEK)) {
				interval = scheme.getInterval();
				while (isBefore(departureTime, end)) {
					if (isAfter(departureTime, begin)) {
						createSchedule(scheme, departureTime);
					}
					departureTime.add(Calendar.MINUTE, interval);
				}
			} else if (scheme.getScheduleType().equals(ScheduleType.SINGLE_RIDE)) {
				if (isAfter(departureTime, begin) && isBefore(departureTime, end)) {
					createSchedule(scheme, departureTime);
				}
			}
			
		}
		
		return schedules;
	}

	
	/**
	 * Returns a {@link PriorityQueue} containing all {@link Schedule}s that
	 * start after start at the same day.
	 * 
	 * @param schemes
	 *            Schemes to create schedules from
	 * @param start
	 *            Only schedules that start after start will be created
	 * @return Queue of schedules
	 */
	public static PriorityQueue<Schedule> createScheduleQueue(
			List<ScheduleScheme> schemes, Calendar start) {
		PriorityQueue<Schedule> queue = new PriorityQueue<>(1, new Comparator<Schedule>() {
			
			@Override
			public int compare(Schedule o1, Schedule o2) {
				boolean isBefore = isBefore(o1.getArrivalTimes()[0], o2.getArrivalTimes()[0]);
				if (isBefore) {
					return -1;
				} else {
					return +1;
				}
			}
		});
		
		
		Calendar begin = (Calendar) start.clone();
		Calendar cal;
		Calendar lastRide;
		int day = begin.get(Calendar.DAY_OF_WEEK);
		
		for (ScheduleScheme scheme : schemes) {
			if (scheme.getDays().contains(day)) {
				if (scheme.getScheduleType().equals(ScheduleType.INTERVALL)) {
					cal = setToFirstRide(scheme, begin);
					lastRide = (Calendar) scheme.getLastRide().clone();
					lastRide.set(Calendar.DAY_OF_WEEK, day);
					while (isBefore(cal, lastRide)) {
						queue.offer(createSchedule(scheme, cal));
						cal.add(Calendar.MINUTE, scheme.getInterval());
					}
				} else {
					cal = (Calendar) scheme.getFirstRide().clone();
					cal.set(Calendar.DAY_OF_WEEK, day);
					if (isAfter(cal, begin)) {
						queue.offer(createSchedule(scheme, cal));
					}
				}
			}
		}
		return queue;
	}

	/**
	 * Returns the departure time of the first ride of scheme after begin.
	 * @param scheme
	 * @param begin
	 * @return
	 */
	private static Calendar setToFirstRide(ScheduleScheme scheme, Calendar begin) {
		Calendar cal = (Calendar) scheme.getFirstRide().clone();
		cal.set(Calendar.DAY_OF_WEEK, begin.get(Calendar.DAY_OF_WEEK));
		int interval = scheme.getInterval();
		
		while (isBefore(cal, begin)) {
			cal.add(Calendar.MINUTE, interval);
		}
		return cal;
	}
	
	private static Schedule createSchedule(ScheduleScheme scheme, Calendar departureTime) {
		List<Node> stationList = scheme.getStations();
		Node[] stations = stationList.toArray(new Node[0]);
		
		List<Integer> arrivalList = scheme.getArrivalTimes();
		Calendar[] arrivalTimes = new Calendar[arrivalList.size()];
		Calendar cal;
		for (int i = 0; i < arrivalTimes.length; i++) {
			cal = (Calendar) departureTime.clone();
			cal.add(Calendar.SECOND, arrivalList.get(i));
			arrivalTimes[i] = cal;
		}
		
		List<Integer> idleList = scheme.getIdleTimes();
		int[] idleTimes = new int[idleList.size()];
		for (int i = 0; i < idleTimes.length; i++) {
			idleTimes[i] = idleList.get(i);
		}
		
		return new Schedule(scheme, stations, arrivalTimes, idleTimes);
	}
	
	/**
	 * Return true if cal is before other while only considering the properties
	 * {@link Calendar#DAY_OF_WEEK}, {@link Calendar#HOUR_OF_DAY} and
	 * {@link Calendar#MINUTE}.
	 * 
	 * @param cal
	 *            The first {@link Calendar}
	 * @param other
	 *            The other {@link Calendar}
	 * @return true if cal is before other, false if cal is after other or at
	 *         the same time
	 */
	public static boolean isBefore(Calendar cal, Calendar other) {
		int calDay = cal.get(Calendar.DAY_OF_WEEK);
		int otherDay = other.get(Calendar.DAY_OF_WEEK);
		if (calDay < otherDay) {
			return true;
		} else if (calDay > otherDay) {
			return false;
		}
		
		int calHour = cal.get(Calendar.HOUR_OF_DAY);
		int otherHour = other.get(Calendar.HOUR_OF_DAY);
		if (calHour < otherHour) {
			return true;
		} else if (calHour > otherHour) {
			return false;
		}
		
		int calMin = cal.get(Calendar.MINUTE);
		int otherMin = other.get(Calendar.MINUTE);
		if (calMin < otherMin) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Return true if cal is after other while only considering the properties
	 * {@link Calendar#DAY_OF_WEEK}, {@link Calendar#HOUR_OF_DAY} and
	 * {@link Calendar#MINUTE}.
	 * 
	 * @param cal
	 *            The first {@link Calendar}
	 * @param other
	 *            The other {@link Calendar}
	 * @return true if cal is after other or at the same time, false if cal is before
	 */
	public static boolean isAfter(Calendar cal, Calendar other) {
		return !isBefore(cal, other);
	}

	
	
	
}


