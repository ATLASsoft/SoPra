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

import de.atlassoft.model.RailwaySystem;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.TrainType;

/**
 * Class which implements the PersistenceService
 * 
 * @author Linus, Andreas
 */
public class PersistenceServiceImpl implements PersistenceService {

	private static final Path SAVE_DATA_PATH = Paths.get("E:\\savedata");//getJarPath().resolveSibling("savedata");
	private static final Path TRAIN_TYPE_PATH = SAVE_DATA_PATH.resolve("traintypes.xml");;
	private static final Path SCHEDULESCHEME_PATH = SAVE_DATA_PATH.resolve("schedulescheme.xml");
	private static Path getJarPath() {
		String classpath = System.getProperty("java.class.path");
		if (classpath.contains(";")) {
			String[] split = classpath.split(";");
			classpath = split[0];
		}
		return Paths.get(classpath);
	}
	
	
	private XMLParser xmlParser;
	
	public PersistenceServiceImpl() {
		xmlParser = new XMLParser();
	}
	
	
	@Override
	public void saveTrainType(TrainType type) throws IOException {
		//xmlParser.saveTrainType(type, TRAIN_TYPE_PATH); //TODO: testmodus entfernen, id als para hinzufügen
	}

	@Override
	public List<TrainType> getTrainTypes() throws IOException {
		//Traintype t1 = new TrainType(name, topSpeed, priority)
		return null;
		 //return xmlParser.getTrainTypes(TRAIN_TYPE_PATH); //TODO: testmodus entfernen
	}

	@Override
	public void deleteTrainType(TrainType type) throws IOException {
		//xmlParser.deleteTrainType(type, TRAIN_TYPE_PATH); //TODO: testmodus entfernen

	}

	@Override
	public void saveSchedule(ScheduleScheme schedule, String railSysID) throws IOException {
		//xmlParser.saveSchedule(schedule, railSysID, SCHEDULESCHEME_PATH); //TODO: testmodus entfernen

	}

	@Override
	public List<ScheduleScheme> loadSchedules(String railSysID) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteSchedules(String railSysID) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveRailwaySystem(RailwaySystem railSys) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public RailwaySystem loadRailwaySystem(String railSysID) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRailwaySystem(String railSysID) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getRailwaySystemIDs() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
