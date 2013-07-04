package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.atlassoft.model.Schedule;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.util.ScheduleFactory;
import de.hohenheim.view.map.NodeMap;

//TODO: unvollständig
/**
 * 
 * @author Alexander Balogh
 * 
 */
public class SimulationLoop {

	private int delta;
	private long last;
	private boolean alive;
	private Calendar lastTime;
	private Calendar simTime;
	private List<ScheduleScheme> activeSchemes;
	private List<Schedule> readySchedules;
	private PriorityQueue<Schedule> schedules;
	private Queue<TrainAgent> finishedTrains;
	private Loop loop;

	/**
	 * Passed simulated time in ms since the start of the SimulationLoop
	 */
	private long passedSimTime;

	/**
	 * Time lapse. For example timeLapse = 1000 means one second in real time
	 * equals 1000 seconds in simulated time.
	 */
	private int timeLapse;

	/**
	 * Creates a new simulation loop.
	 */
	protected SimulationLoop() {
		timeLapse = 1;
		finishedTrains = new ConcurrentLinkedQueue<>();
		readySchedules = new ArrayList<>();
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

		// init time management
		delta = 0;
		passedSimTime = 0;
		simTime = (Calendar) startTime.clone();
		lastTime = (Calendar) startTime.clone();
		last = System.currentTimeMillis();

		alive = true;
		new Thread(loop).start();
	}

	/**
	 * Continues the last run.
	 */
	protected void continueRun() {
		// reset time management
		delta = 0;
		alive = true;
		last = System.currentTimeMillis();

		new Thread(loop).start();
	}

	protected void stopRun() {
		alive = false;
		// TODO: züge stoppen
		deleteFinishedTrains();
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
				System.out.println(delta);
				updateSimTime();
				createNewSchedules();
				createNewTrains();
				deleteFinishedTrains();

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
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
	 * Updates the simulation time.
	 */
	private void updateSimTime() {
		passedSimTime += delta * timeLapse;
		lastTime.setTime(simTime.getTime());
		simTime.add(Calendar.MILLISECOND, delta * timeLapse);
	}

	private void createNewSchedules() {
		readySchedules.addAll(ScheduleFactory.createSchedules(activeSchemes,
				lastTime, simTime));
	}

	private void createNewTrains() {
		// add trains that have to start to ready schedules
		while (ScheduleFactory.isAfter(schedules.peek().getArrivalTimes()[0], simTime)) {
			readySchedules.add(schedules.poll());
		}
		
		// create trains
		Schedule s;
		Iterator<Schedule> it = readySchedules.iterator();
		while (it.hasNext()) {
			s = it.next();
			// if (man kann zug setzten) {
			// it.remove();
			// }
		}
	}

	private void deleteFinishedTrains() {
		NodeMap map = null;
		TrainAgent agent;
		while (!finishedTrains.isEmpty()) {
			agent = finishedTrains.poll();
			map.getAnimationLayer().remove(agent.getTrainFigure());
			map.getMobileObjects().remove(agent.getID());
		}
	}

}
