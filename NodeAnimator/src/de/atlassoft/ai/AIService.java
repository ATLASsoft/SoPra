package de.atlassoft.ai;

import java.util.Calendar;

import de.atlassoft.model.Node;
import de.atlassoft.model.RailwaySystem;
//TODO: unvollständig
public interface AIService {

	void startSimulation(Calendar start);
	void pauseSimulation();
	void continueSimulation();
	void stopSimulation();
	
	/**
	 * Computes the quickest possible travel time from start to goal i.e. going along the
	 * shortest route without being detained and no stops at any nodes.
	 * @param start
	 * @param goal
	 * @param topSpeed
	 * @return
	 */
	long fastestArrival(RailwaySystem railSys, Node start, Node goal, double topSpeed);
	
}
