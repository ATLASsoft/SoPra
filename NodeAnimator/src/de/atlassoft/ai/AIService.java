package de.atlassoft.ai;

import java.util.Calendar;
import java.util.List;
import java.util.Observer;

import de.atlassoft.model.Node;
import de.atlassoft.model.RailwaySystem;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.SimulationStatistic;

/**
 * Provides access to the AI layer and the graph processing algorithms of this
 * application.
 * 
 * @author Alexander Balogh
 * 
 */
public interface AIService {

	/**
	 * Starts a new simulation run at the passed start time (simulation time).
	 * Only the {@link Calendar#DAY_OF_WEEK}, {@link Calendar#HOUR_OF_DAY} and
	 * {@link Calendar#MINUTE} property of start are considered. If there is
	 * already an ongoing simulation no action is taken and no exception is
	 * thrown.
	 * 
	 * @param start
	 *            Simulation time the simulation should start at
	 * @param railSys
	 *            The {@link RailwaySystem} the the simulation takes place on
	 * @param schemes
	 *            The {@link ScheduleScheme ScheduleSchemes} that will
	 *            participate in the simulation
	 * @param o
	 *            {@link Observer} that is interested in updates of simulation
	 *            time
	 * @param resetData
	 *            Set true if all data that has been collected during previous
	 *            simulations should be reseted
	 */
	void startSimulation(
			Calendar start,
			RailwaySystem railSys,
			List<ScheduleScheme> schemes,
			Observer o,
			boolean resetData);
	
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
	 * Stops and ultimately terminates an ongoing simulation. If there is no
	 * ongoing simulation no action is taken, no exception is thrown and null is
	 * returned.
	 * 
	 * @return The {@link SimulationStatistic} of the finished simulation
	 */
	SimulationStatistic finishSimulation();
	
	/**
	 * Checks whether the specified {@link RailwaySystem} is connected.
	 * 
	 * @param railSys The {@link RailwaySystem} to check
	 * 
	 * @return ture if <code>railSys</code> is connected, otherwise false.
	 */
	boolean isConnected(RailwaySystem railSys);
	
	/**
	 * Computes the quickest possible travel time from start to goal i.e. going along the
	 * shortest route without being detained and no stops at any nodes.
	 * @param start
	 * @param goal
	 * @param topSpeed
	 * @return
	 */
	int fastestArrival(RailwaySystem railSys, Node start, Node goal, double topSpeed);
	
	/**
	 * Returns true if there is currently an ongoing simulation run.
	 * 
	 * @return True if there is an ongoing simulation, otherwise false
	 */
	boolean isRunning();
	
	/**
	 * Set the time lapse factor for the ongoing simulation. If there is no ongoing simulation,
	 * no action is taken and no exception is thrown.
	 * 
	 * @param timeLapse The time Lapse factor to set
	 */
	void setTimeLapse(int timeLapse);
	
}
