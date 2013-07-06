package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

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
		
		//TODO: besser lösen
		Display.getDefault().syncExec(new Runnable() {
			int foo;
			@Override
			public void run() {
				figure = FigureFactory.createTrainFigure(
						TrainAgent.this.graph.getRailwaySystem().getNodeMap(), TrainAgent.this.schedule.getStations()[0].getNodeFigure(),
						TrainAgent.this.id,
						TrainAgent.this);
			}
		});
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
		
		
	}
	
	protected void terminate() {
		
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
