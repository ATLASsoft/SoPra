package de.atlassoft.ai;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import de.atlassoft.model.Node;
import de.atlassoft.model.Path;
import de.atlassoft.model.RailwaySystem;
import de.hohenheim.view.mobile.Utility;

class Graph {

	private RailwaySystem railSys;
	
	/**
	 * Maps nodes to their affiliated vertexes.
	 */
	private Map<Node, Vertex> vertexMap;
	Vertex[] vertexes;
	
	Graph(RailwaySystem railSys) {
		this.railSys = railSys;
		vertexMap = new HashMap<>();
		buildGraph();
	}
	
	private void buildGraph() {
		// create vertexes
		Vertex v;
		List<Node> modelNodes = railSys.getNodes();
		vertexes = new Vertex[modelNodes.size()];
		int vertexCount = 0;
		for (Node mNode : modelNodes) {
			v = new Vertex(mNode, vertexCount);
			vertexMap.put(mNode, v);
			vertexes[vertexCount] = v;
			vertexCount++;
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
			start = vertexMap.get(mPath.getStart());
			end = vertexMap.get(mPath.getEnd());
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
		for (Vertex v : vertexes) {
			System.out.println(v);
			System.out.println("ausgehende Kanten:");
			for (Edge edge : v.getOutgoingEdges()) {
				System.out.println(edge.toString());
			}
			System.out.println("");
		}
		
	}
		
	
	/**
	 * Comparator to compare to {@link Vertex} instances. Compares by comparing
	 * the values in a given distance array affiliated with the particular
	 * vertexes.
	 * 
	 * @author Alexander Balogh
	 * 
	 */
	private class VertexComperator implements Comparator<Vertex> {
		private Double[] dist;

		private VertexComperator(Double[] distArray) {
			dist = distArray;
		}
		
		@Override
		public int compare(Vertex o1, Vertex o2) {
			return dist[o1.getID()].compareTo(dist[o2.getID()]);
		}
	}
	
	/**
	 * Computes shortest paths from start to every other vertex using a
	 * implementation of Dijkstra's algorithm. The algorithm causes no side
	 * effects at all. Returns a predecessor array of vertexes where the entry
	 * at position i is the predecessor of the vertex with the id i along every
	 * shortest path from start through the vertex with the id i.
	 * 
	 * @param start
	 *            Source vertex of the algorithm
	 * @param trainTopSpeed
	 * @return Predecessor array
	 */
	Vertex[] SSSP_Dijkstra(Vertex start, double trainTopSpeed) {
		// init dist array
		Double[] dist = new Double[vertexes.length];
		for (int i = 0; i < dist.length; i++) {
			dist[i] = Double.POSITIVE_INFINITY;
		}

		// init heap
		PriorityQueue<Vertex> R = new PriorityQueue<>(dist.length, new VertexComperator(dist));
		dist[start.getID()] = 0.0;
		R.offer(start);
		
		
		// compute shortest paths from start
		Vertex[] predecessor = new Vertex[vertexes.length];
		Vertex v, u;
		double d;
		while (!R.isEmpty()) {
			v = R.poll();
			for (Edge e : v.getOutgoingEdges()) {
				if (!e.isBlocked()) {
					u = e.getEnd();
					d = e.getDistance() / Math.min(e.getTopSpeed(), trainTopSpeed);
					if (d < dist[u.getID()]) {
						R.remove(u);
						dist[u.getID()] = d;
						R.offer(u);
						predecessor[u.getID()] = v;
					}
				}
			}
			
		}
		return predecessor;
	}
		
		
	protected RailwaySystem getRailwaySystem() {
		return railSys;
	}
	
}
