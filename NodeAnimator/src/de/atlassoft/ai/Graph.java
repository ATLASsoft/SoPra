package de.atlassoft.ai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.atlassoft.model.Node;
import de.atlassoft.model.Path;
import de.atlassoft.model.RailwaySystem;
import de.hohenheim.view.mobile.Utility;

class Graph {

	private Map<String, Vertex> vertexes;
	
	Graph(RailwaySystem railSys) {
		vertexes = new HashMap<>();
		buildGraph(railSys);
	}
	
	private void buildGraph(RailwaySystem railSys) {
		// create vertexes
		Vertex v;
		List<Node> modelNodes = railSys.getNodes();
		for (Node mNode : modelNodes) {
			v = new Vertex(mNode);
			vertexes.put(mNode.getName(), v);
		}
		
		// create edges
		Edge e1;
		Edge e2;
		Vertex start;
		Vertex end;
		double distance;
		double topSpeed;
		List<Path> modelPaths = railSys.getPaths();
		for (Path mPath : modelPaths) {
			start = vertexes.get(mPath.getStart().getName());
			end = vertexes.get(mPath.getEnd().getName());
			distance = Utility.getDistance(mPath.getPathFigure());
			topSpeed = mPath.getTopSpeed();
			
			e1 = new Edge(start, end, distance, topSpeed); // edges in both directions
			e2 = new Edge(end, start, distance, topSpeed);
			
			// add edges to vertexes
			start.addOutgoingEdge(e1);
			end.addOutgoingEdge(e2);
		}
		
	}
	
	//TODO: entferen
	/**
	 * Testfunktion ob der Graph richtig erzeugt wurde
	 */
	void output() {
		System.out.println("Graph");
		Vertex v;
		for (Entry<String, Vertex> e : vertexes.entrySet()) {
			v = e.getValue();
			System.out.println(v);
			System.out.println("ausgehende Kanten:");
			for (Edge edge : v.getOutgoingEdges()) {
				System.out.println(edge.toString());
			}
			System.out.println("");
		}
		
	}
		
		
		
		
}
