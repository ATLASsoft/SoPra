package de.atlassoft.application;

import java.util.Calendar;

import de.atlassoft.model.ModelService;
import de.atlassoft.model.Node;
import de.atlassoft.model.RailwaySystem;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.SimulationStatistic;
import de.atlassoft.model.TrainType;

//TODO: Interface auskommentieren
public interface ApplicationService {

	/**
	 * Starts this application up. Only this method should be used to start the
	 * application. Loads resources, builds UI et cetera.
	 */
	void initialize();
	
	/**
	 * Shuts this application down. Only this method should be used to terminate
	 * this application.
	 */
	void shutDown();
	
	/**
	 * Returns the object giving access to the data model layer of this application.
	 * @return the data model
	 */
	ModelService getModel();
	
	void startSimulation(Calendar time);
	
	void pauseSimulation();
	
	void continueSimulation();
	
	void quitSimulation();
	
	void addScheduleScheme(ScheduleScheme scheduleScheme);
	
	void deleteScheduleScheme(ScheduleScheme scheduleScheme);
	
	void saveRailwaySystem(RailwaySystem railSys);
	
	void deleteRailwaySystem(String railSysID);
	
	void setActiveRailwaySystem(String railsSysID);
	
	void addTrainType(TrainType trainType);
	
	void deleteTrainType(TrainType trainType);
	
	void showStatisticDoc(SimulationStatistic statistic);
	
	void showScheduleDoc(ScheduleScheme scheduleScheme);
	
	void showDepartureBoardDoc(Node station);
}
