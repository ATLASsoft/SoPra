package de.hohenheim.modell;

import de.hohenheim.view.FigureFactory;
import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.node.NodeFigure;

public class Node {
  
  NodeFigure node;
  State state;
  String name;
  public Node(NodeMap map, String nodeName, int x, int y, int width, int height) {
	  node = FigureFactory.createNode(map, nodeName, x, y, width, height, this);
	  name = nodeName;
	  state = new State(this);
  }
  
  public NodeFigure getNodeFigure() {
	  return this.node;
  }
  
  public State getState() {
	  return this.state;
  }
  
  public String toString() {
	  return this.name;
  }
}
