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
	private Node blockedNode;//
	private TrainAgent blockingAgent;//
	private Graph g;
	
	protected DashThroughStrategy(TrainAgent agent, Graph graph, Node currentPosition) {
		this.agent = agent;
		this.g = graph;
		this.figure = agent.getTrainFigure();
		List<Node> currentPath = agent.getCurrentPath();
		alternativeRoute = currentPath.subList(
				currentPath.indexOf(currentPosition), currentPath.size());
	}
	
	
	@Override
	long getCosts() {
		long fromAtBlockO = blockedNode.getState().getFromRequest(blockingAgent);
		long tillAtBlockA = blockedNode.getState().getTillRequest(agent);
		
		// do not choose this strategy, if next node of agent is a station,
		// unless its the last station for complexity reasons
		if (blockedNode.equals(alternativeRoute.get(alternativeRoute.size() - 1))) {
			Node[] stationsA = agent.getSchedule().getStations();
			if (!blockedNode.equals(stationsA[stationsA.length - 1])) {
				return Long.MAX_VALUE;
			}
			// agent will disappear at the blockedNode
			else {
				if (tillAtBlockA + SECURITY_MARGIN < fromAtBlockO) {
					return this.routeConst(alternativeRoute, g, agent);
				}
			}
		}
		
		// strategy is impossible when the next node of agent is in the
		// direction the other agent is coming from
		List<Node> curPathO = blockingAgent.getCurrentPath();
		Node nextAfterBlockA = alternativeRoute.get(1 + alternativeRoute.indexOf(blockedNode));
		Node lastNodeO = curPathO.get(curPathO.indexOf(blockedNode) - 1);
		if (nextAfterBlockA.equals(lastNodeO)) {
			return Long.MAX_VALUE;
		}
		
		
		List<Node> nodesToCheck = new ArrayList<>(5);
		for (Edge e : g.getVertex(nextAfterBlockA).getOutgoingEdges()) {
			nodesToCheck.add(e.getEnd().getModelObject());
		}
		nodesToCheck.remove(blockedNode); // seperate check for this node since the conditions differ
		
		boolean allClear = true;
		long fromAtNextAfterBlockA = nextAfterBlockA.getState().getFromRequest(agent);
		
		
		for (Node n : nodesToCheck) {
			
		}
		
		if (tillAtBlockA + SECURITY_MARGIN < fromAtBlockO) {
			return this.routeConst(alternativeRoute, g, agent);
		} else {
			return Long.MAX_VALUE;
		}
		
	}

	
	
	
	
	@Override
	SimpleWalkToAnimator execute() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
