package de.hohenheim.modell.dijkstra;

import de.hohenheim.view.path.PathFigure;

public class Edge {
  double distance;
  PathFigure pathFigure;
  Node start;
  Node end;
  
  public Edge(PathFigure path, Node start, Node end) {
	  this.pathFigure=path;
	  this.distance = path.getDistance();	  
	  this.start=start;
	  this.end=end;
  }
  
  public PathFigure getPathFigure() {
	  return this.pathFigure;	  
  }
  
  public double getDistance() {
	  return this.distance;
  }
  
  public Node getStartNode() {
	  return this.start;
  }
  
  public Node getEndNode() {
	  return this.end;
  }
}
