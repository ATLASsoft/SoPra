package de.atlassoft.model;

import java.beans.PropertyChangeListener;
import java.util.List;
//TODO: Klasse implementieren
public class ModelServiceImpl implements ModelService {

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void firePropertyChangeEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setActiveRailwaySys(RailwaySystem railSys) {
		// TODO Auto-generated method stub

	}

	@Override
	public RailwaySystem getActiveRailwaySys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getRailwaySystemIDs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addTrainType(TrainType type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteTrainType(TrainType type) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<TrainType> getRrainTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addSchedule(ScheduleScheme schedule) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSchedule(ScheduleScheme schedule) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ScheduleScheme> getSchedules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimulationStatistic getStatistic() {
		// TODO Auto-generated method stub
		return null;
	}

}
