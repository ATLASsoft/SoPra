package de.atlassoft.model;

import java.beans.PropertyChangeListener;
import java.util.List;

public interface ModelService {

	void addPropertyChangeListener(PropertyChangeListener listener);
	void removePropertyChangeListener(PropertyChangeListener listener);
	void firePropertyChangeEvent(); // TODO: unklare Spezifikation
	
	void setActiveRailwaySys(RailwaySystem railSys);
	RailwaySystem getActiveRailwaySys();
	List<String> getRailwaySystemIDs();
	
	void addTrainType(TrainType type);
	void deleteTrainType(TrainType type);
	List<TrainType> getRrainTypes();
	
	void addSchedule(ScheduleScheme schedule);
	void deleteSchedule(ScheduleScheme schedule);
	List<ScheduleScheme> getSchedules();
	
	SimulationStatistic getStatistic();
}
