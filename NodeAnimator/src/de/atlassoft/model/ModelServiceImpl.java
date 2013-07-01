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
	private List<ScheduleScheme> activeSchemes;
	private List<ScheduleScheme> passiveSchemes;
	private SimulationStatistic simulationStatistic;



	public ModelServiceImpl() {
		pcSupport = new PropertyChangeSupport(this);
		railSysIDs = new ArrayList<String>();
		trainTypes = new ArrayList<TrainType>();
		activeSchemes = new ArrayList<ScheduleScheme>();
		passiveSchemes = new ArrayList<ScheduleScheme>();
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
	public void setActiveRailwaySys(RailwaySystem railSys) {
		activeRailSys = railSys;
		pcSupport.firePropertyChange(ACTIVE_RAILSYS_PROPNAME, null, activeRailSys);
	}

	@Override
	public RailwaySystem getActiveRailwaySys() {
		return activeRailSys;
	}
	
	@Override
	public void addRailwaySystemID(String id) {
		if (id == null || id.trim().isEmpty()) {
			throw new IllegalArgumentException("id must not be null or empty");
		}
		if (railSysIDs.contains(id)) {
			throw new IllegalArgumentException("id has already been added");
		}
		railSysIDs.add(id);
		pcSupport.firePropertyChange(RAILSYS_IDS_PROPNAME, null, getRailwaySystemIDs());
	}
	
	@Override
	public void removeRailwaySystemID(String id) {
		railSysIDs.remove(id);
		pcSupport.firePropertyChange(RAILSYS_IDS_PROPNAME, null, getRailwaySystemIDs());
	}

	@Override
	public List<String> getRailwaySystemIDs() {
		return Collections.unmodifiableList(railSysIDs);
	}

	@Override
	public void addTrainType(TrainType type) {
		if (type != null && !trainTypes.contains(type)) {
			trainTypes.add(type);
			pcSupport.firePropertyChange(TRAIN_TYPES_PROPNAME, null, getTrainTypes());
		}

	}

	@Override
	public void deleteTrainType(TrainType type) {
		trainTypes.remove(type);
		pcSupport.firePropertyChange(TRAIN_TYPES_PROPNAME, null, getTrainTypes());
	}

	@Override
	public List<TrainType> getTrainTypes() {
		return Collections.unmodifiableList(trainTypes);
	}

	@Override
	public void addActiveScheduleScheme(ScheduleScheme schedule) {
		if (schedule != null && !activeSchemes.contains(schedule)) {
			activeSchemes.add(schedule);
			pcSupport.firePropertyChange(ACTIVE_SCHEDULE_SCHEMES_PROPNAME, null, getActiveScheduleSchemes());
		}
	}

	@Override
	public void removeActiveScheduleScheme(ScheduleScheme schedule) {
		activeSchemes.remove(schedule);
		pcSupport.firePropertyChange(ACTIVE_SCHEDULE_SCHEMES_PROPNAME, null, getActiveScheduleSchemes());
	}

	@Override
	public List<ScheduleScheme> getActiveScheduleSchemes() {
		return Collections.unmodifiableList(activeSchemes);
	}
	
	@Override
	public void addPassiveScheduleScheme(ScheduleScheme schedule) {
		if (schedule != null && !passiveSchemes.contains(schedule)) {
			passiveSchemes.add(schedule);
			pcSupport.firePropertyChange(PASSIVE_SCHEDULE_SCHEMES_PROPNAME, null, getPassiveScheduleSchemes());
		}
	}

	@Override
	public void removePassiveScheduleScheme(ScheduleScheme schedule) {
		passiveSchemes.remove(schedule);
		pcSupport.firePropertyChange(PASSIVE_SCHEDULE_SCHEMES_PROPNAME, null, getPassiveScheduleSchemes());
	}

	@Override
	public List<ScheduleScheme> getPassiveScheduleSchemes() {
		return Collections.unmodifiableList(passiveSchemes);
	}

	@Override
	public SimulationStatistic getStatistic() {
		// TODO: noch unklar
		return simulationStatistic;
	}

}
