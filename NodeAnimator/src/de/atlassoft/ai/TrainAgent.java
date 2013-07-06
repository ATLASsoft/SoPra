package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.List;

import de.atlassoft.model.Schedule;
import de.atlassoft.model.State;
import de.atlassoft.model.TrainRideStatistic;
import de.hohenheim.view.FigureFactory;
import de.hohenheim.view.mobile.TrainFigure;

//TODO: implementiern
public class TrainAgent implements Runnable {

	private int id;
	private TrainFigure figure;
	private Schedule schedule;
	private TrainRideStatistic statistic;
	private Graph graph;
	private List<State> blockedState;
	
	protected TrainAgent(Graph graph, int id, Schedule schedule) {
		this.statistic = new TrainRideStatistic(schedule.getScheme());
		this.schedule = schedule;
		this.id = id;
		this.graph = graph;
		this.blockedState = new ArrayList<>();
		figure = FigureFactory.createTrainFigure(
				graph.getRailwaySystem().getNodeMap(),
				schedule.getStations()[0].getNodeFigure(),
				id,
				this);
	}
	
	
	
	protected void setTimeLapse(int timeLapse) {
		
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
		
		Vertex[] preList = graph.SSSP_Dijkstra(schedule.getStations()[0],
				schedule.getStations()[schedule.getStations().length - 1],
				schedule.getScheme().getTrainType().getTopSpeed());
		
//		Vertex target = graph.getVertex(schedule.getStations()[1]);
//		List<NodeFigure> path = new ArrayList<>();
//		path.add(schedule.getStations()[1].getNodeFigure());
//		path.add(preList[target.getID()].getModelObject().getNodeFigure());
			
		
	}
	
	protected void terminate() {
		
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
