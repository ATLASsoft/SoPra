package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.List;

import de.atlassoft.model.Node;
import de.hohenheim.view.mobile.TrainFigure;
import de.hohenheim.view.mobile.animation.SimpleWalkToAnimator;

public class DashThroughStrategy extends PathFindingStrategy {

	/**
	 * Security margin in milliseconds;
	 */
	private static final int SECURITY_MARGIN = 5000;
	
	private TrainAgent agent;
	private TrainFigure figure;
	private List<Node> alternativeRoute;
	private Node blockedNode;
	private TrainAgent blockingAgent;
	private Graph g;
	
	protected DashThroughStrategy(TrainAgent agent, Graph graph, Node currentPosition, TrainAgent blockingAgent, Node blockedNode) {
		this.agent = agent;
		this.g = graph;
		this.figure = agent.getTrainFigure();
		this.blockedNode = blockedNode;
		this.blockingAgent = blockingAgent;
		
		List<Node> currentPath = agent.getCurrentPath();
		alternativeRoute = currentPath.subList(
				currentPath.indexOf(currentPosition), currentPath.size());
	}
	
	
	@Override
	long getCosts() {
		long fromAtBlockO = blockedNode.getState().getFromRequest(blockingAgent);
		long tillAtBlockA = blockedNode.getState().getTillRequest(agent);
		
		
		
		// agent is too late to dash through
		if (tillAtBlockA > fromAtBlockO + SECURITY_MARGIN) {
			return Long.MAX_VALUE;
		}
		
		// do not choose this strategy, if next node of agent is a station,
		// unless its the last station for complexity reasons
		if (blockedNode.equals(alternativeRoute.get(alternativeRoute.size() - 1))) {
			Node[] stationsA = agent.getSchedule().getStations();
			if (!blockedNode.equals(stationsA[stationsA.length - 1])) {
				return Long.MAX_VALUE;
			}
			// agent will disappear at the blockedNode
			else {
				return 0;
			}
		}
		
		// strategy is impossible when the next node of agent is in the
		// direction the other agent is coming from
		List<Node> curPathO = blockingAgent.getCurrentPath();
		Node nextAfterBlockA = alternativeRoute.get(1 + alternativeRoute.indexOf(blockedNode));
		
		int indexLastNodeO = curPathO.indexOf(blockedNode) - 1;
		
		// other agent is still at the first position
		if (indexLastNodeO < 0) {
			return Long.MAX_VALUE;
		}
		
		Node lastNodeO = curPathO.get(indexLastNodeO);
		
		if (nextAfterBlockA.equals(lastNodeO)) {
			return Long.MAX_VALUE;
		}
		
		// check whether all relevant nodes are free
		List<Node> nodesToCheck = new ArrayList<>(5);
		for (Edge e : g.getVertex(nextAfterBlockA).getOutgoingEdges()) {
			nodesToCheck.add(e.getEnd().getModelObject());
		}
		nodesToCheck.remove(blockedNode); // seperate check for this node since the conditions differ
		
		long now = agent.getSimTime();
		boolean allClear = true;
		long fromAtNextAfterBlockA = nextAfterBlockA.getState().getFromRequest(agent);
		
		for (Node n : nodesToCheck) {
			if (!n.getState().freeBetween(now, fromAtNextAfterBlockA)) {
				allClear = false;
			}
		}
		
		if (allClear) {
			return 0;
		} 
		
		// surrounding nodes are not unblocked
		else {
			return Long.MAX_VALUE;
		}
	}

	
	
	
	
	@Override
	SimpleWalkToAnimator execute() {
		
		agent.setCurrentPath(alternativeRoute);
		
		// no need to update reservations since the path is the same, only cut off
		//
		SimpleWalkToAnimator anim = figure.walkAlong(transformPath(alternativeRoute));
		anim.setTimeLapse(agent.getTimeLapse());
		anim.ignoreNode(blockedNode);
		figure.startAnimation();
		
		System.out.println("agent " + agent.getID() + " choose DashThroughStrategy");
		return anim;
	}

	
	
}
