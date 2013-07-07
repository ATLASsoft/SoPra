package de.atlassoft.util;

import java.io.IOException;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.io.persistence.PersistenceService;
import de.atlassoft.io.persistence.PersistenceServiceImpl;
import de.atlassoft.model.Node;
import de.atlassoft.model.Path;
import de.atlassoft.model.RailwaySystem;

abstract class SaveDataAssistent {
	
	private static RailwaySystem createRailSys1() {
		RailwaySystem railSys = new RailwaySystem("RailSys 1");

		// create nodes
		Node node1 = new Node("Node 1", 15, 23, 15, 15);
		railSys.addNode(node1);
		Node node2 = new Node("Node 2", 555, 200, 15, 15);
		railSys.addNode(node2);
		Node node3 = new Node("Node 3", 400, 300, 15, 15);
		railSys.addNode(node3);
		Node node4 = new Node("Node 4", 555, 400, 15, 15);
		railSys.addNode(node4);
		Node node5 = new Node("Node 5", 100, 53, 15, 15);
		railSys.addNode(node5);

		// creates paths
		Path path = new Path(node1, node5, 120);
		railSys.addPath(path);
		path = new Path(node5, node3, 200);
		railSys.addPath(path);
		path = new Path(node4, node3, 5);
		railSys.addPath(path);
		path = new Path(node3, node2, 500);
		railSys.addPath(path);
		path = new Path(node2, node4, 500);
		railSys.addPath(path);

		return railSys;
	}
	
	
	public static void main(String[] args) throws IOException {
		PersistenceService persistence = new PersistenceServiceImpl();
		//persistence.saveRailwaySystem(createRailSys1());
	}
}
