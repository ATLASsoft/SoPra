package de.atlassoft.io.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Attribute;
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
 * @author Linus and
 */
public class PersistenceServiceXMLImpl implements PersistenceService {

	@Override
	public void saveTrainType(TrainType type) {

		File f = new File("D:\\traintypes.xml");
		
		/**
		 * If the XML already exists
		 */
		if (f.exists()) {
			try {

				SAXBuilder builder = new SAXBuilder();
				File xmlFile = new File("D:\\traintypes.xml");

				Document doc = (Document) builder.build(xmlFile);

				Element train = new Element(type.getName());

				train.addContent(new Element("topspeed").setText(Double
						.toString(type.getTopSpeed())));

				train.addContent(new Element("priority").setText(Integer
						.toString(type.getPriority())));

				doc.getRootElement().addContent(train);
				

				XMLOutputter xmlOutput = new XMLOutputter();

				// display nice nice
				xmlOutput.setFormat(Format.getPrettyFormat());
				xmlOutput.output(doc, new FileWriter("D:\\traintypes.xml"));

				// xmlOutput.output(doc, System.out);

				System.out.println("File updated!");
			} catch (IOException io) {
				io.printStackTrace();
			} catch (JDOMException e) {
				e.printStackTrace();
			}
		}

		/**
		 * If the XML does not exists already
		 */
		else {
			try {

				Element traintypes = new Element("TrainTypes");
				Document doc = new Document(traintypes);

				Element train = new Element(type.getName());

				train.addContent(new Element("topspeed").setText(Double
						.toString(type.getTopSpeed())));

				train.addContent(new Element("priority").setText(Integer
						.toString(type.getPriority())));

				doc.getRootElement().addContent(train);

				// new XMLOutputter().output(doc, System.out);
				XMLOutputter xmlOutput = new XMLOutputter();

				xmlOutput.setFormat(Format.getPrettyFormat());
				xmlOutput.output(doc, new FileWriter("D:\\traintypes.xml"));

				System.out.println("File Saved!");
			} catch (IOException io) {
				System.out.println(io.getMessage());
			}
		}
	}

	@Override
	public List<TrainType> getTrainTypes() {
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
