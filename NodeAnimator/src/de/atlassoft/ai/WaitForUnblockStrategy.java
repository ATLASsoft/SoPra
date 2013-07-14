package de.atlassoft.ai;

import java.util.List;

import de.atlassoft.model.Node;
import de.hohenheim.view.mobile.TrainFigure;
import de.hohenheim.view.mobile.animation.SimpleWalkToAnimator;

public class WaitForUnblockStrategy extends PathFindingStrategy {

	private TrainFigure figure;
	private TrainAgent agent;
	private Node currentPosition;
	private List<Node> alternativeRoute;
	private TrainAgent blockingAgent;
	private Node blockedNode;
	private Node nextAfterBlockO;
	private Graph g;
	
	public WaitForUnblockStrategy(
			TrainAgent agent,
			Node currentPosition,
			TrainAgent blockingAgent,
			Node blockedNode,
			Graph graph) {
		
		this.agent = agent;
		this.currentPosition = currentPosition;
		this.blockingAgent = blockingAgent;
		this.blockedNode = blockedNode;
		this.g = graph;
		
		this.figure = agent.getTrainFigure();
		
		List<Node> currentPath = agent.getCurrentPath();
		this.alternativeRoute = currentPath.subList(
				currentPath.indexOf(currentPosition), currentPath.size());

		List<Node> pathOther = blockingAgent.getCurrentPath();
		
		//TODO: block ist station vom anderen agent und nextAfterBlock (im neuen pfad) ist currentPosition
		// set the next node of the other agent after the block or null if the
		// blocked node is a station of the other agent (in this case it's the
		// last node in the current path list of the other agent)
		int indexOfNextAfterBlockO = 1 + pathOther.indexOf(blockedNode);
		if (indexOfNextAfterBlockO < pathOther.size()) {
			nextAfterBlockO =
					pathOther.get(indexOfNextAfterBlockO);
		} else {
			nextAfterBlockO = null;
		}
	}
	
	@Override
	protected long getCosts() {
		if (nextAfterBlockO != null && nextAfterBlockO.equals(currentPosition)) {
			return Long.MAX_VALUE;
		}
		
		long blockedTill = blockedNode.getState().getTillRequest(blockingAgent);
		long simTime = agent.getSimTime();
		
		return (blockedTill - simTime) + this.routeCost(alternativeRoute, g, agent);
	}

	@Override
	SimpleWalkToAnimator execute() {
		// wait till next node is blocked
		figure.waitFor(blockedNode.getState());
		
		agent.setCurrentPath(alternativeRoute);
		
		// continue 
		SimpleWalkToAnimator anim = figure.walkAlong(transformPath(alternativeRoute));
		anim.setTimeLapse(agent.getTimeLapse());
		figure.startAnimation();
		System.out.println("agent " + agent.getID() + " choose WaitForUnblockStrategy");
		return anim;
	}

}
