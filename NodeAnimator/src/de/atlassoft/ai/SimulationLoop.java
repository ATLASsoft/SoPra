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

	private int delta;
	private long last;
	private volatile boolean alive;
	private Calendar lastTime;
	private Calendar simTime;
	private List<ScheduleScheme> activeSchemes;
	private List<Schedule> readySchedules;
	private PriorityQueue<Schedule> schedules;
	private List<TrainAgent> activeAgents;
	private Loop loop;
	private Graph graph;
	private int agentCounter;
	private ExecutorService executor;
	private Thread loopThread;

	/**
	 * Passed simulated time in ms since the start of the SimulationLoop
	 */
	private long passedSimTime;

	/**
	 * Time lapse. For example timeLapse = 1000 means one second in real time
	 * equals 1000 seconds in simulated time.
	 */
	private volatile int timeLapse;
	
	/**
	 * Creates a new simulation loop.
	 */
	protected SimulationLoop(Graph graph) {
		this.graph = graph;
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
		readySchedules.clear();
		activeSchemes = schemes;
		schedules = ScheduleFactory
				.createScheduleQueue(schemes, startTime); // create schedules
															// for first day
		
		agentCounter = 1;
		
		// init time management
		delta = 0;
		passedSimTime = 0;
		simTime = (Calendar) startTime.clone();
		lastTime = (Calendar) startTime.clone();
		last = System.currentTimeMillis();

		alive = true;
		loopThread = new Thread(loop);
		loopThread.start();
	}

	/**
	 * Continues the last run.
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

	protected void stopRun() {
		// stop simulation loop
		alive = false;
		try {
			loopThread.join();
		} catch (InterruptedException e) {
			System.out.println("unhandled interrupt @ stopRun");
		}
		System.out.println("loop gestopt");
		// stop trains
		synchronized (activeAgents) {
			Iterator<TrainAgent> it = activeAgents.iterator();
			while (it.hasNext()) {
				it.next().pauseRide();
			}
		}
		
		
	}
	
	protected void setTimeLapse(int timeLapse) {
		this.timeLapse = timeLapse;
		synchronized (activeAgents) {
			Iterator<TrainAgent> it = activeAgents.iterator();
			while (it.hasNext()) {
				it.next().setTimeLapse(timeLapse);
			}
		}
	}
	
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
	 * Indicates whether the simulation loop is running at the moment.
	 * 
	 * @return true if running
	 */
	protected boolean isAlive() {
		return alive;
	}

	private class Loop implements Runnable { // TODO: shutdown sicherstellen

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
		Calendar cal = new GregorianCalendar();
		cal.clear();
		cal.set(Calendar.DAY_OF_WEEK, simTime.get(Calendar.DAY_OF_WEEK));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		schedules.addAll(ScheduleFactory.createScheduleQueue(activeSchemes, cal));
	}

	private void createNewTrains() {
		// add trains that have to start to ready schedules
		while (!schedules.isEmpty()
				&& ScheduleFactory.isAfter(simTime, schedules.peek().getArrivalTimes()[0])) {
			readySchedules.add(schedules.poll());
		}
		
		// create trains
		Schedule s;
		TrainAgent agent;
		Iterator<Schedule> it = readySchedules.iterator();
		while (it.hasNext()) {
			s = it.next();
			if (s.getStations()[0].getState().getState() != State.BLOCKED) {
				it.remove();
				agent = new TrainAgent(graph, agentCounter, s, activeAgents);
				agentCounter++;
				agent.setTimeLapse(timeLapse);//TODO: was passiert wenn eine aktion zwischen erstellen und starten durchgeführt wird
				activeAgents.add(agent);
				executor.execute(agent);
			}
		}
	}
}
