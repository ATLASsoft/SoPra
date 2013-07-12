package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.List;

import de.atlassoft.model.Node;
import de.atlassoft.model.State;

class Vertex {

	private Node modelObject;
	private List<Edge> outgoingEdges;
	protected int id;
	
	Vertex(Node modelObject, int id) {
		this.modelObject = modelObject;
		this.id = id;
		outgoingEdges = new ArrayList<>();
	}
	
	void addOutgoingEdge(Edge edge) {
		outgoingEdges.add(edge);
	}
	
	List<Edge> getOutgoingEdges() {
		return outgoingEdges;
	}
	
	boolean isBlocked() {
		if (modelObject.getState().getState() == State.BLOCKED) {
			return true;
		} else {
			return false;
		}
	}
	
	Node getModelObject() {
		return modelObject;
	}
	
	@Override
	public String toString() {
		return "id: " + id + " Name: " + modelObject.getName();
	}
}
