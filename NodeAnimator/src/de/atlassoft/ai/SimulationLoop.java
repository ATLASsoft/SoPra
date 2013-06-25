package de.atlassoft.ai;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SimulationLoop {

	private int delta;
	private long last;
	private boolean alive;
	private Calendar lastTime;
	private Calendar simTime;
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
	
	
	
	protected SimulationLoop() {
		timeLapse = 1;
		loop = new Loop();
	}
	
	/**
	 * Starts a new run.
	 * @param startTime
	 */
	protected void startRun(Calendar startTime) {
		delta = 0;
		passedSimTime = 0;
		simTime = (Calendar) startTime.clone();
		lastTime = (Calendar) startTime.clone();
		alive = true;
		last = System.currentTimeMillis();
		new Thread(loop).start();
	}
	
	/**
	 * Continues the last run.
	 */
	protected void continueRun() {
		delta = 0;
		last = System.currentTimeMillis();
		alive = true;
		new Thread(loop).start();
	}
	
	protected void stopRun() {
		alive = false;
	}
	
	
	
	private void computeDelta() {
		delta = (int) (System.currentTimeMillis() - last);
		last = System.currentTimeMillis();
	}
	
	private void updateSimTime() {
		passedSimTime += delta * timeLapse;
		lastTime.setTime(simTime.getTime());
		simTime.add(Calendar.MILLISECOND, delta * timeLapse);
	}
	
	private void createNewTrains() {
		
	}
	
	
	private class Loop implements Runnable { //TODO: shutdown sicherstellen
		
		@Override
		public void run() {
			while (alive) {
				computeDelta(); System.out.println(delta);
				updateSimTime();
				createNewTrains();

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	//TODO: mainmethode entfernen
	public static void main(String[] args) throws InterruptedException {
		Calendar cal = new GregorianCalendar();
		SimulationLoop loop = new SimulationLoop();
		loop.startRun(cal);
		
		Thread.sleep(4000);
		
		loop.stopRun();
		Thread.sleep(4000);
		loop.continueRun();
		Thread.sleep(4000);
		loop.stopRun();
		
		
	}
}
