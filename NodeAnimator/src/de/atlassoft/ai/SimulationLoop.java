package de.atlassoft.ai;

public class SimulationLoop {

	private long delta;
	private long last;
	private boolean run;
	
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
		
	}
	
	
	
	private void computeDelta() {
		delta = System.currentTimeMillis() - last;
		last = System.currentTimeMillis();
	}
	
	private void updateSimTime() {
		passedSimTime += delta * timeLapse;
	}
	
	private void createNewTrains() {
		
	}
	
	
	private class Loop implements Runnable {
		
		@Override
		public void run() {
			while (run) {
				computeDelta();
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
}
