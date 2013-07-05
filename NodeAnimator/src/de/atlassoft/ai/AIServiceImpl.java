package de.atlassoft.ai;

import java.util.Calendar;
import java.util.List;

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
	
	@Override
	public void startSimulation(Calendar start, RailwaySystem railSys, List<ScheduleScheme> schemes) {
		if (!running) {
			//simulation starten
			loop = new SimulationLoop(new Graph(railSys));
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
			loop.stopRun(); //TODO: vervollständigen
			running = false;
		}
		return null;
	}

	@Override
	public long fastestArrival(RailwaySystem railSys, Node start, Node goal,
			double topSpeed) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isConnected(RailwaySystem railSys) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}
	
	
}
