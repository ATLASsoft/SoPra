package de.atlassoft.io.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.atlassoft.model.TrainType;

/**
 * Class which provides methods to create, delete, modify and read XML-Files
 * 
 * @author Linus and
 */
class XMLParser {
	

	protected void createXML(Path path, String rootElement) throws IOException{
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
		if (!Files.exists(path)) {
			createXML(path, "Traintype");
			}
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

	protected List<TrainType> getTrainTypes(Path path){
		List<TrainType> res = new ArrayList<TrainType>();
		SAXBuilder builder = new SAXBuilder();
		  File xmlFile = new File(path.toString());
	 
		  try {
	 
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List<Element> list = rootNode.getChildren();
			
		
			
			for (int i = 0; i < list.size(); i++) {

			   Element node = (Element) list.get(i);

			   TrainType add = new TrainType
					   		(node.getName(),
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
	
	protected void deleteTrainType(TrainType type, Path path) throws IOException{
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
			
			
		
	}catch (JDOMException exp) {
		throw new IOException(exp);
	} finally {
		if (out != null) {
			out.close();
		}

	}
	
	
	}}

