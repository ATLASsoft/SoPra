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

import de.atlassoft.model.Node;
import de.atlassoft.model.Schedule;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.State;
import de.atlassoft.util.ScheduleFactory;
import de.hohenheim.view.path.PathFigure;

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
			if (canSet(s)) {
				it.remove();
				agent = new TrainAgent(graph, agentCounter, s, aiPort);
				agentCounter++;
				agent.setTimeLapse(timeLapse);//TODO: was passiert wenn eine aktion zwischen erstellen und starten durchgeführt wird
				activeAgents.add(agent);
				executor.execute(agent);
			}
		}
	}
	
	/**
	 * Checks whether the specified {@link Schedule} can start at the moment.
	 * The schedule can start if at least one of the two following conditions is
	 * true:</p>
	 * a) the start node of the schedule is unblocked <br>
	 * b) the start node of the schedule is blocked, by a train that is moving
	 * towards it AND the new created train will not drive in the direction the
	 * blocking train is coming from AND the next node the new created train
	 * would move to is unblocked
	 * 
	 * @param schedule
	 *            {@link Schedule} whose creation should be considered
	 * @return true if the train can be safely created, otherwise false
	 */
	private boolean canSet(Schedule schedule) {
		State state = schedule.getStations()[0].getState();
		
		// return true if the start node is unblocked
		if (state.getState() == State.UNBLOCKED) {
			return true;
		} 
		
		// safety check, return false if the blocking agent has not set any
		// requests or has already cleared them
		TrainAgent blockingAgent = state.getBlocker();
		Long fromO = state.getFromRequest(blockingAgent);
		if (fromO == null) {
			return false;
		}

		// return false for safety reasons, if the arrival time of the blocking
		// agent at the star node is in less than 10 seconds (in simulation time).
		if (fromO < passedSimTime + 10000) {
			return false;
		}
		
		PathFigure pathO = blockingAgent.getTrainFigure().getPath();
		
		// return false for safety reasons, if the blocking agent has no set
		// path for whatever reasons.
		if (pathO == null) {
			return false;
		}
		
		// compute the path the new created train would take
		Node nextNodeA = graph.getShortestPath(schedule.getStations()[0],
				schedule.getStations()[1],
				schedule.getScheme().getTrainType().getTopSpeed()).get(1);

		// return false if the new created train would drive in the same
		// direction than the blocking agent is coming from
		if (nextNodeA.equals(pathO.getModellObject().getStart())
				|| nextNodeA.equals(pathO.getModellObject().getEnd())) {
			return false;
		}
		
		// return false if the new created train could not immediately
		// start to drive in the direction of nextNodeA
		if (nextNodeA.getState().getState() == State.BLOCKED) {
			return false;
		}

		// everything went good, train can start :)
		return true;	
	}
	
}
