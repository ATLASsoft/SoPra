package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.List;

import de.atlassoft.model.Node;

class Vertex {

	private Node modelObject;
	private List<Edge> outgoingEdges;
	private boolean blocked;
	
	Vertex(Node modelObject) {
		this.modelObject = modelObject;
		outgoingEdges = new ArrayList<>();
	}
	
	void addOutgoingEdge(Edge edge) {
		outgoingEdges.add(edge);
	}
	
	List<Edge> getOutgoingEdges() {
		return outgoingEdges;
	}
	
	boolean isBlocked() {
		return blocked;
	}
	
	void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	
	Node getModelObject() {
		return modelObject;
	}
	
	@Override
	public String toString() {
		return "ID: " + modelObject.getName();
	}
}