package de.atlassoft.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * Implementation of the {@link ModelService} interface, providing access to the
 * data model layer of this application.
 * 
 * @author Alexander Balogh
 * 
 */
public class ModelServiceImpl implements ModelService {

	/**
	 * Provides methods to generate and distribute {@link PropertyChangeEvent
	 * PropertyChangeEvents}.
	 */
	private PropertyChangeSupport pcSupport;
	
	/**
	 * The active {@link RailwaySystem}.
	 */
	private RailwaySystem activeRailSys;
	
	/**
	 * List containing the id of every {@link RailwaySystem}.
	 */
	private List<String> railSysIDs;
	
	/**
	 * List of all {@link TrainType TrainTypes}.
	 */
	private List<TrainType> trainTypes;
	
	/**
	 * List of all active {@link ScheduleScheme ScheduleSchemes} that belong to
	 * {@link ModelServiceImpl#activeRailSys} i.e. the schemes that will
	 * participate in the simulation.
	 */
	private List<ScheduleScheme> activeSchemes;
	
	/**
	 * List of all passive {@link ScheduleScheme ScheduleSchemes} that belong to
	 * {@link ModelServiceImpl#activeRailSys} i.e. the schemes that will not
	 * participate in the simulation.
	 */
	private List<ScheduleScheme> passiveSchemes;



	/**
	 * Create a new instance of this class.
	 */
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
		activeSchemes.clear();
		passiveSchemes.clear();
		pcSupport.firePropertyChange(ACTIVE_RAILSYS_PROPNAME, null, activeRailSys);
		pcSupport.firePropertyChange(ACTIVE_SCHEDULE_SCHEMES_PROPNAME, null, activeSchemes);
		pcSupport.firePropertyChange(PASSIVE_SCHEDULE_SCHEMES_PROPNAME, null, passiveSchemes);
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
		
		Iterator<ScheduleScheme> it = activeSchemes.iterator();
		while (it.hasNext()) {
			if (it.next().getTrainType().equals(type)) {
				it.remove();
			}
		}
		
		it = passiveSchemes.iterator();
		while (it.hasNext()) {
			if (it.next().getTrainType().equals(type)) {
				it.remove();
			}
		}
		
		pcSupport.firePropertyChange(TRAIN_TYPES_PROPNAME, null, getTrainTypes());
		pcSupport.firePropertyChange(PASSIVE_SCHEDULE_SCHEMES_PROPNAME, null, getPassiveScheduleSchemes());
		pcSupport.firePropertyChange(ACTIVE_SCHEDULE_SCHEMES_PROPNAME, null, getActiveScheduleSchemes());
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

}
