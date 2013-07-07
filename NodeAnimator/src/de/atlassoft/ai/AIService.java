package de.atlassoft.ai;

import java.util.Calendar;
import java.util.List;
import java.util.Observer;

import de.atlassoft.model.Node;
import de.atlassoft.model.RailwaySystem;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.SimulationStatistic;
//TODO: unvollständig
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
	 */
	void startSimulation(Calendar start, RailwaySystem railSys, List<ScheduleScheme> schemes, Observer o);
	
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
	SimulationStatistic finishSimulation();
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
	
	boolean isRunning();
	
	void setTimeLapse(int timeLapse);
	
}
