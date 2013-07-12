package de.atlassoft.util;

import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.PriorityQueue;

import de.atlassoft.model.Node;
import de.atlassoft.model.Schedule;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.ScheduleType;

public abstract class ScheduleFactory {

	public static PriorityQueue<Schedule> createScheduleQueue(Calendar startOfSimulation, Calendar start, List<ScheduleScheme> schemes) {
		PriorityQueue<Schedule> queue = new PriorityQueue<>(1, new Comparator<Schedule>() {
			@Override
			public int compare(Schedule o1, Schedule o2) {
				return o1.getArrivalTime(o1.getStations()[0])
						- o2.getArrivalTime(o2.getStations()[0]);
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
					lastRide = new GregorianCalendar();
					lastRide.clear();
					lastRide.set(Calendar.DAY_OF_WEEK, day);
					lastRide.set(Calendar.HOUR_OF_DAY, scheme.getLastRide().get(Calendar.HOUR_OF_DAY));
					lastRide.set(Calendar.MINUTE, scheme.getLastRide().get(Calendar.MINUTE));
					lastRide.set(Calendar.SECOND, 0);
					lastRide.set(Calendar.MILLISECOND, 0);
					while (isBefore(cal, lastRide)) {
						queue.offer(createSchedule(scheme, cal, startOfSimulation));
						cal.add(Calendar.MINUTE, scheme.getInterval());
					}
				} else {
					cal = (Calendar) begin.clone();
					cal.set(Calendar.DAY_OF_WEEK, day);
					cal.set(Calendar.HOUR_OF_DAY, scheme.getFirstRide().get(Calendar.HOUR_OF_DAY));
					cal.set(Calendar.MINUTE, scheme.getFirstRide().get(Calendar.MINUTE));
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					if (isAfter(cal, begin)) {
						queue.offer(createSchedule(scheme, cal, startOfSimulation));
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
		Calendar cal = (Calendar) begin.clone();
		cal.set(Calendar.DAY_OF_WEEK, begin.get(Calendar.DAY_OF_WEEK));
		cal.set(Calendar.HOUR_OF_DAY, scheme.getFirstRide().get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, scheme.getFirstRide().get(Calendar.MINUTE));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		int interval = scheme.getInterval();
		
		while (isBefore(cal, begin)) {
			cal.add(Calendar.MINUTE, interval);
		}
		return cal;
	}
	
	private static Schedule createSchedule(ScheduleScheme scheme, Calendar departureTime, Calendar simStart) {
		int normalizedDepartureTime = (int) (departureTime.getTimeInMillis() - simStart.getTimeInMillis());
		
		List<Node> stationList = scheme.getStations();
		Node[] stations = stationList.toArray(new Node[0]);
		
		Schedule schedule = new Schedule(scheme, stations);
		
		List<Integer> idleList = scheme.getIdleTimes();
		List<Integer> arrivalList = scheme.getArrivalTimes();
		for (int i = 0; i < stations.length; i++) {
			schedule.addIdleTime(stations[i], idleList.get(i) * 1000);
			
			schedule.addArrivalTime(stations[i],
					normalizedDepartureTime + (arrivalList.get(i) * 1000));
		}
		
		return schedule;
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


