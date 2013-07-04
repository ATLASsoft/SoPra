package de.atlassoft.io.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.atlassoft.model.Node;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.ScheduleType;
import de.atlassoft.model.TrainType;

/**
 * Class which provides methods to create, delete, modify and read XML-Files
 * 
 * @author Linus and
 */
class XMLParser {

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

	protected void saveTrainType(TrainType type, Path path) throws IOException {
		// if file does not exist, create new
		FileWriter out = null;

		// add content

		try {

			SAXBuilder builder = new SAXBuilder();

			Document doc = (Document) builder.build(path.toFile());

			Element train = new Element(type.getName());

			train.addContent(new Element("topspeed").setText(Double
					.toString(type.getTopSpeed())));

			train.addContent(new Element("priority").setText(Integer
					.toString(type.getPriority())));

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

	protected List<TrainType> getTrainTypes(Path path) {
		List<TrainType> res = new ArrayList<TrainType>();
		if(!Files.exists(path)){
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

				TrainType add = new TrainType(node.getName(),
						Double.parseDouble(node.getChildText("topspeed")),
						Integer.parseInt(node.getChildText("priority")));
				res.add(add);
			}

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}

		return res;
	}

	protected void deleteTrainType(TrainType type, Path path)
			throws IOException {
		if(!Files.exists(path)){
			return;
		}
		FileWriter out = null;
		try {

			SAXBuilder builder = new SAXBuilder();

			Document doc = (Document) builder.build(path.toFile());
			Element rootNode = doc.getRootElement();

			rootNode.removeChild(type.getName());

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

	protected void saveSchedule(ScheduleScheme schedule, Path path)
			throws IOException {

		FileWriter out = null;
		if (!Files.exists(path)) {
			createXML(path, "Schedules");
		}
		try {

			SAXBuilder builder = new SAXBuilder();
			Document doc = (Document) builder.build(path.toFile());

			//Element RailSysID = new Element("RailSysID");
			Element sced = new Element("Schedule");
			Element abfahrten = new Element("Departures");
			Element fahrTage = new Element("DrivingDays");

			
			
			// Schleife �ber alle Stationen
			List<Node> stationList = schedule.getStations();

			sced.setAttribute(new Attribute("id", schedule.getID()));
			sced.addContent(new Element("RailSysID").setText(schedule.getRailSysID()));
			sced.addContent(new Element("TrainType").setText(schedule
					.getTrainType().getName()));
			sced.addContent(new Element("ScheduleType").setText(schedule
					.getScheduleType().toString()));
			
			for (int i = 0; i < stationList.size(); i++) {
				abfahrten.addContent(new Element("Departure" + i).setText(schedule
						.getStations().get(i).getName()+ ":" + Integer.toString(schedule.getArrivalTimes().get(i)) + ":" + Integer.toString(schedule.getIdleTimes().get(i))));
			}
			sced.addContent(abfahrten);
			sced.addContent(new Element("Firstride").setText(
					Integer.toString(schedule.getFirstRide().get(Calendar.DAY_OF_WEEK)) + " "
				+   Integer.toString(schedule.getFirstRide().get(Calendar.HOUR_OF_DAY)) + " "
				+	Integer.toString(schedule.getFirstRide().get(Calendar.MINUTE))
				));
			sced.addContent(new Element("Lastride").setText(
					Integer.toString(schedule.getFirstRide().get(Calendar.DAY_OF_WEEK)) + " "
				+   Integer.toString(schedule.getFirstRide().get(Calendar.HOUR_OF_DAY)) + " "
				+	Integer.toString(schedule.getFirstRide().get(Calendar.MINUTE))
				));
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

	protected List<ScheduleScheme> loadSchedules(String railSysID, Path path)
			throws IOException {
		List<ScheduleScheme> res = new ArrayList<ScheduleScheme>();
		if(!Files.exists(path)){
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

				// one RailsysId element
				Element node = (Element) scheduleListe.get(i);
				if (node.getChildText("RailSysID").equalsIgnoreCase(railSysID)) {

					String railSysId = node.getChildText("RailSysID");
					String trainType = node.getChildText("TrainType");
					String scheduleType = node.getChildText("ScheduleType");
					
					//schleife �ber departures
					List<Element> abfahrten = node.getChild("Departures").getChildren();
					
					for (int x = 0; x < abfahrten.size(); x++) {
						Element singleAbfahrt = abfahrten.get(x);

						String[] stringArray = singleAbfahrt.getText()
								.split(":");
						System.out.println(stringArray[0].toString());
						System.out.println(stringArray[1].toString());
						System.out.println(stringArray[2].toString());
					}

						// System.out.println(downnode.getChildText("TrainType").toString());
						// System.out.println(downnode.getChildText("ScheduleType")
						// .toString());
						// System.out.println(downnode.getChildText("Firstride")
						// .toString());

					}
			}

			return res;
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
		return res;
	}
				
				/*
				 * //create Days List List<Element> listwah =
				 * node.getChildren("DrivingDays"); List<Integer> intlist = new
				 * ArrayList<Integer>(); for (int x = 0; x < listwah.size();
				 * x++){ Integer a =
				 * Integer.parseInt(listwah.get(x).toString()); intlist.add(a);
				 * }
				 * 
				 * //CreateTraintype List<Element> traintype =
				 * node.getChildren("TrainType"); TrainType t = null; for (int x
				 * = 0; x < traintype.size(); x++){ String traintypestring =
				 * traintype.get(x).toString(); t = new
				 * TrainType(traintypestring,0,0); }
				 */

				// get Firstride
				/*
				 * Calendar cal=Calendar.getInstance(); try { String str_date =
				 * null; List<Element> cale = node.getChildren("Schedule"); for
				 * (int x = 0; x < cale.size(); x++){ Element hello = (Element)
				 * list.get(x); str_date =
				 * hello.getChildText("FirstRide").toString(); }
				 * System.out.print(str_date); DateFormat formatter ; Date date
				 * ; formatter = new SimpleDateFormat("hh"); date =
				 * (Date)formatter.parse(str_date); cal.setTime(date); } catch
				 * (ParseException e) {
				 * e.printStackTrace(); } ScheduleScheme add = new
				 * ScheduleScheme
				 * (ScheduleType.valueOf(node.getChildText("ScheduleType")),t,
				 * intlist, cal , "") ; res.add(add); }
				 * 
				 * } catch (IOException io) {
				 * System.out.println(io.getMessage()); } catch (JDOMException
				 * jdomex) { System.out.println(jdomex.getMessage());
				 */

	
	
	protected void deleteSchedules(String railSysID, Path path)
			throws IOException {
		if(!Files.exists(path)){
			return;
		}
		FileWriter out = null;
		try {
			
			SAXBuilder builder = new SAXBuilder();

			Document doc = (Document) builder.build(path.toFile());
			// Schedules Element
			Element rootNode = doc.getRootElement();

			List<Element> scheduleListe = rootNode.getChildren();
			
			for (int i = 0; i < scheduleListe.size(); i++){
				Element schedule = (Element) scheduleListe.get(i);
				if (schedule.getChildText("RailSysID").equalsIgnoreCase(railSysID)){
					rootNode.removeContent(schedule);
				}
				// all RailsysId Elemente
//			List<Element> railSysListe = rootNode.getChildren();
//			System.out.println(railSysListe.size());
//
//			for (int i = 0; i < railSysListe.size(); i++) {
//				System.out.println("Hallo ich bin in der Schleife");
//				// one RailsysId element
//				Element node = (Element) railSysListe.get(i);
//
//				if (Integer.toString(node.getAttribute("id").getIntValue()).equalsIgnoreCase(railSysID)) {
//					
//					//rootNode.removeChild(node.getName());
//					
//					System.out.println(Integer.toString(node.getAttribute("id").getIntValue()));
//					System.out.println(railSysID);

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

	protected void deleteSingleSchedule(ScheduleScheme shedule, Path path)
			throws IOException {
		if(!Files.exists(path)){
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
				// TODO RailSysID muss stimmen/�berpr�ft werden, da Name nicht eindeutig
				if (schedule.getAttributeValue("id").equalsIgnoreCase(schedule.getName())) {
					rootNode.removeChild(schedule.getName());
					System.out.println("Schedule deleted!");
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
}