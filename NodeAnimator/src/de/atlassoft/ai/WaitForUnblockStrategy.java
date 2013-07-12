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
		nextAfterBlockO =
				pathOther.get(1 + pathOther.indexOf(blockedNode));
	}
	
	@Override
	protected long getCosts() {
		if (nextAfterBlockO.equals(currentPosition)) {
			return Long.MAX_VALUE;
		}
		
		long blockedTill = blockedNode.getState().getTillRequest(blockingAgent);
		long simTime = agent.getSimTime();
		
		List<Double> costList = g.getCosts(alternativeRoute,
				agent.getSchedule().getScheme().getTrainType().getTopSpeed());
		
		long routeCost = Math.round(costList.get(costList.size() - 1) * 60 *60 * 1000 );
		
		
		
		return (blockedTill - simTime) + routeCost;
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
		System.out.println("agent 1 choose WaitForUnblockStrategy");
		return anim;
	}

}
