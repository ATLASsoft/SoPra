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
	 * Property name of the active railway system. Use to invoke a {@link PropertyChangeEvent}
	 * after using {@link ModelService#setActiveRailwaySys(RailwaySystem)}.
	 */
	public static String ACTIVE_RAILSYS_PROPNAME = "activeRailSys";
	
	/**
	 * Property name of the train type property. Use to invoke a {@link PropertyChangeEvent}
	 * after updating the list of train types.
	 */
	public static String TRAIN_TYPES_PROPNAME = "trainTypes";
	
	/**
	 * Property name of the schedule scheme property. Use to invoke a {@link PropertyChangeEvent}
	 * after updating the list of schedule schemes.
	 */
	public static String SCHEDULE_SCHEMES_PROPNAME = "scheduleSchemes";
	
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
	 * Reports a property update of the property affiliated with propertyName to
	 * all listeners that have been registered to this model by invoking a
	 * {@link PropertyChangeEvent}. Note that oldValue of the fired
	 * {@link PropertyChangeEvent} is not supported. Only the XXX_PROPNAME
	 * constants of this interface are valid parameters for this method.
	 * 
	 * @param propertyName
	 *            name of the changed property, must be one of the XXX_PROPNAME
	 *            constants of this interface
	 * @throws IllegalArgumentException
	 *             if propertyName is none of the XXX_PROPNAME constants
	 */
	void firePropertyChangeEvent(String propertyName); // TODO: unklare Spezifikation

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
	 * Returns a list containing all IDs of all <code>RailwaySystems</code>.
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
	 * Adds a new {@link ScheduleScheme} to the list of schedule schemes. If
	 * schedule is null or has already been added, no exception is thrown and no
	 * action is taken.
	 * 
	 * @param schedule
	 *            The {@link ScheduleScheme} to be added
	 */
	void addScheduleScheme(ScheduleScheme schedule);

	/**
	 * Removes a {@link ScheduleScheme} from the list of schedule schemes.
	 * If schedule is null, or was never added no exception is thrown and no
	 * action is taken.
	 * 
	 * @param schedule
	 *            The {@link ScheduleScheme} to be removed
	 */
	void deleteScheduleScheme(ScheduleScheme schedule);

	/**
	 * Returns a list containing all <code>ScheduleSchemes</code>.
	 * 
	 * @return List of all <code>ScheduleSchemes</code> or an empty list if
	 *         there are no schedule schemes
	 */
	List<ScheduleScheme> getScheduleSchemes();

	/**
	 * Returns the latest {@link SimulationStatistic}.
	 * 
	 * @return The latest {@link SimulationStatistic} or null if there is
	 *         no statistic
	 */
	SimulationStatistic getStatistic();
}
