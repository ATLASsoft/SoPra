package de.atlassoft.model;

import de.atlassoft.ai.TrainAgent;
import de.hohenheim.view.FigureFactory;
import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.mobile.TrainFigure;

//TODO: braucht man die klasse?
public class Train {

	private TrainFigure trainFigure;
	private TrainAgent agent;
	private Schedule schedule;
	private TrainRideStatistic statistic;
	private int id;
	private State state;

	public Train(NodeMap map, int id, Schedule schedule) {
		trainFigure = FigureFactory.createTrainFigure(
				map, schedule.getStations()[0].getNodeFigure(), id, this);
		this.id = id;
		this.schedule = schedule;
		this.statistic = new TrainRideStatistic(schedule.getScheme());
		state = new State(this);
	}

	public TrainFigure getTrainFigure() {
		return trainFigure;
	}

	public TrainAgent getTrainAgent() {
		return agent;
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

	public State getState() {
		return state;
	}

}
