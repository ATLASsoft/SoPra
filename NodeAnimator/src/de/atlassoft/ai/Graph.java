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

class Graph {

	private RailwaySystem railSys;
	
	/**
	 * Maps nodes to their affiliated vertexes.
	 */
	protected Map<Node, Vertex> vertexMap;
	protected Vertex[] vertexes;
	
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
			distance = mPath.getPathFigure().getDistance() / 10.0; // convert distance from pixel to km
			topSpeed = mPath.getTopSpeed();
			e1 = new Edge(start, end, distance, topSpeed, mPath); // edges in both directions
			e2 = new Edge(end, start, distance, topSpeed, mPath);
			
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
	 * Return the array containing all vertexes.
	 * 
	 * @return Array of {@link Vertex vertexes}
	 */
	protected Vertex[] getVertexes() {
		return vertexes;
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
	 * Checks whether this graph is connected by doing a breadth first search.
	 * 
	 * @return true if connected, otherwise false
	 */
	protected boolean isConnected() {
		if (vertexes.length < 2) {
			return true;
		}

		// init BFS
		boolean[] closedList = new boolean[vertexes.length];
		
		Queue<Vertex> openList = new LinkedList<>();
		Vertex start = vertexes[0];
		closedList[start.id] = true;
		openList.add(start);

		// BFS
		Vertex v, u;
		while (!openList.isEmpty()) {
			v = openList.poll();
			for (Edge e : v.getOutgoingEdges()) {
				u = e.getEnd();
				if (!closedList[u.id]) {
					closedList[u.id] = true;
					openList.offer(u);
				}
			}
		}

		// return false if at least one vertex was not reached
		for (boolean isReached : closedList) {
			if (!isReached) {
				return false;
			}
		}

		// all vertexes has been reached, return true
		return true;
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @param topSpeed
	 * @return
	 */
	protected double getShortestTravelTime(Node source, Node target, double topSpeed) {
		// Dijkstra
		double[] dist = new double[vertexes.length];
		Vertex[] predecessor = new Vertex[vertexes.length];
		sssp_Dijkstra_ingnoreBlocks(source, topSpeed, predecessor, dist);
		
		// compute travel time
		return dist[getVertex(target).id]; // in hours
	}
	
	/**
	 * Computes the time in hours an object with the specified topSpeed needs to
	 * walk along the passed path.
	 * 
	 * @param path
	 * @param mobileTopSpeed
	 * @return
	 */
	protected List<Double> getCosts(List<Node> path, double mobileTopSpeed) {
		double cost = 0;
		Vertex start;
		Vertex end;
		List<Double> res = new ArrayList<Double>();
		
		res.add(cost); // cost to reach first node
		for (int i = 0; i < path.size() - 1; i++) {
			start = vertexMap.get(path.get(i));
			end = vertexMap.get(path.get(i + 1));
			
			for (Edge e : start.getOutgoingEdges()) {
				if (e.getEnd().equals(end)) {
					cost += e.getActualCost(mobileTopSpeed);
					break;
				}
			}
			res.add(cost);
		}
		
		return res;
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @param topSpeed
	 * @return
	 */
	protected List<Node> getShortestPath(Node source, Node target, double topSpeed) {
		double[] dist = new double[vertexes.length];
		Vertex[] predecessor = new Vertex[vertexes.length];
		sssp_Dijkstra_ingnoreBlocks(source, topSpeed, predecessor, dist);
		
		// construct list from predecessor array
		List<Node> path = new ArrayList<>();
		path.add(target);
		int i = 0;
		Vertex pre;
		while ((pre = predecessor[vertexMap.get(path.get(i)).id]) != null) {
			path.add(pre.getModelObject());
			i++;
		}
		Collections.reverse(path);
		
		return path;
	}
	
	
	/**
	 * Comparator to compare to {@link Vertex} instances. Compares by comparing
	 * the values in a given distance array affiliated with the particular
	 * vertexes.
	 * 
	 * @author Alexander Balogh
	 * 
	 */
	protected class VertexComperator implements Comparator<Vertex> {
		private double[] dist;

		protected VertexComperator(double[] distArray) {
			dist = distArray;
		}
		
		@Override
		public int compare(Vertex o1, Vertex o2) {
			if (dist[o1.id] < dist[o2.id]) {
				return -1;
			}
			if (dist[o1.id] > dist[o2.id]) {
				return 1;
			}
			
			return 0;
		}
	}
	
	/**
	 * Computes shortest paths from start to every other vertex using a
	 * implementation of Dijkstra's algorithm. Ignores all possible blockades.
	 * The algorithm alters the two passed arrays but other than that causes no
	 * side effects at all. The predecessor array contains all vertexes where
	 * the entry at position i is the predecessor of the vertex with the id i
	 * along every shortest path from start through the vertex with the id i.
	 * The dist array entry at position i equals the simulation time, a mobile
	 * object with <code>trainTopSpeed</code> needs to get from source to
	 * vertexes[i] in hours.
	 * 
	 * @param start
	 *            Source vertex of the algorithm
	 * @param trainTopSpeed
	 *            Top speed of the mobile object
	 * @param predecessor
	 *            The predecessor array, will be altered
	 * @param dist
	 *            The dist array, will be altered
	 * @return Predecessor array
	 */
	private void sssp_Dijkstra_ingnoreBlocks(Node source, double trainTopSpeed,
			Vertex[] predecessor, double[] dist) {
		
		Vertex start = vertexMap.get(source);
		
		// init dist array
		for (int i = 0; i < dist.length; i++) {
			dist[i] = Double.POSITIVE_INFINITY;
		}

		// init open and closed list
		PriorityQueue<Vertex> openList = new PriorityQueue<>(dist.length, new VertexComperator(dist));
		boolean[] closedList = new boolean[vertexes.length];
		dist[start.id] = 0.0;
		openList.offer(start);
		
		// compute shortest paths from start
		Vertex v, u;
		double d;
		while (!openList.isEmpty()) {
			v = openList.poll();
			closedList[v.id] = true;;
			for (Edge e : v.getOutgoingEdges()) {
				u = e.getEnd();
				
				if (closedList[u.id]) {
					continue;
				}
				
				d = e.getActualCost(trainTopSpeed) + dist[v.id];
				if (d < dist[u.id]) {
					openList.remove(u);
					dist[u.id] = d;
					openList.offer(u);
					predecessor[u.id] = v;
				}

			}
			
		}
	}
	
	/**
	 * Computes shortest paths from start to every other vertex using a
	 * implementation of Dijkstra's algorithm. This algorithm considers only
	 * nodes and paths that are not blocked. The algorithm alters the two passed
	 * arrays but other than that causes no side effects at all. The predecessor
	 * array contains all vertexes where the entry at position i is the
	 * predecessor of the vertex with the id i along every shortest path from
	 * start through the vertex with the id i. The dist array entry at position
	 * i equals the simulation time, a mobile object with
	 * <code>trainTopSpeed</code> needs to get from source to vertexes[i] in
	 * hours.
	 * 
	 * @param start
	 *            Source vertex of the algorithm
	 * @param trainTopSpeed
	 *            Top speed of the mobile object
	 * @param predecessor
	 *            The predecessor array, will be altered
	 * @param dist
	 *            The dist array, will be altered. Shortest distance in hours
	 * @return Predecessor array
	 */
	protected void sssp_Dijkstra_considerBlocks(Node source, double trainTopSpeed,
			Vertex[] predecessor, double[] dist) {
		
		Vertex start = vertexMap.get(source);
		
		// init dist array
		for (int i = 0; i < dist.length; i++) {
			dist[i] = Double.POSITIVE_INFINITY;
		}

		// init open and closed list
		PriorityQueue<Vertex> openList = new PriorityQueue<>(dist.length, new VertexComperator(dist));
		boolean[] closedList = new boolean[vertexes.length];
		dist[start.id] = 0.0;
		openList.offer(start);
		
		// compute shortest paths from start
		Vertex v, u;
		double d;
		while (!openList.isEmpty()) {
			v = openList.poll();
			closedList[v.id] = true;;
			for (Edge e : v.getOutgoingEdges()) {
				u = e.getEnd();

				if (closedList[u.id]) {
					continue;
				}
				
				if (e.isBlocked() || u.isBlocked()) {
					continue;
				}
				
				d = e.getActualCost(trainTopSpeed) + dist[v.id];
				if (d < dist[u.id]) {
					openList.remove(u);
					dist[u.id] = d;
					openList.offer(u);
					predecessor[u.id] = v;
				}

			}
			
		}
	}
	
	
	
	
	
	
	
}
