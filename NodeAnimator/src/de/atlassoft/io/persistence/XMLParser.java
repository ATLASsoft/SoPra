package de.atlassoft.io.persistence;

import java.util.List;

/**
 * Class which provides methods to create, delete, modify and read XML-Files
 * 
 * @author Linus
 */
public class XMLParser {

	/**
	 * Returns a list of Objects (TrainTypes, ScheduleSchemes, RailwaySystems)
	 * form a XML-File
	 * 
	 * @param fileName
	 *            The name of the XML-File which is to be read
	 * @return List<Object> The list of Objects (TrainTypes, ScheduleSchemes,
	 *         RailwaySystems) stored in the XML-File
	 */
	protected List<Object> readXML(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Edits an existing XML-File
	 * 
	 * @param fileName
	 *            The name of the XML-File which is to be edited
	 */
	protected void editXML(String fileName) {
		// TODO Auto-generated method stub
	}

	/**
	 * Creates a new XML-File with the given fileName and the specific Object
	 * 
	 * @param fileName
	 *            The name of the XML-File which is to be created
	 * @param toSave
	 *            The Object that is to be saved in the XML-File
	 */
	protected void createXML(String fileName, Object toSave) {
		// TODO Auto-generated method stub
	}

	/**
	 * Deletes an existing XML-File with the given fileName
	 * 
	 * @param fileName
	 *            The name of the XML-File which is to be deleted
	 */
	protected void deleteXML(String fileName) {
		// TODO Auto-generated method stub
	}
}
