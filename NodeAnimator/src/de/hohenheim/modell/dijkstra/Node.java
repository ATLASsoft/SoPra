package de.hohenheim.modell.dijkstra;

import de.hohenheim.view.node.NodeFigure;

public class Node {
  float distance   = -1;
  Node predecessor = null;
  NodeFigure node;
  
  public Node(NodeFigure node) {
	  this.node=node;
  }
  
  public NodeFigure getNode() {
	  return this.node;
  }
  
  public float getDistance() {
	  return this.distance;
  }
  
  public Node getPredecessor() {
	  return this.predecessor;
  }
  
  public void setPredecessor(Node node) {
	  this.predecessor=node;
  }
}
