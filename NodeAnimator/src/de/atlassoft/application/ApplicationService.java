package de.atlassoft.application;

import java.util.Calendar;
import java.util.Observer;

import de.atlassoft.model.ModelService;
import de.atlassoft.model.Node;
import de.atlassoft.model.RailwaySystem;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.SimulationStatistic;
import de.atlassoft.model.TrainType;

//TODO: Interface auskommentieren
/**
 * Interface providing access to the application service layer (highest
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
	void startSimulation(Calendar time, Observer o);
	
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
	
	/**
	 * Terminates an ongoing simulation run. If there is no simulation run at
	 * the moment, no action is taken and no exception thrown. After terminating
	 * the simulation this method returns the {@link SimulationStatistic}
	 * associated with the terminated simulation. If there was no ongoing
	 * simulation, null will be returned.
	 * 
	 * @return {@link SimulationStatistic} of the terminated simulation or null
	 */
	SimulationStatistic quitSimulation();
	
	/**
	 * Sets the time lapse factor for the ongoing simulation. Thereby, factor n
	 * means one time unit (i.e. second) in real time equates n time units in
	 * simulation time. <code>timeLapse</code> must be an integer between 1 and
	 * 3000 (inclusive), otherwise an {@link IllegalArgumentException} is
	 * thrown. If there is no simulation run at the moment, no action is taken
	 * and no exception is thrown unless timeLapse does not match the previously
	 * mentioned constraints.
	 * 
	 * @param timeLapse
	 *            Time lapse factor to be set
	 * @throws IllegalArgumentException
	 *             if <code>timeLapse</code> is less than 1 or more than 3000
	 */
	void setTimeLapse(int timeLapse);
	
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
	
	/**
	 * Checks whether the specified {@link RailwaySystem} is connected.
	 * 
	 * @param railSys
	 *            {@link RailwaySystem} to check
	 * @return true if <code>railSys</code> is connected, otherwise false
	 */
	boolean isConnected(RailwaySystem railSys);
	
	/**
	 * Computes the quickest possible travel time for an agent with the
	 * specified top speed from start to goal i.e. going along the shortest route
	 * without being detained and no stops at any nodes.
	 * 
	 * @param railSys
	 *            {@link RailwaySystem} start and goal belong to
	 * @param start
	 *            The start {@link Node}
	 * @param goal
	 *            The goal {@link Node}
	 * @param topSpeed
	 *            The top speed of the agent
	 * @return fastest travel time in minutes
	 */
	public int getFastestTravelTime(RailwaySystem railSys, Node start, Node goal, double topSpeed);

	}
