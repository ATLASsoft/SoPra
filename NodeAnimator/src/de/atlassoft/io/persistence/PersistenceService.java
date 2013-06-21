package de.atlassoft.io.persistence;

import java.io.IOException;
import java.util.List;
import de.atlassoft.model.RailwaySystem;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.TrainType;

/**
 * Service interface providing access to the persistence layer.
 * 
 * @author Linus, Andreas
 */
public interface PersistenceService {

	/**
	 * Saves a TrainType in the specific XML-File for TrainTypes
	 * 
	 * @param type
	 *            The TrainType that is to be saved by the System
	 */
	public void saveTrainType(TrainType type) throws IOException;

	/**
	 * Returns a List of all TrainTypes saved
	 * 
	 * @return List<TrainType> The list which contains all TrainTypes saved so
	 *         far
	 */
	public List<TrainType> getTrainTypes() throws IOException;

	/**
	 * Deletes a TrainType from the TrainTypes stored in the specific XML-File
	 * for TrainTypes
	 * 
	 * @param type
	 *            The TrainType that is to be saved by the System
	 */
	public void deleteTrainType(TrainType type) throws IOException;

	/**
	 * Saves a ScheduleScheme in the specific XML-File for ScheduleSchemes
	 * 
	 * @param schedule
	 *            The ScheduleScheme that is to be saved
	 */
	public void saveSchedule(ScheduleScheme schedule, String railSysID) throws IOException;

	/**
	 * Returns a List of all ScheduleSchemes that have been saved
	 * 
	 * @param railSysID
	 *            The ID of the RailwaySystem, to which the ScheduleSchemes
	 *            belong
	 * @return List<ScheduleScheme> The List of all the ScheduleSchemes that
	 *         belong to the RailwaySystem
	 */
	public List<ScheduleScheme> loadSchedules(String railSysID) throws IOException;

	/**
	 * Deletes all Schedules belonging to a specific RailwaySystem
	 * 
	 * @param railSysID
	 *            The ID of the RailwaySystem, of which the Schedules are to be
	 *            deleted
	 */
	public void deleteSchedules(String railSysID) throws IOException;

	/**
	 * Saves a RailwaySystem in the specific file for RailwaySystems
	 * 
	 * @param railSys
	 *            The RailwaySystem that is to be saved
	 * @param id
	 *            The ID of this RailwaySystem
	 */
	public void saveRailwaySystem(RailwaySystem railSys) throws IOException;

	/**
	 * Loads a specific RailwaySystem
	 * 
	 * @param railSysID
	 *            The ID of the RailwaySystem that is to be loaded
	 * @return RailwaySystem The RailwaySystem that was requested
	 */
	public RailwaySystem loadRailwaySystem(String railSysID) throws IOException;

	/**
	 * Deletes a RailwaySystem from the file which stores all RailwaySystems
	 * 
	 * @param railSysID
	 *            The ID of the RailwaySystem that is to be deleated
	 */
	public void deleteRailwaySystem(String railSysID) throws IOException;

	/**
	 * Returns a List of all the ID of all RailwaySystems that are stored in the
	 * file of all RailwaySystems
	 * 
	 * @return List<String> The List of all ID of all RailwaySystems
	 */
	public List<String> getRailwaySystemIDs() throws IOException;
}
