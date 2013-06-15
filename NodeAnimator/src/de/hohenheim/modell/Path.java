package de.hohenheim.modell;

import de.hohenheim.view.FigureFactory;
import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.path.PathFigure;

public class Path {
  
	private State state;
	private PathFigure path;
	
	public Path(NodeMap map, Node start, Node end) {
		path = FigureFactory.createPath(map, start.getNodeFigure(), end.getNodeFigure(), this);
		state = new State(this);
	}
	
	public State getState() {
		return this.state;
	}
	
	public PathFigure getPathFigure() {
		return this.path;
	}
}
