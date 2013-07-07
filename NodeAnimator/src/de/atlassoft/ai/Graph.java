package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

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
	
	/**
	 * Creates a new instance from the specified {@link RailwaySystem}.
	 * @param railSys
	 */
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


	
	/**
	 * Returns the vertex that belongs to the {@link Node} n.
	 * 
	 * @param n
	 *            The model object of the wanted vertex
	 * @return The {@link Vertex} that belongs to n
	 * @see {@link Vertex#getModelObject()}
	 */
	protected Vertex getVertex(Node n) {
		return vertexMap.get(n);
	}
	
	/**
	 * Returns the {@link RailwaySystem} this graph belongs to.
	 * 
	 * @return The {@link RailwaySystem} this graph belongs to
	 */
	protected RailwaySystem getRailwaySystem() {
		return railSys;
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
	Vertex[] SSSP_Dijkstra(Node source, Node target,double trainTopSpeed) {
		Vertex start = vertexMap.get(source);
		
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
					d = (e.getDistance() / Math.min(e.getTopSpeed(), trainTopSpeed)) + dist[v.getID()];
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
		// construct list from predecessor array
//		List<Node> path = new ArrayList<>();
//		path.add(target);
//		int i = 0;
//		Vertex pre;
//		while ((pre = predecessor[vertexMap.get(path.get(i)).getID()]) != null) {
//			path.add(pre.getModelObject());
//			i++;
//		}
//		Collections.reverse(path);
//		
//		return path;
	}
	
	/**
	 * Checks whether this graph is connected by doing a breadth first search.
	 * 
	 * @return true if connected, otherwise false
	 */
	protected boolean isConnected() {
		if (vertexes.length < 2) {
			return true;
		}
		
		// init BFS
		boolean[] reached = new boolean[vertexes.length]; // bool inits with false
		Queue<Vertex> R = new LinkedList<>();
		Vertex start = vertexes[0];
		reached[start.getID()] = true;
		R.add(start);
		
		// BFS
		Vertex v, u;
		while (!R.isEmpty()) {
			v = R.poll();
			for (Edge e : v.getOutgoingEdges()) {
				u = e.getEnd();
				if (!reached[u.getID()]) {
					reached[u.getID()] = true;
					R.offer(u);
				}
			}
		}
		
		// return false if at least one vertex was not reached
		for (boolean isReached : reached) {
			if (!isReached) {
				return false;
			}
		}
		
		// all vertexes has been reached, return true
		return true;
	}
	
	//TODO: besser lösen, momentan dijktra kopie
	protected double getDistance(Node source, Node target, double topSpeed) {
		Vertex start = vertexMap.get(source);
		
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
					d = (e.getDistance() / Math.min(e.getTopSpeed(), topSpeed)) + dist[v.getID()];
					if (d < dist[u.getID()]) {
						R.remove(u);
						dist[u.getID()] = d;
						R.offer(u);
						predecessor[u.getID()] = v;
					}
				}
			}
		}
		return dist[getVertex(target).getID()]; // ist pixel / km/h
	}
	
	
}
