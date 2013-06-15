package de.hohenheim.modell.dijkstra;

import java.util.ArrayList;
import java.util.HashMap;

import de.hohenheim.view.mobile.Utility;
import de.hohenheim.view.node.NodeFigure;
import de.hohenheim.view.path.PathFigure;

/**
 * 
 * @author Marc Fernandes
 * Nutzungsbeispiel:
 *     ArrayList<NodeFigure> list = new ArrayList<NodeFigure>();
 *	   list.addAll(map.getRooms().values());
 *	   Graph g = new Graph(list, map.getPaths());
 *	   g.printDistancesFromStartNode(g.getNodes().get(0), new ArrayList<Node>());
 *	   g.getShortestPath(g.getNodes().get(0), g.getNodes().get(6), new ArrayList<Node>());
 */
public class Graph {
  ArrayList<Node> nodes             = new ArrayList<Node>(); 
  HashMap<NodeFigure, Node> nodehashMap		= new HashMap<NodeFigure, Node>();
  ArrayList<Edge> edges             = new ArrayList<Edge>();
  HashMap<Node, Double> distance    = new HashMap<Node, Double>();
  HashMap<Node, Node>   predecessor = new HashMap<Node, Node>();
  HashMap<Node, ArrayList<Edge>> node_edges = new HashMap<Node, ArrayList<Edge>>();
  
  public Graph(ArrayList<NodeFigure> nodeList, HashMap<NodeFigure, ArrayList<PathFigure>> paths) {
	  for(int i=0; i<nodeList.size(); i++) {
		  Node node = new Node(nodeList.get(i));
		  nodes.add(node);
		  nodehashMap.put(nodeList.get(i), node);
	  }
	  
	  for(int i=0; i<nodes.size(); i++) {
		Node start = nodes.get(i);
		NodeFigure room = start.getNode();  
	    ArrayList<PathFigure> pas = paths.get(room);
		for(int j=0; j<pas.size(); j++) {
			NodeFigure erf = Utility.getNextNode(pas.get(j), room);
			Node end = getNode(erf);
			Edge e = new Edge(pas.get(j), start, end);
			if(node_edges.get(start) == null) {
				  node_edges.put(start, new ArrayList<Edge>());
			}
			node_edges.get(start).add(e);
			edges.add(e);
		  }
	  }	  
  }
  
  public ArrayList<Node> getNodes() {
	  return this.nodes;
  }
  
  public ArrayList<Edge> getEdges() {
	  return this.edges;
  }
  
  private void initialize(Node start, ArrayList<Node> Q) {
	  distance    = new HashMap<Node, Double>();
	  predecessor = new HashMap<Node, Node>();
	  
	  for(int i=0;i<nodes.size();i++) {
		  if(nodes.get(i).equals(start)) {
			  distance.put(nodes.get(i), new Double(0.0));
			  predecessor.put(nodes.get(i), null);			  
		  } else {
			  distance.put(nodes.get(i), new Double(Double.POSITIVE_INFINITY));
			  predecessor.put(nodes.get(i), null);
		  }
		  Q.add(nodes.get(i));
	  }
  }
  private void dijkstra(Node start, ArrayList<Node> Q) {
	  initialize(start, Q);
	 
	  while (Q.size()>0) {		  
		  int tmp_index=-1;
		  Node n=null;
		  double tmp_dist = Double.POSITIVE_INFINITY;
		  for(int i = 0; i < Q.size(); i++) {
			  Node n_temp = Q.get(i);
			  if(distance.get(n_temp).doubleValue() < tmp_dist) {
				  n         = n_temp;
				  tmp_dist  = distance.get(n).doubleValue();
				  tmp_index = i;
			  }			  
		  }
		  if(n==null) {
			  break;
		  }
		  Q.remove(tmp_index);
		 
		 ArrayList<Edge> es = node_edges.get(n);  
		 for(int i=0; i<es.size(); i++) {
			 double dist = es.get(i).getDistance();
			 Node end = es.get(i).getEndNode();

			 double alt = distance.get(n) + dist;
			 if(alt<distance.get(end)) {
				 distance.put(end, new Double(alt));
				 predecessor.put(end, n);
			 }
		 }		  
	  }	  
  }
  
  public void printDistancesFromStartNode(Node start, ArrayList<Node> Q) {
	  dijkstra(start, Q);
	  for(int i=0;i<nodes.size(); i++) {
		  String name=" - ";
		  if(predecessor.get(nodes.get(i))!=null)
		    name=predecessor.get(nodes.get(i)).getNode().getName();
		    System.out.print("Von " + name + " zu "+ nodes.get(i).getNode().getName()+"=");		    		  
		    System.out.println(distance.get(nodes.get(i)));
	  }
  }
  
  public ArrayList<Node> getShortestPath(Node start, Node end, ArrayList<Node> Q) {
	  dijkstra(start, Q);
	  ArrayList<Node> retVal = new ArrayList<Node>();
	  ArrayList<Node> retValtmp = new ArrayList<Node>();
	  Node pred = predecessor.get(end);
	  //double dist = distance.get(end); //This is the complete distance...
	  retValtmp.add(end);
	  while(pred!=null) {
		  retValtmp.add(pred);
		  pred=predecessor.get(pred);		  
	  }
	  
	 /*
	  * Now everything is in the reverse order ... 
	  */
	  for(int i=retValtmp.size()-1; i>=0; i--) {
	    retVal.add(retValtmp.get(i));
	  }
	  
	  for(int i=0; i<retVal.size(); i++) {
		  System.out.println(retVal.get(i).getNode().getName());
	  }
	  return retVal;
  }
 
  public HashMap<NodeFigure, Node> getNodeHashMap() {
	  return this.nodehashMap;
  }
  
  private Node getNode(NodeFigure room) {
	  for(int i=0; i<nodes.size(); i++) {
		  if(nodes.get(i).getNode().equals(room)) {
			  return nodes.get(i);
		  }
	  }
	  return null;
  }
}
