package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.atlassoft.model.Node;
import de.atlassoft.model.Schedule;
import de.atlassoft.model.State;
import de.atlassoft.model.TrainRideStatistic;
import de.hohenheim.view.FigureFactory;
import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.mobile.TrainFigure;
import de.hohenheim.view.mobile.animation.SimpleWalkToAnimator;
import de.hohenheim.view.node.NodeFigure;

//TODO: implementiern
public class TrainAgent implements Runnable {

	private int id;
	private TrainFigure figure;
	private Schedule schedule;
	private TrainRideStatistic statistic;
	private Graph graph;
	private List<State> blockedState;
	private int timeLapse;
	private SimpleWalkToAnimator anim;
	private Thread runningThread;
	private AIServiceImpl aiPort;
	
	protected TrainAgent(Graph graph, int id, Schedule schedule, AIServiceImpl aiPort) {
		this.statistic = new TrainRideStatistic(schedule.getScheme());
		this.schedule = schedule;
		this.id = id;
		this.graph = graph;
		this.aiPort = aiPort;
		this.blockedState = new ArrayList<>();
		figure = FigureFactory.createTrainFigure(
				graph.getRailwaySystem().getNodeMap(),
				schedule.getStations()[0].getNodeFigure(),
				id,
				this);
	}
	
	
	
	protected void continueRide() { //TODO: wenn nur start/stop aufgerufen wird überflüssig, da des auch in der loop gemacht werden kann
		figure.startAnimation();
	}
	
	protected void pauseRide() {
		figure.stopAnimation();
	}
	
	protected void setTimeLapse(int timeLapse) {
		this.timeLapse = timeLapse;
		if (anim != null) {
			anim.setTimeLapse(timeLapse);
		}
	}
	
	
	protected TrainFigure getTrainFigure() {
		return figure;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	protected TrainRideStatistic getTrainRideStatistic() {
		return statistic;
	}

	protected int getID() {
		return id;
	}
	
	
	
	@Override
	public void run() {
		runningThread = Thread.currentThread();
		List<NodeFigure> path;
		for (int i = 0; i < schedule.getStations().length - 1; i++) {
			path = getShortestPath(schedule.getStations()[i], schedule.getStations()[i + 1]);
			anim = figure.walkAlong(path);
			anim.setTimeLapse(timeLapse);
			figure.startAnimation();	
			
			// wait till animator reached end
			synchronized (anim) { // acquire lock
				try {
					while (! figure.getNodeFigure().getModellObject().equals(schedule.getStations()[i+1])) {
						anim.wait();
						
						// animation is finished, goal reached
						if (anim.isFinished()) {
							statistic.addStop(figure.getNodeFigure().getModellObject(), 10); //TODO: delay
						}
						// animation has been aborted before reaching goal
						else {
							System.out.println("train agent " + id + " waits");
							figure.stopAnimation();//TODO: gscheit reagieren
							figure.clearAnimations();
							figure.busy(100);
							figure.startAnimation();
						}
					}
				} catch (InterruptedException e) {
					System.out.println("train agent " + id + " has been interrupted, terminates");
					return;
				}
			}
			
			
		}
		removeFigure();//TODO: animation/warten bevor die züge verschwinden
		aiPort.getLoop().activeAgents.remove(this);
		aiPort.getSimStat().addStatistic(statistic);
	}
	
	protected void terminate() {
		runningThread.interrupt(); //TODO: states cleanen
		removeFigure();
	}
	
	private void removeFigure() {
		figure.stopAnimation();
		figure.clearAnimations();
		final NodeMap map = graph.getRailwaySystem().getNodeMap();
		map.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				map.getAnimationLayer().remove(figure);
				map.getMobileObjects().remove("" + id);
			}
		});
	}
	
	
	private List<NodeFigure> getShortestPath(Node source, Node target) {
		List<Node> path = graph.getShortestPath(source, target,
				schedule.getScheme().getTrainType().getTopSpeed());
		
		Iterator<Node> it = path.iterator();
		List<NodeFigure> figurePath = new ArrayList<>();
		it.next(); // one does not need first element
		while (it.hasNext()) {
			figurePath.add(it.next().getNodeFigure());
		}
		return figurePath;
	}
	
	
	
	@Override
	public String toString() {
		return "id: " + id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TrainAgent)) {
			return false;
		} else {
			return this.id == ((TrainAgent) obj).id;
		}
	}
	
	@Override
	public int hashCode() {
		return id;
	}

}
