package de.atlassoft.model;

import de.hohenheim.view.FigureFactory;
import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.mobile.TrainFigure;
import de.hohenheim.view.node.NodeFigure;

//TODO: kommentieren, Noch unklar ob State notwendig
public class Train {

	private TrainFigure trainFigure;
	private TrainType trainType;
	private Schedule schedule;
	private TrainRideStatistic statistic;
	private int id;
	private State state;
	
	
	public Train(NodeMap map, NodeFigure in_room, int id, TrainType trainType, Schedule schedule) {
		// TODO: animationlistener etc. siehe Train aus framework
		trainFigure = FigureFactory.createTrainFigure(map, in_room, id, this);
		this.id = id;
		this.schedule = schedule;
		this.trainType = trainType;
		//this.statistic = new TrainRideStatistic(scheduleScheme); //TODO: statistic erstellen
		state = new State();
		state.setState(State.UNBLOCKED);
	}
	
	public TrainType getTrainType() {
		return trainType;
	}

	public TrainFigure getTrainFigure() {
		return trainFigure;
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
