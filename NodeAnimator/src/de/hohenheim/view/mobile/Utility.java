package de.hohenheim.view.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

import de.hohenheim.modell.dijkstra.Graph;
import de.hohenheim.modell.dijkstra.Node;
import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.node.NodeFigure;
import de.hohenheim.view.path.PathFigure;

/**
 * Utility class to help calculate paths, distances and more.
 * @author Marc Fernandes
 *
 */
public class Utility {
  
  /**
   * Calculates the distance value on the path between the start room
   * and the end room.
   * @param {@link PathFigure} path - the path we want to have the length.
   *  
   * @return double distance
   */
  public static double getDistance(PathFigure path) {
	  PointList points = path.getPoints();
	  double distance=0;
	  for(int i=0; i<points.size()-1; i++) {
		  Point p1 = points.getPoint(i);
		  Point p2 = points.getPoint(i+1);
		  distance+=p1.getDistance(p2);  
	  }
	  return distance;
  }
  /**
   * Determines the room at the end of the path regarding the start room which we  pass as parameter.
   * @param {@link PathFigure} path - The path where we want to have the end room. 
   * @param {@link NodeFigure} start - The start room.
   * @return {@link NodeFigure} room - the room which is at the other end of the path regarding the start room.
   */
  public static NodeFigure getNextNode(PathFigure path, NodeFigure start) {
	  if(path.getSourceAnchor().getOwner().equals(start)) {
		  return (NodeFigure) path.getTargetAnchor().getOwner();
	  } else {
		  return (NodeFigure) path.getSourceAnchor().getOwner();
	  }	  
  }
  
  /**
   * We want to determine if there exists a edge between two rooms.
   * 
   * @param {@link HashMap} paths - The HashMap with all paths of all rooms 
   * @param start - The start room.
   * @param end - The end room.
   * @return boolean - true when a path between the two nodes exist.
   */
  public static boolean hasEdge(HashMap<NodeFigure, ArrayList<PathFigure>> paths,NodeFigure start, NodeFigure end) {
	  ArrayList<PathFigure> p = paths.get(start);
	  for(int i=0; i<p.size(); i++) {
		  NodeFigure end_tmp = getNextNode(p.get(i), start);
		  if(end_tmp.equals(end)) {
			  return true;
		  }
	  }
	  return false;
  }
  /**
   * Calculates a route between two rooms. this algorithm is quite simple and does not optimize the route (no shortest paths).
   * @param {@link NodeMap} map - The RoomMap where the rooms are located. 
   * @param {@link NodeFigure} start - The start room where the calculation begins. 
   * @param {@link NodeFigure} end - The end room where the calculation should stop.
   * @return {@link Iterator} with all RoomFigures ({@link NodeFigure}) which mark the route.
   */
  public static Iterator<NodeFigure> getRoute(
		  NodeMap map,
		  NodeFigure start, 
		  NodeFigure end) {
	  
	  ArrayList<NodeFigure> roomList = new ArrayList<NodeFigure>();
	  ArrayList<NodeFigure> roomVisited = new ArrayList<NodeFigure>();
	  ArrayList<PathFigure> edgeVisited = new ArrayList<PathFigure>();
	  
	  //Cut off the start_room, we don't need it. it must be saved in the personfigure for the animation.
	  ArrayList<NodeFigure> tmppath = getRoute(roomList, roomVisited, edgeVisited, map.getPaths(), start, end);
	  ArrayList<NodeFigure> path = new ArrayList<NodeFigure>();
	  if(tmppath.size()>1) {
	    path.addAll(tmppath.subList(1, tmppath.size())); 
	  } else {
		  path=tmppath;
	  }	  
	  
	  return path.iterator();
  }
  
  /**
   * This method is a helper and makes recursion. 
   * @see Utility#getRoute(NodeMap, NodeFigure, NodeFigure)
   */
  private static ArrayList<NodeFigure> getRoute(
		  ArrayList<NodeFigure> roomList,
		  ArrayList<NodeFigure> roomVisited,
		  ArrayList<PathFigure> edgeVisited,
		  HashMap<NodeFigure, ArrayList<PathFigure>> paths,
		  NodeFigure start, 
		  NodeFigure end
		  ) {

	  roomList.add(start);
	  if(roomList.contains(end)) {
		  return roomList;
	  }
	  
	  ArrayList<PathFigure> direct_paths_start = paths.get(start);
	  
	  for(int i=0; i<direct_paths_start.size(); i++) {
		  PathFigure next_path = direct_paths_start.get(i);
		  
		  NodeFigure next_start = Utility.getNextNode(next_path, start);
		 
		  if(next_start.equals(end)) {			  			 			  
			  roomList.add(end);			  
			  return roomList;
          }
	  }
	 
	  if(!roomList.contains(end)) {
		  for(int i=0; i<direct_paths_start.size(); i++) {
			  PathFigure next_path = direct_paths_start.get(i);
			  NodeFigure next_start = Utility.getNextNode(next_path, start);			  
			  if(!edgeVisited.contains(next_path)) {
			    if(!roomList.contains(end)) {
				  if(!roomList.contains(next_start)) {					  
					edgeVisited.add(next_path);
			        roomList=getRoute(roomList, roomVisited, edgeVisited, paths, next_start, end);
			      }
			    }
			  }
		  }
	  }	 
	  
	  //Optimize. we sometimes have nodes inside which are wrong. We now filter them out...
	  //i.e. all nodes which have no edge to the former node. 
	  int i=0;
	  while(i<roomList.size()-1) {
		  if(!hasEdge(paths, roomList.get(i), roomList.get(i+1))) {
			  roomList.remove(i);
			  i=0;
		  } else {		  
		  i++;
		  }
	  }
	  return roomList;
  }   
  
  
  public static Iterator<NodeFigure> getOptimizedRoute(
		  NodeMap map,
		  NodeFigure start, 
		  NodeFigure end) {
	  
	  ArrayList<NodeFigure> list = new ArrayList<NodeFigure>();
	  list.addAll(map.getNodes().values());
	  Graph g = new Graph(list, map.getPaths());
	  
	  HashMap<NodeFigure, Node> nodeMap = g.getNodeHashMap();
	  ArrayList<Node> nodes=g.getShortestPath(nodeMap.get(start), nodeMap.get(end), new ArrayList<Node>());
	  ArrayList<NodeFigure> nodeFigures=new ArrayList<NodeFigure>();
	  for(int i=1; i<nodes.size();i++) {
		  nodeFigures.add(nodes.get(i).getNode());
	  }
	  return nodeFigures.iterator();
  }
  
}
