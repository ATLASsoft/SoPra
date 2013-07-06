package de.atlassoft.io.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.atlassoft.model.ModelServiceImpl;
import de.atlassoft.model.RailwaySystem;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.TrainType;

/**
 * Class which implements the PersistenceService
 * 
 * @author Linus, Andreas
 */
public class PersistenceServiceImpl implements PersistenceService {

	private static final Path SAVE_DATA_PATH = Paths.get("C:\\SOPRAsavedata");// getJarPath().resolveSibling("savedata");
	private static final Path TRAIN_TYPE_PATH = SAVE_DATA_PATH.resolve("traintypes.xml");
	private static final Path SCHEDULESCHEME_PATH = SAVE_DATA_PATH.resolve("schedulescheme.xml");
	private static final Path RAILWAYSYS_PATH = SAVE_DATA_PATH.resolve("railwaysys.xml");
	
	private static Path getJarPath() {
		String classpath = System.getProperty("java.class.path");
		if (classpath.contains(";")) {
			String[] split = classpath.split(";");
			classpath = split[0];
		}
		return Paths.get(classpath);
	}

	protected static void createSaveDataDir() throws IOException {
		if (!Files.exists(SAVE_DATA_PATH)) {
			Files.createDirectory(SAVE_DATA_PATH);
		}
	}

	private XMLParser xmlParser;

	public PersistenceServiceImpl() {
		xmlParser = new XMLParser();
	}

	@Override
	public void saveTrainType(TrainType type) throws IOException {
		createSaveDataDir();
		if (!Files.exists(TRAIN_TYPE_PATH)) {
			xmlParser.createXML(TRAIN_TYPE_PATH, "Traintypes");
		}
		xmlParser.saveTrainType(type, TRAIN_TYPE_PATH);
	}

	@Override
	public List<TrainType> loadTrainTypes() throws IOException {
		return xmlParser.loadTrainTypes(TRAIN_TYPE_PATH);
	}

	@Override
	public void deleteTrainType(TrainType type) throws IOException {
		xmlParser.deleteTrainType(type, TRAIN_TYPE_PATH);
	}

	@Override
	public void saveSchedule(ScheduleScheme schedule) throws IOException {
		createSaveDataDir();
		if (!Files.exists(SCHEDULESCHEME_PATH)) {
			xmlParser.createXML(SCHEDULESCHEME_PATH, "Schedules");
		}
		xmlParser.saveSchedule(schedule, SCHEDULESCHEME_PATH);

	}

	@Override
	public List<ScheduleScheme> loadSchedules(RailwaySystem railSys, List<TrainType> trainTypes)
			throws IOException {
		return xmlParser.loadSchedules(railSys, SCHEDULESCHEME_PATH, trainTypes);
	}

	@Override
	public void deleteSchedules(String railSysID) throws IOException {
		xmlParser.deleteSchedules(railSysID, SCHEDULESCHEME_PATH);
	}

	@Override
	public void deleteSingleSchedule(ScheduleScheme schedule)
			throws IOException {
		xmlParser.deleteSingleSchedule(schedule, SCHEDULESCHEME_PATH);

	}

	@Override
	public void deleteSchedules(TrainType train) throws IOException {
		xmlParser.deleteScheduleByTrainType(train, SCHEDULESCHEME_PATH);

	}
	
	@Override
	public void saveRailwaySystem(RailwaySystem railSys) throws IOException {
		createSaveDataDir();
		if (!Files.exists(RAILWAYSYS_PATH)) {
			xmlParser.createXML(RAILWAYSYS_PATH, "Railwaysystems");
		}
		xmlParser.saveRailWaySystem(railSys, RAILWAYSYS_PATH);
	}

	@Override
	public RailwaySystem loadRailwaySystem(String railSysID) throws IOException {
		return xmlParser.loadRailwaySystem(railSysID, RAILWAYSYS_PATH);
	}

	@Override
	public void deleteRailwaySystem(String railSysID) throws IOException {
		xmlParser.deleteRailwaySystem(railSysID, RAILWAYSYS_PATH);
	}

	@Override
	public List<String> getRailwaySystemIDs() throws IOException {
		return xmlParser.getRailwaySystemIDs(RAILWAYSYS_PATH);
	}
}
