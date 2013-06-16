package de.atlassoft.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Service interface providing access to the model layer.
 * 
 * @author Alexander Balogh
 * 
 */
public interface ModelService {

	/**
	 * Property name of the active railway system. Used by {@link PropertyChangeEvent}.
	 */
	public static String ACTIVE_RAILSYS_PROPNAME = "activeRailSys";
	
	/**
	 * Property name of the train type property. Used by {@link PropertyChangeEvent}
	 * after updating the list of train types.
	 */
	public static String TRAIN_TYPES_PROPNAME = "trainTypes";
	
	/**
	 * Property name of the active schedule scheme property. Used by {@link PropertyChangeEvent}
	 * after updating the list of active schedule schemes.
	 */
	public static String ACTIVE_SCHEDULE_SCHEMES_PROPNAME = "activeSchemes";
	
	/**
	 * Property name of the passive schedule scheme property. Used by {@link PropertyChangeEvent}
	 * after updating the list of passive schedule schemes.
	 */
	public static String PASSIVE_SCHEDULE_SCHEMES_PROPNAME = "passiveSchemes";
	
	/**
	 * Property name of the railway system ids property. Used by {@link PropertyChangeEvent}
	 * after updating the list of railway system ids.
	 */
	public static String RAILSYS_IDS_PROPNAME = "railsysIDs";
	
	
	
	/**
	 * Add a {@link PropertyChangeListener} to the listener list. The listener is
	 * registered for all properties. The same listener object may be added more
	 * than once, and will be called as many times as it is added. If listener
	 * is null, no exception is thrown and no action is taken.
	 * 
	 * @param listener
	 *            The PropertyChangeListener to be added
	 */
	void addPropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Remove a {@link PropertyChangeListener} from the listener list. This removes a
	 * PropertyChangeListener that was registered for all properties. If
	 * listener was added more than once to the same event source, it will be
	 * notified one less time after being removed. If listener is null, or was
	 * never added, no exception is thrown and no action is taken.
	 * 
	 * @param listener
	 *            The PropertyChangeListener to be removed
	 */
	void removePropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Removes the currently active railway system from the model and sets the
	 * passed {@link RailwaySystem} as active railway system. If railSys is
	 * null, the current active railway system will be removed but no new
	 * railway system will be set.
	 * 
	 * @param railSys
	 *            {@link RailwaySystem} to be added
	 */
	void setActiveRailwaySys(RailwaySystem railSys);

	/**
	 * Returns the currently active {@link RailwaySystem}.
	 * 
	 * @return The currently active {@link RailwaySystem} or null if there
	 *         is no active {@link RailwaySystem} set
	 */
	RailwaySystem getActiveRailwaySys();

	/**
	 * Adds a new {@link String} to the list of railway system IDs. If id is
	 * null, empty or has already been added, an
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param id
	 *            The {@link String} to be added
	 */
	void addRailwaySystemID(String id);

	/**
	 * Removes a {@link String} from the list of railway system IDs. If id is
	 * null, or was never added no exception is thrown and no action is taken.
	 * 
	 * @param type
	 *            The {@link String} to be removed
	 */
	void removeRailwaySystemID(String id);

	/**
	 * Returns a list of {@link Strings}, containing all IDs of all
	 * <code>RailwaySystems</code>.
	 * 
	 * @return List of all <code>RailwaySystems</code> or an empty list if there
	 *         are no <code>RailwaySystems</code>
	 */
	List<String> getRailwaySystemIDs();

	/**
	 * Adds a new {@link TrainType} to the list of train types. If type is
	 * null or has already been added, no exception is thrown and
	 * no action is taken.
	 * 
	 * @param type
	 *            The {@link TrainType} to be added
	 */
	void addTrainType(TrainType type);

	/**
	 * Removes a {@link TrainType} from the list of train types. If type is
	 * null, or was never added no exception is thrown and no action is taken.
	 * 
	 * @param type
	 *            The {@link TrainType} to be removed
	 */
	void deleteTrainType(TrainType type);

	/**
	 * Returns a list containing all <code>TrainTypes</code>.
	 * 
	 * @return List of all <code>TrainTypes</code> or an empty list if there are
	 *         no train types
	 */
	List<TrainType> getTrainTypes();

	/**
	 * Adds a new {@link ScheduleScheme} to the list of active schedule schemes.
	 * If schedule is null or has already been added, no exception is thrown and
	 * no action is taken.
	 * 
	 * @param scheme
	 *            The {@link ScheduleScheme} to be added
	 */
	void addActiveScheduleScheme(ScheduleScheme scheme);

	/**
	 * Removes a {@link ScheduleScheme} from the list of active schedule
	 * schemes. If schedule is null, or was never added no exception is thrown
	 * and no action is taken.
	 * 
	 * @param scheme
	 *            The {@link ScheduleScheme} to be removed
	 */
	void removeActiveScheduleScheme(ScheduleScheme scheme);

	/**
	 * Returns a list containing all active <code>ScheduleSchemes</code>.
	 * 
	 * @return List of all <code>ScheduleSchemes</code> or an empty list if
	 *         there are no schedule schemes
	 */
	List<ScheduleScheme> getActiveScheduleSchemes();

	/**
	 * Adds a new {@link ScheduleScheme} to the list of passive schedule schemes.
	 * If schedule is null or has already been added, no exception is thrown and
	 * no action is taken.
	 * 
	 * @param scheme
	 *            The {@link ScheduleScheme} to be added
	 */
	void addPassiveScheduleScheme(ScheduleScheme scheme);
	
	/**
	 * Removes a {@link ScheduleScheme} from the list of passive schedule
	 * schemes. If schedule is null, or was never added no exception is thrown
	 * and no action is taken.
	 * 
	 * @param scheme
	 *            The {@link ScheduleScheme} to be removed
	 */
	void removePassiveScheduleScheme(ScheduleScheme scheme);
	
	/**
	 * Returns a list containing all passive <code>ScheduleSchemes</code>.
	 * 
	 * @return List of all <code>ScheduleSchemes</code> or an empty list if
	 *         there are no schedule schemes
	 */
	List<ScheduleScheme> getPassiveScheduleSchemes();
	
	/**
	 * Returns the latest {@link SimulationStatistic}.
	 * 
	 * @return The latest {@link SimulationStatistic} or null if there is
	 *         no statistic
	 */
	SimulationStatistic getStatistic();
}
