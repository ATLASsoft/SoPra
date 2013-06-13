package de.atlassoft.io.persistence;

import de.atlassoft.model.TrainType;

public class PersistenceTEST {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PersistenceServiceXMLImpl pers = new PersistenceServiceXMLImpl();
		TrainType t1 = new TrainType("ICE", 300.0, 10);
		TrainType t2 = new TrainType("TGV", 400.0, 10);
		pers.saveTrainType(t1);
		/**
		 * add this to see how it works if the xml already exists
		 */
		// pers.saveTrainType(t2);
	}

}
