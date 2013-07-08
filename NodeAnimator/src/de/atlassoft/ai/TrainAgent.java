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
	private List<TrainAgent> agents;
	
	protected TrainAgent(Graph graph, int id, Schedule schedule, List<TrainAgent> agents) {
		this.statistic = new TrainRideStatistic(schedule.getScheme());
		this.schedule = schedule;
		this.id = id;
		this.graph = graph;
		this.agents = agents;
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
		
		List<NodeFigure> path;
		for (int i = 0; i < schedule.getStations().length - 1; i++) {
			path = getShortestPath(schedule.getStations()[i], schedule.getStations()[i + 1]);
			anim = figure.walkAlong(path);
			anim.setTimeLapse(timeLapse);
			figure.startAnimation();	
			
			// wait till animator reached end
			synchronized (anim) { // acquire lock
				try {
					System.out.println("warten");anim.wait();System.out.println("genug gewartet");
				} catch (InterruptedException e) {
					System.out.println("interrupted");
					e.printStackTrace();
				}
			}
			
			
		}
		removeFigure();//TODO: animation/warten bevor die züge verschwinden
		
	}
	
	protected void terminate() {
		
	}
	
	private void removeFigure() {
		agents.remove(this);
		figure.stopAnimation();
		figure.clearAnimations();
		final NodeMap map = graph.getRailwaySystem().getNodeMap();
		map.getDisplay().syncExec(new Runnable() {
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
