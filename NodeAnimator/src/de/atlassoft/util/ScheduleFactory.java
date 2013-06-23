package de.atlassoft.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.atlassoft.model.Schedule;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.ScheduleType;
import de.atlassoft.model.TrainType;

public abstract class ScheduleFactory {

	public static List<Schedule> createSchedules(
			List<ScheduleScheme> schemes,
			Calendar begin,
			Calendar end) {
		Calendar departureTime;
		int interval;
		List<Schedule> schedules = new ArrayList<>();
		for (ScheduleScheme scheme : schemes) {
			departureTime = (Calendar) scheme.getFirstRide().clone();
			if (scheme.getScheduleType().equals(ScheduleType.INTERVALL)) {
				interval = scheme.getInterval();
				while (departureTime.before(end)) {
					if (departureTime.after(begin)) {
						//TODO: schedule erzeugen
					}
					departureTime.add(Calendar.MINUTE, interval);
				}
			} else if (scheme.getScheduleType().equals(ScheduleType.SINGLE_RIDE)) {
				if (departureTime.after(begin) && departureTime.before(end)) {
					//TODO: schedule erzeugen
				}
			}
			
		}
		
		return schedules;
	}



	
}


