package de.atlassoft.io.docexport;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.itextpdf.text.DocumentException;

import de.atlassoft.model.Node;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.SimulationStatistic;

/**
 * Service interface providing access to the docexport layer.
 * 
 * @author Linus Neﬂler & Andreas Szlatki
 */
public interface DocExportService {
	
	/**
	 * Creates a PDF document which contains the statistics of the simulation.
	 * If stat is null, an {@link IllegalArgumentException} is thrown.
	 *
	 * @param stat The statistics to be added to the document.
	 */
	void createStatisticDoc (SimulationStatistic stat) throws DocumentException, MalformedURLException, IOException;
	
	/**
	 * 
	 * Creates a PDF document which contains a schedule for a specific train.
	 * If schedule is null, an {@link IllegalArgumentException} is thrown.
	 *
	 * @param schedule The ScheduleScheme to be added to the document.
	 */
	void createScheduleDoc (ScheduleScheme schedule) throws FileNotFoundException, DocumentException, MalformedURLException, IOException;
	
	/**
	 * 
	 * Creates a PDF document which contains a departure board for a specific station.
	 * If station is null, an an {@link IllegalArgumentException} is thrown.
	 *
	 * @param station The Node from which the departure board is requested.
	 */
	
	void createDepartureBoard (Node station, List<ScheduleScheme> scedList) throws DocumentException, MalformedURLException, IOException;
}
