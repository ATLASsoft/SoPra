package de.atlassoft.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Implementation of the {@link ModelService} interface, providing access to the
 * data model layer of this application.
 * 
 * @author Alexander Balogh
 * 
 */
public class ModelServiceImpl implements ModelService {

	private PropertyChangeSupport pcSupport;
	private RailwaySystem activeRailSys;
	private List<String> railSysIDs;
	private List<TrainType> trainTypes;
	private List<ScheduleScheme> scheduleSchemes;

	public ModelServiceImpl() {
		pcSupport = new PropertyChangeSupport(this);
		railSysIDs = new ArrayList<String>();
		trainTypes = new ArrayList<TrainType>();
		scheduleSchemes = new ArrayList<ScheduleScheme>();
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcSupport.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcSupport.removePropertyChangeListener(listener);
	}

	@Override
	public void firePropertyChangeEvent() {
		// TODO Methode noch unklar

	}

	@Override
	public void setActiveRailwaySys(RailwaySystem railSys) {
		activeRailSys = railSys;
	}

	@Override
	public RailwaySystem getActiveRailwaySys() {
		return activeRailSys;
	}

	@Override
	public List<String> getRailwaySystemIDs() {
		return Collections.unmodifiableList(railSysIDs); // TODO: railsys ids setzten
	}

	@Override
	public void addTrainType(TrainType type) {
		if (type != null && !trainTypes.contains(type)) {
			trainTypes.add(type);
		}

	}

	@Override
	public void deleteTrainType(TrainType type) {
		trainTypes.remove(type);
	}

	@Override
	public List<TrainType> getTrainTypes() {
		return Collections.unmodifiableList(trainTypes);
	}

	@Override
	public void addSchedule(ScheduleScheme schedule) {
		if (schedule != null && !scheduleSchemes.contains(schedule)) {
			scheduleSchemes.add(schedule);
		}
	}

	@Override
	public void deleteSchedule(ScheduleScheme schedule) {
		scheduleSchemes.remove(schedule);
	}

	@Override
	public List<ScheduleScheme> getSchedules() {
		return Collections.unmodifiableList(scheduleSchemes);
	}

	@Override
	public SimulationStatistic getStatistic() {
		// TODO: noch unklar
		return null;
	}

}
