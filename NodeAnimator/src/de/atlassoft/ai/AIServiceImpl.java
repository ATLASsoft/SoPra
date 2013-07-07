package de.atlassoft.ai;

import java.util.Calendar;
import java.util.List;
import java.util.Observer;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.model.Node;
import de.atlassoft.model.RailwaySystem;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.SimulationStatistic;
//TODO: implementieren
/**
 * Implements the {@link ApplicationService} interface. Provides access to the
 * AI layer and the graph processing algorithms of this application.
 * 
 * @author Alexander Balogh
 * 
 */
public class AIServiceImpl implements AIService {

	private SimulationLoop loop;
	
	/**
	 * Indicates if there is an ongoing simulation.
	 */
	private boolean running = false;
	private ApplicationService application;
	
	public AIServiceImpl(ApplicationService app) {
		application = app;
	}
	
	
	@Override
	public void startSimulation(Calendar start, RailwaySystem railSys, List<ScheduleScheme> schemes, Observer o) {
		if (!running) {
			loop = new SimulationLoop(new Graph(railSys));
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
			loop.shutDown(); //TODO: statistik
			running = false;
		}
		return null;
	}

	@Override
	public int fastestArrival(RailwaySystem railSys, Node start, Node goal,
			double topSpeed) {
		double d = new Graph(railSys).getDistance(start, goal, topSpeed);
		return ((int) (d * 100_000.0 / 60.0)) + 1;
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
	
}
