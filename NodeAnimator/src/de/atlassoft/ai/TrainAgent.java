package de.atlassoft.ai;

import de.atlassoft.model.Schedule;
import de.atlassoft.model.TrainRideStatistic;
import de.hohenheim.view.FigureFactory;
import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.mobile.TrainFigure;

//TODO: implementiern
public class TrainAgent implements Runnable {

	private int id;
	private TrainFigure figure;
	private Schedule schedule;
	private TrainRideStatistic statistic;
	private Graph graph;
	
	public TrainAgent(Graph graph, int id, Schedule schedule) {
		this.figure = FigureFactory.createTrainFigure(
				graph.getRailwaySystem().getNodeMap(), schedule.getStations()[0].getNodeFigure(),
				id,
				this);
		
		this.statistic = new TrainRideStatistic(schedule.getScheme());
		this.schedule = schedule;
		this.id = id;
	}
	
	
	
	public TrainFigure getTrainFigure() {
		return figure;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public TrainRideStatistic getTrainRideStatistic() {
		return statistic;
	}

	public int getID() {
		return id;
	}
	
	
	
	@Override
	public void run() {
		
		
	}

}
