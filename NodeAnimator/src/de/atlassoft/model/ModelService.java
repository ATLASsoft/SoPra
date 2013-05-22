package de.atlassoft.model;

import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Service interface providing access to the model layer.
 */
public interface ModelService {

	/**
	 * Add a PropertyChangeListener to the listener list.
	 * The listener is registered for all properties. The same
	 * listener object may be added more than once, and will be called
	 * as many times as it is added. If listener is null,
	 * no exception is thrown and no action is taken.
	 *
	 * @param listener The PropertyChangeListener to be added
	 */
	void addPropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Remove a PropertyChangeListener from the listener list.
	 * This removes a PropertyChangeListener that was registered for all
	 * properties. If listener was added more than once to the same event
	 * source, it will be notified one less time after being removed.
	 * If listener is null, or was never added, no exception is thrown
	 * and no action is taken.
	 *
	 * @param listener The PropertyChangeListener to be removed
	 */
	void removePropertyChangeListener(PropertyChangeListener listener);

	/**
	 * NOCH UNKLAR!
	 */
	void firePropertyChangeEvent(); // TODO: unklare Spezifikation



	/**
	 * Removes the currently active railway system from the model and sets
	 * the passed <code>RailwaySystem</code> as active railway system.
	 * If railSys is null, the current active railway system will be removed
	 * but no new railway system will be set.
	 *
	 * @param railSys <code>RailwaySystem</code> to be added
	 */
	void setActiveRailwaySys(RailwaySystem railSys);

	/**
	 * Returns the currently active <code>RailwaySystem</code>.
	 *
	 * @return The currently active <code>RailwaySystem</code> or null
	 * if there is no active <code>RailwaySystem</code> set
	 */
	RailwaySystem getActiveRailwaySys();

	/**
	 * Returns a list containing all IDs of all <code>RailwaySystems</code>.
	 *
	 * @return List of all <code>RailwaySystems</code> or an empty list if
	 * there are no <code>RailwaySystems</code>
	 */
	List<String> getRailwaySystemIDs();



	/**
	 * Adds a new <code>TrainType</code> to the list of train types.
	 * If type is null, no exception is thrown and no action is taken.
	 *
	 * @param type The <code>TrainType</code> to be added
	 */
	void addTrainType(TrainType type);

	/**
	 * Removes a <code>TrainType</code> from the list of train types.
	 * If type is null, or was never added no exception is thrown
	 * and no action is taken.
	 *
	 * @param type The <code>TrainType</code> to be removed
	 */
	void deleteTrainType(TrainType type);

	/**
	 * Returns a list containing all <code>TrainTypes</code>.
	 *
	 * @return List of all <code>TrainTypes</code> or an empty list
	 * if there are no train types
	 */
	List<TrainType> getTrainTypes();



	/**
	 * Adds a new <code>ScheduleScheme</code> to the list of schedule schemes.
	 * If schedule is null, no exception is thrown and no action is taken.
	 *
	 * @param schedule The <code>ScheduleScheme</code> to be added
	 */
	void addSchedule(ScheduleScheme schedule);

	/**
	 * Removes a <code>ScheduleScheme</code> from the list of chedule schemes.
	 * If schedule is null, or was never added no exception is thrown
	 * and no action is taken.
	 *
	 * @param schedule The <code>ScheduleScheme</code> to be removed
	 */
	void deleteSchedule(ScheduleScheme schedule);

	/**
	 * Returns a list containing all <code>ScheduleSchemes</code>.
	 *
	 * @return List of all <code>ScheduleSchemes</code> or an empty list
	 * if there are no schedule schemes
	 */
	List<ScheduleScheme> getSchedules();



	/**
	 * Returns the latest <code>SimulationStatistic</code>.
	 * @return The latest <code>SimulationStatistic</code> or null
	 * if there is no statistic
	 */
	SimulationStatistic getStatistic();
}
