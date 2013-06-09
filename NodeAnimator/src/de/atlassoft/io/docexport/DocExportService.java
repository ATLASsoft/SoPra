package de.atlassoft.io.docexport;

import de.atlassoft.model.Node;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.SimulationStatistic;

/**
 * Service interface providing access to the docexport layer.
 */
public interface DocExportService {
	
	/**
	 * Creates a PDF document which contains the statistics of the simulation.
	 * If stat is null no exception is thrown and no action is taken.
	 *
	 * @param stat The statistics to be added to the document.
	 */
	void createStatisticDoc (SimulationStatistic stat);
	
	/**
	 * 
	 *Creates a PDF document which contains a schedule for a specific train.
	 *
	 * @param schedule The ScheduleScheme to be added to the document.
	 */
	void createScheduleDoc (ScheduleScheme schedule);
	
	/**
	 * 
	 *Creates a PDF document which contains a departure board for a specific station.
	 *
	 * @param station The Node from which the departure board is requested.
	 */
	void createDepartureBoard (Node station);
}
