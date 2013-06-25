package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.atlassoft.model.Schedule;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.util.ScheduleFactory;

//TODO: Klasse löschen (in die Simloop intergriet)
class TrainAgentMultiScheduler {

	private List<ScheduleScheme> activeSchemes;
	private Queue<Schedule> readySchedules;
	private Runnable worker;
	private boolean alive;
	
	
	protected TrainAgentMultiScheduler() {
		activeSchemes = new ArrayList<>();
		readySchedules = new ConcurrentLinkedQueue<>();
		
		worker = new Worker();
		alive = true;
		new Thread(worker).start();
	}
	
	protected void createNewTrains(Calendar begin, Calendar end) {
		readySchedules.addAll(ScheduleFactory.createSchedules(activeSchemes, begin, end));
		worker.notifyAll();
	}
	
	
	private class Worker implements Runnable { //TODO: shutdown sicherstellen
		@Override
		public void run() {
			while (alive) {
				// create new trains
				// delete old trains
				synchronized (this) {  // acquire monitor in order to wait
					while (readySchedules.isEmpty()) {
						try {
							this.wait();
						} catch (InterruptedException e) {}
					}
				}
			}
		
		
		}
	}
	
}
