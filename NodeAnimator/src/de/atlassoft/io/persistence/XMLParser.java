package de.atlassoft.io.persistence;



import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.imageio.ImageIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.atlassoft.model.ModelServiceImpl;
import de.atlassoft.model.Node;
import de.atlassoft.model.RailwaySystem;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.ScheduleType;
import de.atlassoft.model.State;
import de.atlassoft.model.TrainType;

/**
 * Class which provides methods to create, delete, modify and read XML-Files
 * 
 * @author Linus and Andy
 */
class XMLParser {
	
	private PersistenceServiceImpl impl;

	/*
	 * CREATE XML
	 */
	protected void createXML(Path path, String rootElement) throws IOException {
		FileWriter out = null;
		try {

			Element root = new Element(rootElement);

			Document doc = new Document(root);
			XMLOutputter xmlOutput = new XMLOutputter();
			out = new FileWriter(path.toFile());
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, out);
			System.out.println("File Saved!");
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/*
	 * SAVE TrainType
	 */
	protected void saveTrainType(TrainType type, Path path, Path picpath) throws IOException {
		// if file does not exist, create new
		FileWriter out = null;

		// add content

		try {

			SAXBuilder builder = new SAXBuilder();

			Document doc = (Document) builder.build(path.toFile());

			Element train = new Element("Name").setAttribute(new Attribute("ID", type.getName()));

			train.addContent(new Element("topspeed").setText(Double
					.toString(type.getTopSpeed())));

			train.addContent(new Element("priority").setText(Integer
					.toString(type.getPriority())));
			
//			Image test = type.getImg();
//			File outPutFile = new File(".png");
//			ImageIO.write(test, "png", outPutFile);
			String imgToAddInXML;
			//Display d = Display.getDefault();
			
	
			Image image = type.getImg();

			if (image == null) {
				imgToAddInXML = "Nicht vorhanden";
			} else {
				ImageLoader saver = new ImageLoader();
				saver.data = new ImageData[] { image.getImageData() };
				saver.save(picpath + type.getName() + ".png", SWT.IMAGE_PNG);
				imgToAddInXML = picpath + type.getName() + ".png";
			}
			
			train.addContent(new Element("picpath").setText(imgToAddInXML));

			doc.getRootElement().addContent(train);

			XMLOutputter xmlOutput = new XMLOutputter();

			out = new FileWriter(path.toFile());
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, out);

			System.out.println("File updated!");
		} catch (JDOMException exp) {
			throw new IOException(exp);
		} finally {
			if (out != null) {
				out.close();
			}

		}

	}

	/*
	 * LOAD TrainTypes
	 */
	protected List<TrainType> loadTrainTypes(Path path) {
		List<TrainType> res = new ArrayList<TrainType>();
		if (!Files.exists(path)) {
			return res;
		}
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(path.toString());

		try {

			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List<Element> list = rootNode.getChildren();

			for (int i = 0; i < list.size(); i++) {

				Element node = (Element) list.get(i);

				TrainType newTrainType = new TrainType(node.getAttributeValue("ID"),
						Double.parseDouble(node.getChildText("topspeed")),
						Integer.parseInt(node.getChildText("priority")));
				if (!node.getChildText("picpath").equals("Nicht vorhanden")) {
					Display d = Display.getDefault();
					Image imgToAdd = new Image(d, node.getChildText("picpath"));
					newTrainType.setImg(imgToAdd);
				}
				res.add(newTrainType);
			}

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}

		return res;
	}

	/*
	 * DELETE TrainType
	 */
	protected void deleteTrainType(TrainType type, Path path)
			throws IOException {
		if (!Files.exists(path)) {
			return;
		}
		FileWriter out = null;
		try {

			SAXBuilder builder = new SAXBuilder();

			Document doc = (Document) builder.build(path.toFile());
			Element rootNode = doc.getRootElement();
			
			List<Element> allTrainTypes = rootNode.getChildren();
			
			for (Element oneTrainType : allTrainTypes){
				if (oneTrainType.getAttributeValue("ID").equals(type.getName())){
					rootNode.removeContent(oneTrainType);
				}
			}

			XMLOutputter xmlOutput = new XMLOutputter();

			out = new FileWriter(path.toFile());
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, out);
			System.out.println("File deleted!");

		} catch (JDOMException exp) {
			throw new IOException(exp);
		} finally {
			if (out != null) {
				out.close();
			}

		}

	}

	/*
	 * SAVE Schedule
	 */
	protected void saveSchedule(ScheduleScheme schedule, Path path)
			throws IOException {

		FileWriter out = null;
		if (!Files.exists(path)) {
			createXML(path, "Schedules");
		}
		try {

			SAXBuilder builder = new SAXBuilder();
			Document doc = (Document) builder.build(path.toFile());

			// Element RailSysID = new Element("RailSysID");
			Element sced = new Element("Schedule");
			Element abfahrten = new Element("Departures");
			Element fahrTage = new Element("DrivingDays");

			// Schleife �ber alle Stationen
			List<Node> stationList = schedule.getStations();

			sced.setAttribute(new Attribute("id", schedule.getID()));
			sced.addContent(new Element("RailSysID").setText(schedule
					.getRailSysID()));
			sced.addContent(new Element("TrainType").setText(
					schedule.getTrainType().getName()));
			sced.addContent(new Element("ScheduleType").setText(schedule
					.getScheduleType().toString()));

			for (int i = 0; i < stationList.size(); i++) {
				abfahrten.addContent(new Element("Departure" + i)
						.setText(schedule.getStations().get(i).getName()
								+ ":"
								+ Integer.toString(schedule.getArrivalTimes()
										.get(i))
								+ ":"
								+ Integer.toString(schedule.getIdleTimes().get(
										i))));
			}
			sced.addContent(abfahrten);
			sced.addContent(new Element("Firstride").setText(Integer
					.toString(schedule.getFirstRide().get(Calendar.DAY_OF_WEEK))
					+ ":"
					+ Integer.toString(schedule.getFirstRide().get(
							Calendar.HOUR_OF_DAY))
					+ ":"
					+ Integer.toString(schedule.getFirstRide().get(
							Calendar.MINUTE))));
			sced.addContent(new Element("Lastride").setText(Integer
					.toString(schedule.getFirstRide().get(Calendar.DAY_OF_WEEK))
					+ ":"
					+ Integer.toString(schedule.getFirstRide().get(
							Calendar.HOUR_OF_DAY))
					+ ":"
					+ Integer.toString(schedule.getFirstRide().get(
							Calendar.MINUTE))));
			sced.addContent(new Element("Intervall").setText(Integer
					.toString(schedule.getInterval())));
			List<Integer> fahrtage = schedule.getDays();
			for (int i = 0; i < fahrtage.size(); i++) {
				String add = fahrtage.get(i).toString();
				fahrTage.addContent(new Element("DrivingDay").setText(add));
			}
			sced.addContent(fahrTage);

			doc.getRootElement().addContent(sced);

			XMLOutputter xmlOutput = new XMLOutputter();

			out = new FileWriter(path.toFile());
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, out);

			System.out.println("File updated!");
		} catch (JDOMException exp) {
			throw new IOException(exp);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	/*
	 * LOAD Schedules
	 */
	protected List<ScheduleScheme> loadSchedules(RailwaySystem railSys, Path path, List<TrainType> trainTypes)
			throws IOException {
		List<ScheduleScheme> res = new ArrayList<ScheduleScheme>();
		if (!Files.exists(path)) {
			return res;
		}
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(path.toString());

		try {

			Document document = (Document) builder.build(xmlFile);

			// Schedules Element
			Element rootNode = document.getRootElement();

			// all Schedule Elemente
			List<Element> scheduleListe = rootNode.getChildren();

			for (int i = 0; i < scheduleListe.size(); i++) {

				// one Schedule Element
				Element node = (Element) scheduleListe.get(i);
				if (node.getChildText("RailSysID").equalsIgnoreCase(railSys.getID())) {

					String railSysId = node.getChildText("RailSysID");
					String trainType = node.getChildText("TrainType");
					String scheduleType = node.getChildText("ScheduleType");
					String newName = node.getAttributeValue("id");
					String newInterval = node.getChildText("Intervall");
					List<Integer> newDrivingDays = new ArrayList<Integer>();
					String[] departures = null;
					
					// FirstRide && LastRide
					String[] firstRide = node.getChildText("Firstride").split(":");
					String[] lastRide = node.getChildText("Lastride").split(":");
					
					Calendar newCalFirst = new GregorianCalendar();
					newCalFirst.set(Calendar.DAY_OF_WEEK,Integer.parseInt(firstRide[0]));
					newCalFirst.set(Calendar.HOUR_OF_DAY, Integer.parseInt(firstRide[1]));
					newCalFirst.set(Calendar.MINUTE, Integer.parseInt(firstRide[2]));
					
					Calendar newCalLast = new GregorianCalendar();
					newCalLast.set(Calendar.DAY_OF_WEEK,Integer.parseInt(lastRide[0]));
					newCalLast.set(Calendar.HOUR_OF_DAY, Integer.parseInt(lastRide[1]));
					newCalLast.set(Calendar.MINUTE, Integer.parseInt(lastRide[2]));
					
					// schleife �ber drivingDays
					List<Element> drivingDays = node.getChild("DrivingDays")
							.getChildren();
					for (int x = 0; x < drivingDays.size(); x++) {
						newDrivingDays.add(Integer.parseInt(drivingDays.get(x).getText()));
					}

					// trainType aus dem Model holen
					TrainType newTrainType = null;
					
					for (TrainType singleTrainType : trainTypes){
						if (singleTrainType.getName().equalsIgnoreCase(trainType)){
							 newTrainType = singleTrainType;
						}
					}
					
					// neues ScheduleScheme erstellen
					ScheduleScheme newSchedule = new ScheduleScheme(
							ScheduleType.valueOf(scheduleType),
							newTrainType,
							newDrivingDays,
							newCalFirst,
							railSysId,
							newName);
					
					// schleife �ber departures
					List<Element> abfahrten = node.getChild("Departures")
							.getChildren();
					for (int x = 0; x < abfahrten.size(); x++) {
						Element singleAbfahrt = abfahrten.get(x);
						departures = singleAbfahrt.getText().split(":");
						List<Node> nodeList = railSys.getNodes();
						Node newNode = null;
						for (Node nod : nodeList){
							if (nod.getName().equalsIgnoreCase(departures[0])){
								newNode = nod;
							}
						}
						newSchedule.addStop(newNode, Integer.parseInt(departures[1]), Integer.parseInt(departures[2]));
					}

					newSchedule.setLastRide(newCalLast);
					newSchedule.setInterval(Integer.parseInt(newInterval));
			
					res.add(newSchedule);
				}
			}

			return res;
		} catch (JDOMException exp) {
			{throw new IOException(exp);
		} 
		} 
	}

	/*
	 * DELETE SCHEDULES by RailSysID
	 */
	protected void deleteSchedules(String railSysID, Path path)
			throws IOException {
		if (!Files.exists(path)) {
			return;
		}
		FileWriter out = null;
		try {

			SAXBuilder builder = new SAXBuilder();

			Document doc = (Document) builder.build(path.toFile());
			// Schedules Element
			Element rootNode = doc.getRootElement();

			List<Element> scheduleListe = rootNode.getChildren();

			for (int i = 0; i < scheduleListe.size(); i++) {
				Element schedule = (Element) scheduleListe.get(i);
				if (schedule.getChildText("RailSysID").equalsIgnoreCase(
						railSysID)) {
					rootNode.removeContent(schedule);
					i--;
				}

				XMLOutputter xmlOutput = new XMLOutputter();

				out = new FileWriter(path.toFile());
				xmlOutput.setFormat(Format.getPrettyFormat());
				xmlOutput.output(doc, out);
				System.out.println("RailSysId deleted!");
			}

		} catch (JDOMException exp) {
			throw new IOException(exp);
		} finally {
			if (out != null) {
				out.close();
			}

		}

	}

	/*
	 * DELETE SINGLE SCHEDULE by name and RailSysID
	 */
	protected void deleteSingleSchedule(ScheduleScheme scheduleToDelete,
			Path path) throws IOException {
		if (!Files.exists(path)) {
			return;
		}
		FileWriter out = null;
		try {

			SAXBuilder builder = new SAXBuilder();

			Document doc = (Document) builder.build(path.toFile());
			// Schedules Element
			Element rootNode = doc.getRootElement();

			List<Element> scheduleListe = rootNode.getChildren();
			for (int i = 0; i < scheduleListe.size(); i++) {
				Element schedule = (Element) scheduleListe.get(i);
				// Checks if the Schedule is belongig to the proper RailSys and
				// checks the name of the Schedule
				if (schedule.getAttributeValue("id").equalsIgnoreCase(
						scheduleToDelete.getID())
						&& schedule.getChildText("RailSysID").equalsIgnoreCase(
								scheduleToDelete.getRailSysID())) {
					rootNode.removeContent(schedule);
					System.out.println("Single Schedule deleted!");
				}
				XMLOutputter xmlOutput = new XMLOutputter();

				out = new FileWriter(path.toFile());
				xmlOutput.setFormat(Format.getPrettyFormat());
				xmlOutput.output(doc, out);
			}
		} catch (JDOMException exp) {
			throw new IOException(exp);
		} finally {
			if (out != null) {
				out.close();
			}

		}
	}

	/*
	 * DELETE SCHEDULES by TrainType
	 */
	protected void deleteScheduleByTrainType(TrainType train, Path path)
			throws IOException {
		if (!Files.exists(path)) {
			return;
		}
		FileWriter out = null;
		try {

			SAXBuilder builder = new SAXBuilder();

			Document doc = (Document) builder.build(path.toFile());
			// Schedules Element
			Element rootNode = doc.getRootElement();

			List<Element> scheduleListe = rootNode.getChildren();
			for (int i = 0; i < scheduleListe.size(); i++) {
				Element schedule = (Element) scheduleListe.get(i);
				// Checks if the Schedule is belonging to the proper RailSys and
				// checks the name of the Schedule
				if (schedule.getChildText("TrainType").equalsIgnoreCase(
						train.getName())) {
					System.out.println("Schedule deleted by TrainType!");
					rootNode.removeContent(schedule);
					i--;
				}
				XMLOutputter xmlOutput = new XMLOutputter();

				out = new FileWriter(path.toFile());
				xmlOutput.setFormat(Format.getPrettyFormat());
				xmlOutput.output(doc, out);
			}
		} catch (JDOMException exp) {
			throw new IOException(exp);
		} finally {
			if (out != null) {
				out.close();
			}

		}
	}

	protected void saveRailWaySystem(RailwaySystem railSys, Path path)
			throws IOException {
		FileWriter out = null;
		if (!Files.exists(path)) {
			createXML(path, "Railwaysystems");
		}
		try {

			SAXBuilder builder = new SAXBuilder();
			Document doc = (Document) builder.build(path.toFile());
			XMLOutputter xmlOutput = new XMLOutputter();
			
			List<Node> nodelist= railSys.getNodes();
			List<de.atlassoft.model.Path> pathlist = railSys.getPaths();
			//Erstellen
			Element railsys = new Element("Railwaysystem");
			Element nodes = new Element("Nodes");
			Element paths = new Element("Paths");
			railsys.setAttribute(new Attribute("id", railSys.getID()));
			//Schleife f�r alle Knoten
			for (int i = 0; i < nodelist.size(); i++) {

				Element node = new Element ("Node" + i);
				Element name = new Element ("Name").setText(nodelist.get(i).getName());
				Element x = new Element ("X-Koordinate").setText(Integer.toString(nodelist.get(i).getNodeFigure().getBounds().x()));
				Element y = new Element ("Y-Koordinate").setText(Integer.toString(nodelist.get(i).getNodeFigure().getBounds().y()));
				node.addContent(name);
				node.addContent(x);
				node.addContent(y);
				nodes.addContent(node);
			}
			railsys.addContent(nodes);
			// Schleife f�r alle Paths
			for (int i = 0; i < pathlist.size(); i++) {

				Element singlePath = new Element("Path" + i);
				Element topspeed = new Element("Topspeed").setText(Double
						.toString(pathlist.get(i).getTopSpeed()));
				Element start = new Element("Start").setText(pathlist.get(i)
						.getStart().getName());
				Element end = new Element("End").setText(pathlist.get(i)
						.getEnd().getName());

				singlePath.addContent(topspeed);
				singlePath.addContent(start);
				singlePath.addContent(end);

				paths.addContent(singlePath);
			}
			railsys.addContent(paths);
			
			doc.getRootElement().addContent(railsys);		
			out = new FileWriter(path.toFile());
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, out);

			System.out.println("File updated!");

		} catch (JDOMException exp) {
			throw new IOException(exp);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	protected void deleteRailwaySystem(String railSysID, Path path) throws IOException {
		if (!Files.exists(path)) {
			return;
		}
		FileWriter out = null;
		try {

			SAXBuilder builder = new SAXBuilder();

			Document doc = (Document) builder.build(path.toFile());
			// RailwaySystems Element
			Element rootNode = doc.getRootElement();
			
			// RailwaySystem Elemente
			List<Element> railSysliste = rootNode.getChildren();
			for (int i = 0; i < railSysliste.size(); i++) {
				//RailwaySystem Element
				Element railwaySys = (Element) railSysliste.get(i);

				if (railwaySys.getAttributeValue("id").equalsIgnoreCase(
						railSysID)) {
					rootNode.removeContent(railwaySys);
					System.out.println("Railwaysystem deleted!");
				}
				XMLOutputter xmlOutput = new XMLOutputter();

				out = new FileWriter(path.toFile());
				xmlOutput.setFormat(Format.getPrettyFormat());
				xmlOutput.output(doc, out);
			}
		} catch (JDOMException exp) {
			throw new IOException(exp);
		} finally {
			if (out != null) {
				out.close();
			}

		}

	}
	
	protected RailwaySystem loadRailwaySystem(String railSysID, Path path) throws IOException {
		RailwaySystem res = null;
		if (!Files.exists(path)) {
			return res;
		}
		res = new RailwaySystem(railSysID);
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(path.toString());

		try {

			Document document = (Document) builder.build(xmlFile);

			// RailwaySystems Element
			Element rootNode = document.getRootElement();

			// all Railwaysystem Elements
			List<Element> railwaysystemListe = rootNode.getChildren();

			for (int i = 0; i < railwaysystemListe.size(); i++) {
				
				if(railwaysystemListe.get(i).getAttributeValue("id").equalsIgnoreCase(railSysID)){
				// one Railwaysystem Element
				Element railwaysystem = (Element) railwaysystemListe.get(i);
				
				//all Node Elements
				List<Element> nodesListe = railwaysystem.getChild("Nodes").getChildren();
				
				//one Node Element
				for (Element node : nodesListe){
					Node newNode = new Node(node.getChildText("Name"), 
							Integer.parseInt(node.getChildText("X-Koordinate")), 
							Integer.parseInt(node.getChildText("Y-Koordinate")));
					res.addNode(newNode);
				}
				
				//aktuell im geladenen Streckennetz vorhanden knoten
				List<Node> resultNodes = res.getNodes();
				//all Path Elements
				List<Element> pathsListe = railwaysystem.getChild("Paths").getChildren();
				
				for (Element singlePath : pathsListe){
					Node startNodeToAdd = null;
					Node endNodeToAdd = null;
					for (Node resultNode : resultNodes){
						if (resultNode.getName().equalsIgnoreCase(singlePath.getChildText("Start"))){
							 startNodeToAdd = resultNode;
						}
							if (resultNode.getName().equalsIgnoreCase(
									singlePath.getChildText("End"))) {
								 endNodeToAdd = resultNode;
							}
					}
					de.atlassoft.model.Path newPath = new de.atlassoft.model.Path(startNodeToAdd,endNodeToAdd, Double.parseDouble(singlePath.getChildText("Topspeed")));
					res.addPath(newPath);
				}
				
			}
			}
		return res;
			} catch (JDOMException exp) {
				{throw new IOException(exp);
			} 
			} 
	}
	
	protected List<String> getRailwaySystemIDs(Path path) throws IOException {

		List<String> res = new ArrayList<String>();

		if (!Files.exists(path)) {
			return res;
		}
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(path.toString());

		try {

			Document document = (Document) builder.build(xmlFile);

			// RailwaySystems Element
			Element rootNode = document.getRootElement();

			// all RailwaySystem Elemente
			List<Element> railSysliste = rootNode.getChildren();

			for (int i = 0; i < railSysliste.size(); i++) {
				res.add(railSysliste.get(i).getAttributeValue("id"));
			}

			return res;
		} catch (JDOMException exp) {
			{
				throw new IOException(exp);
			}
		}
	}
}
	
