package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.widgets.Display;

import de.atlassoft.model.Schedule;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.State;
import de.atlassoft.util.ScheduleFactory;

//TODO: unvollständig
/**
 * 
 * @author Alexander Balogh
 * 
 */
public class SimulationLoop extends Observable {

	private Calendar simStartTime;
	private int delta;
	private long last;
	private volatile boolean alive;
	private Calendar lastTime;
	private Calendar simTime;
	private List<ScheduleScheme> activeSchemes;
	private List<Schedule> readySchedules;
	private PriorityQueue<Schedule> schedules;
	protected List<TrainAgent> activeAgents;
	private Loop loop;
	private Graph graph;
	private int agentCounter;
	private ExecutorService executor;
	private Thread loopThread;
	private AIServiceImpl aiPort;

	/**
	 * Passed simulated time in ms since the start of the SimulationLoop
	 */
	private volatile long passedSimTime;

	/**
	 * Time lapse. For example timeLapse = 1000 means one second in real time
	 * equals 1000 seconds in simulated time.
	 */
	private volatile int timeLapse;
	
	
	
	/**
	 * Creates a new simulation loop.
	 */
	protected SimulationLoop(Graph graph, AIServiceImpl aiPort) {
		this.graph = graph;
		this.aiPort = aiPort;
		timeLapse = 1;
		activeAgents = Collections.synchronizedList(new ArrayList<TrainAgent>());
		readySchedules = new ArrayList<>();
		executor = Executors.newCachedThreadPool();
		loop = new Loop();
	}

	
	
	/**
	 * Starts a new run at specified time with passed schemes.
	 * 
	 * @param startTime
	 *            Simulation time the simulation should start at
	 * @param schemes
	 *            {@link ScheduleScheme}s that should participate in the
	 *            simulation
	 */
	protected void startRun(Calendar startTime, List<ScheduleScheme> schemes) {
		simStartTime = new GregorianCalendar();
		simStartTime.clear();
		simStartTime.set(Calendar.DAY_OF_WEEK, startTime.get(Calendar.DAY_OF_WEEK));
		simStartTime.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
		simStartTime.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
		simStartTime.set(Calendar.SECOND, 0);
		simStartTime.set(Calendar.MILLISECOND, 0);
		simTime = (Calendar) simStartTime.clone();
		lastTime = (Calendar) simStartTime.clone();
		
		
		readySchedules.clear();
		activeSchemes = schemes;
		schedules = ScheduleFactory
				.createScheduleQueue(simStartTime, simTime, schemes); // create schedules
		
		agentCounter = 1;
		alive = true;
		
		// init time management
		delta = 0;
		passedSimTime = 0;
		last = System.currentTimeMillis();

		loopThread = new Thread(loop);
		loopThread.start();
	}

	/**
	 * Continues the last run and all agents/animations.
	 */
	protected void continueRun() {
		// reset time management
		delta = 0;
		alive = true;
		last = System.currentTimeMillis();

		// start simulation loop
		loopThread = new Thread(loop);
		loopThread.start();
		
		// start trains
		synchronized (activeAgents) {
			Iterator<TrainAgent> it = activeAgents.iterator();
			while (it.hasNext()) {
				it.next().continueRide();
			}
		}
	}

	/**
	 * Stops an ongoing run and all agents/animations.
	 */
	protected void stopRun() {
		// stop simulation loop
		alive = false;
		try {
			loopThread.join();
		} catch (InterruptedException e) {
			System.out.println("unhandled interrupt @ stopRun");
		}
		System.out.println("loop stopped");
		// stop trains
		synchronized (activeAgents) {
			Iterator<TrainAgent> it = activeAgents.iterator();
			while (it.hasNext()) {
				it.next().pauseRide();
			}
		}
		
		
	}
	
	/**
	 * Sets the time lapse factor for this loop and all agents/animations.
	 * @param timeLapse The time lapse factor to be set
	 */
	protected void setTimeLapse(int timeLapse) {
		this.timeLapse = timeLapse;
		synchronized (activeAgents) {
			Iterator<TrainAgent> it = activeAgents.iterator();
			while (it.hasNext()) {
				it.next().setTimeLapse(timeLapse);
			}
		}
	}
	
	/**
	 * Shuts down the thread pool and all agents. This instance of
	 * {@link SimulationLoop} cannot be used again after the shutdown. In order
	 * to shut down properly one should first call
	 * {@link SimulationLoop#stopRun()}.
	 */
	protected void shutDown() {
		System.out.println("standard shutdown");
		executor.shutdown();
		synchronized (activeAgents) {
			Iterator<TrainAgent> it = activeAgents.iterator();
			while (it.hasNext()) {
				it.next().terminate();
			}
			activeAgents.clear();
		}
		try {
			if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
				System.out.println("forced shutdown");
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {  
			System.out.println("unhandled interrupt @ forced shutdown");
		}
		
	}

	/**
	 * Returns the current simulation time in milliseconds after the start
	 * of the simulation.
	 * 
	 * @return The simulation time
	 */
	protected long getSimTimeInMillis() {
		return passedSimTime;
	}
	
	/**
	 * Indicates whether the simulation loop is running at the moment.
	 * 
	 * @return true if running
	 */
	protected boolean isAlive() {
		return alive;
	}

	
	
	
	private class Loop implements Runnable {

		@Override
		public void run() {
			while (alive) {
				computeDelta();
//				System.out.println("delta: " + delta);
				updateSimTime();
				createNewTrains();

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					System.out.println("loop has been interrupted, terminates");
					alive = false;
				}
			}
		}

	}
	
	
	

	/**
	 * Computes delta since last loop pass in milliseconds. Sets
	 * {@link SimulationLoop#delta}.
	 */
	private void computeDelta() {
		delta = (int) (System.currentTimeMillis() - last);
		last = System.currentTimeMillis();
	}
	

	/**
	 * Updates the simulation time and creates new schedules at every day
	 * change.
	 */
	private void updateSimTime() {
		passedSimTime += delta * timeLapse;
		lastTime.setTime(simTime.getTime());
		simTime.add(Calendar.MILLISECOND, delta * timeLapse);
		
		// notify observer
		setChanged();
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				notifyObservers(simTime);
			}
		});
		
		// create new schedules when day of sim time has changed
		if (simTime.get(Calendar.DAY_OF_WEEK) != lastTime.get(Calendar.DAY_OF_WEEK)) {
			createNewSchedules();
		}
	}

	
	/**
	 * Creates all schedules for the day that is set for simTime has at the
	 * moment.
	 */
	private void createNewSchedules() {
		schedules.addAll(ScheduleFactory.createScheduleQueue(simStartTime, simTime, activeSchemes));
	}
	

	/**
	 * Creates and starts new {@link TrainAgent} instances.
	 */
	private void createNewTrains() {
		// add trains that have to start to ready schedules
		while (!schedules.isEmpty()
				&& schedules.peek().getArrivalTime(
						schedules.peek().getStations()[0]) < passedSimTime) {
			readySchedules.add(schedules.poll());
		}
		
		// create trains
		Schedule s;
		TrainAgent agent;
		Iterator<Schedule> it = readySchedules.iterator();
		while (it.hasNext()) {
			s = it.next();
			if (s.getStations()[0].getState().getState() == State.UNBLOCKED) {
				it.remove();
				agent = new TrainAgent(graph, agentCounter, s, aiPort);
				agentCounter++;
				agent.setTimeLapse(timeLapse);//TODO: was passiert wenn eine aktion zwischen erstellen und starten durchgeführt wird
				activeAgents.add(agent);
				executor.execute(agent);
			}
		}
	}
}
