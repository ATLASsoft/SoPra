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
			Graph graph,
			Node currentPosition,
			TrainAgent blockingAgent,
			Node blockedNode) {
		
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
		
		// block is target of other agent
		if (nextAfterBlockO == null) {
			Node[] stationsOther = blockingAgent.getSchedule().getStations();
			
			// if it is the last station it does not matter
			if (!stationsOther[stationsOther.length - 1].equals(blockedNode)) {

				Node nextStationO = null;
				for (int i = 0; i< stationsOther.length; i++) {
					if (stationsOther[i].equals(blockedNode)) {
						nextStationO = stationsOther[i + 1];
					}
				}
				
				// target was no station
				if (nextStationO == null) {
					return Long.MAX_VALUE;
				}
				
				nextAfterBlockO = g.getShortestPath(blockedNode,
						nextStationO, 
						blockingAgent.getSchedule().getScheme().getTrainType().getTopSpeed()).get(1);
				if (nextAfterBlockO.equals(currentPosition)) {
					return Long.MAX_VALUE;
				}
			}
		}
		
		long blockedTill = blockedNode.getState().getTillRequest(blockingAgent);
		long simTime = agent.getSimTime();
		
		return (blockedTill - simTime);
	}

	@Override
	SimpleWalkToAnimator execute() {
		
		
		// wait till next node is unblocked
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
