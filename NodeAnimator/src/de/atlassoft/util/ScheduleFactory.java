package de.atlassoft.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
	private static boolean isBefore(Calendar cal, Calendar other) {
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
	private static boolean isAfter(Calendar cal, Calendar other) {
		return !isBefore(cal, other);
	}

	
	
	
}


