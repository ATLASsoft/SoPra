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

	private static final Path SAVE_DATA_PATH = Paths.get("D", "savedata");//getJarPath().resolveSibling("savedata");
	private static final Path TRAIN_TYPE_PATH = SAVE_DATA_PATH.resolve("traintypes.xml");;
	
	
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
		//xmlParser.saveTrainType(type, TRAIN_TYPE_PATH); //TODO: testmodus entfernen
	}

	@Override
	public List<TrainType> getTrainTypes() {
		//Traintype t1 = new TrainType(name, topSpeed, priority)
		
		
		//xmlParser.get(...)
		
		
		
		List<TrainType> res = new ArrayList<TrainType>();
		SAXBuilder builder = new SAXBuilder();
		  File xmlFile = new File("E:\\traintypes.xml");
	 
		  try {
	 
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List<Element> list = rootNode.getChildren("Train");
		
			
			for (int i = 0; i < list.size(); i++) {
				
			   Element node = (Element) list.get(i);
			   
			   TrainType add = new TrainType(node.getChildText("id"), Double.parseDouble(node.getChildText("topspeed")),Integer.parseInt(node.getChildText("priority")));
			   res.add(add); 
			}
	 
		  } catch (IOException io) {
			System.out.println(io.getMessage());
		  } catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		  }
		
		return res;

	}

	@Override
	public void deleteTrainType(TrainType type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveSchedule(ScheduleScheme schedule) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ScheduleScheme> loadSchedules(String railSysID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteSchedules(String railSysID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveRailwaySystem(RailwaySystem railSys, String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public RailwaySystem loadRailwaySystem(String railSysID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRailwaySystem(String railSysID) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getRailwaySystemIDs() {
		// TODO Auto-generated method stub
		return null;
	}

}
