package de.atlassoft.io.persistence;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

	protected void saveTrainType(TrainType type, Path path) throws IOException {
		// if file does not exist, create new
				if (!Files.exists(path)) {
					// createNewTrainTypeXML(TRAIN_TYPE_PATH);
				}

				// add content
				FileWriter out = null;

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

					// display nice nice
					out = new FileWriter("D:\\traintypes.xml");
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
				

				/*
				 * If the XML does not exists already
				 */
//				else {
//					try {
		//
//						Element traintypes = new Element("TrainTypes");
//						Document doc = new Document(traintypes);
		//
//						Element train = new Element(type.getName());
		//
//						train.addContent(new Element("topspeed").setText(Double
//								.toString(type.getTopSpeed())));
		//
//						train.addContent(new Element("priority").setText(Integer
//								.toString(type.getPriority())));
		//
//						doc.getRootElement().addContent(train);
		//
//						// new XMLOutputter().output(doc, System.out);
//						XMLOutputter xmlOutput = new XMLOutputter();
		//
//						xmlOutput.setFormat(Format.getPrettyFormat());
//						xmlOutput.output(doc, new FileWriter("D:\\traintypes.xml"));
		//
//						System.out.println("File Saved!");
//					} catch (IOException io) {
//						System.out.println(io.getMessage());
//					}
//				}
	}
	
	
}
