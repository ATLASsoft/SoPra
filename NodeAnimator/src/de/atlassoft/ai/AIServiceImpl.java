package de.atlassoft.ai;

import java.util.Calendar;
import java.util.List;
import java.util.Observer;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.model.Node;
import de.atlassoft.model.RailwaySystem;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.SimulationStatistic;

/**
 * Implements the {@link ApplicationService} interface. Provides access to the
 * AI layer and the graph processing algorithms of this application.
 * 
 * @author Alexander Balogh
 * 
 */
public class AIServiceImpl implements AIService {

	private SimulationLoop loop;
	private SimulationStatistic statistic;
	
	/**
	 * Indicates if there is an ongoing simulation.
	 */
	private boolean running = false;
	
	public AIServiceImpl(ApplicationService app) {
	}
	
	@Override
	public void startSimulation(
			Calendar start,
			RailwaySystem railSys,
			List<ScheduleScheme> schemes,
			Observer o,
			boolean resetData) {
		
		if (!running) {
			railSys.clear(resetData);
			if (resetData) {
				statistic = new SimulationStatistic(railSys);
			}
			loop = new SimulationLoop(new Graph(railSys), this);
			loop.addObserver(o);
			loop.startRun(start, schemes);
			running = true;
		}
	}

	@Override
	public void pauseSimulation() {
		if (running) {
			loop.stopRun();
		}
	}

	@Override
	public void continueSimulation() {
		if (running) {
			loop.continueRun();
		}
	}

	@Override
	public SimulationStatistic finishSimulation() {
		if (running) {
			loop.stopRun();
			loop.shutDown();
			running = false;
			return statistic;
		}
		return null;
	}

	@Override
	public int fastestArrival(RailwaySystem railSys, Node start, Node goal,
			double topSpeed) {
		double d = new Graph(railSys).getShortestTravelTime(start, goal, topSpeed);
		return ((int) (d * 60.0)) + 1;  // convert from hours to minute and round up
	}

	@Override
	public boolean isConnected(RailwaySystem railSys) {
		return new Graph(railSys).isConnected();
	}

	@Override
	public boolean isRunning() {
		return running;
	}
	
	@Override
	public void setTimeLapse(int timeLapse) {
		if (running) {
			loop.setTimeLapse(timeLapse);
		}
	}
	
	
	
	/**
	 * @return The {@link SimulationLoop} of the ongoing simulation or null if
	 *         there is no ongoing simulation
	 */
	protected SimulationLoop getLoop() {
		return loop;
	}
	
	/**
	 * @return The {@link SimulationStatistic} of the ongoing simulation.
	 */
	protected SimulationStatistic getSimStat() {
		return statistic;
	}
}
