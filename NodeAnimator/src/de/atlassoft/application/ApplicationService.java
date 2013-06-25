package de.atlassoft.application;

import java.util.Calendar;

import de.atlassoft.model.ModelService;
import de.atlassoft.model.Node;
import de.atlassoft.model.RailwaySystem;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.SimulationStatistic;
import de.atlassoft.model.TrainType;

//TODO: Interface auskommentieren
/**
 * Interface provideing access to the application service layer (highest
 * abstraction) of this application.
 * 
 * @author Alexander Balogh
 * 
 */
public interface ApplicationService {

	/**
	 * Starts this application up. Only this method should be used to start the
	 * application. Loads resources, builds UI et cetera.
	 */
	void initialize();
	
	/**
	 * Shuts this application down. Only this method should be used to terminate
	 * this application.
	 */
	void shutDown();
	
	/**
	 * Returns the object giving access to the data model layer of this application.
	 * @return the data model
	 */
	ModelService getModel();
	
	/**
	 * Starts a new simulation run at the passed start time (simulation time).
	 * Only the {@link Calendar#DAY_OF_WEEK}, {@link Calendar#HOUR_OF_DAY} and
	 * {@link Calendar#MINUTE} property of time are considered. If there is
	 * already an ongoing simulation no action is taken and no exception is
	 * thrown.
	 * 
	 * @param time
	 *            Simulation time the simulation should start at
	 */
	void startSimulation(Calendar time);
	
	/**
	 * Pauses an ongoing simulation run. If there is no ongoing simulation, no
	 * action is taken and no exception is thrown.
	 */
	void pauseSimulation();
	
	/**
	 * Continues an ongoing but paused simulation run. If there is no ongoing
	 * simulation or the run is not paused, no action is taken and no exception
	 * is thrown.
	 */
	void continueSimulation();
	
	void quitSimulation();
	
	void addScheduleScheme(ScheduleScheme scheduleScheme);
	
	void deleteScheduleScheme(ScheduleScheme scheduleScheme);
	
	void saveRailwaySystem(RailwaySystem railSys);
	
	void deleteRailwaySystem(String railSysID);
	
	void setActiveRailwaySystem(String railsSysID);
	
	void addTrainType(TrainType trainType);
	
	void deleteTrainType(TrainType trainType);
	
	void showStatisticDoc(SimulationStatistic statistic);
	
	void showScheduleDoc(ScheduleScheme scheduleScheme);
	
	void showDepartureBoardDoc(Node station);
}
