package de.atlassoft.ai;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.atlassoft.model.Node;
import de.atlassoft.model.Path;

public final class StrategyFactory {

	private TrainAgent agent;
	private Graph graph;
	
	/**
	 * Create a new factory. This factory can only produce strategies for the
	 * specified agent on the specified graph.
	 * 
	 * @param agent
	 *            {@link TrainAgent} to produce strategies for
	 * @param graph
	 *            The {@link Graph} <code>agent</code> is in
	 */
	public StrategyFactory(TrainAgent agent, Graph graph) {
		this.agent = agent;
		this.graph = graph;
	}
	

	
	/**
	 * Returns the default strategy.
	 * 
	 * @param currentPosition {@link Node} the agent is currently at
	 * @param goal {@link Node} the agent wants to go to
	 * @return
	 */
	public PathFindingStrategy defaultStrategy(Node currentPosition, Node goal) {
		PathFindingStrategy s = new ShortestPathStrategy(
				graph,
				agent,
				currentPosition,
				goal,
				new HashSet<Node>(),	// do not ignore any nodes
				new HashSet<Path>());   // or paths
		
		s.getCosts();
		
		return s;
	}
	
	
	public PathFindingStrategy nodeBlockedStrategy(Node currentPosition, TrainAgent blockingAgent, Node blockedNode, Node goal) {
		// wait till other agent has passed
		PathFindingStrategy waitA = new WaitForUnblockStrategy(agent, graph, currentPosition, blockingAgent, blockedNode);
		long waitCostAgent = waitA.getCosts();
		
		// dash through
		PathFindingStrategy dash = new DashThroughStrategy(agent, graph, currentPosition, blockingAgent, blockedNode);
		long dashCostAgent = dash.getCosts();
		
		// find alternative route
		HashSet<Node> ignoredNodes = new HashSet<>();
		ignoredNodes.add(blockedNode);
		PathFindingStrategy altRouteA = new ShortestPathStrategy(
				graph,
				agent,
				currentPosition,
				goal,
				ignoredNodes,	
				new HashSet<Path>());   // do not ignore any paths
		long altRouteCostAgent = altRouteA.getCosts();
		
		// draw back
		PathFindingStrategy drawBackA = new DrawBackStrategy(agent, graph, currentPosition, goal, blockingAgent);
		long drawBackCostAgent = drawBackA.getCosts();

		
		// alternative strategies for the blocking agent:
		PathFindingStrategy bestStrategyOther = null;
		long bestStratOtherCost = 0;
		List<Node> curPathOther = blockingAgent.getCurrentPath();
		Long tillAtBlock = blockedNode.getState().getTillRequest(blockingAgent);
		if (curPathOther != null && tillAtBlock != null) {
			Node goalOther = curPathOther.get(curPathOther.size() - 1);
			
			// time the other agent needs to reach the blocked node, must be added to the costs
			// of the algorithms of the other agent
			long timeTillAtBlockO = tillAtBlock - agent.getSimTime();
			
			// other agent alternative route
			HashSet<Node> ignoreNodesOther = new HashSet<>();
			ignoreNodesOther.add(currentPosition);
			PathFindingStrategy altRouteO = new ShortestPathStrategy(graph, blockingAgent, blockedNode, goalOther, ignoreNodesOther, new HashSet<Path>());
			long altRouteCostOther = altRouteO.getCosts();
			
			// draw back other
			PathFindingStrategy drawBackO = new DrawBackStrategy(blockingAgent, graph, blockedNode, goalOther, agent);
			long drawBackCostOther = drawBackO.getCosts();
			
			
			if (altRouteCostOther < drawBackCostOther) {
				bestStrategyOther = altRouteO;
				bestStratOtherCost = altRouteCostOther;
			} else {
				bestStrategyOther = drawBackO;
				bestStratOtherCost = drawBackCostOther;
			}
			
			if (bestStratOtherCost < Long.MAX_VALUE) {
				bestStratOtherCost += 2 * timeTillAtBlockO;
			}
		}
		
		
		// use treemap to find element with fewest costs
		TreeMap<Long, PathFindingStrategy> map = new TreeMap<>();
		map.put(waitCostAgent, waitA);
		map.put(dashCostAgent, dash);
		map.put(altRouteCostAgent, altRouteA);
		map.put(drawBackCostAgent, drawBackA);
		
		Entry<Long, PathFindingStrategy> bestStrategyAgent = map.firstEntry();
		
//		if (bestStrategyOther == null || bestStrategyAgent.getKey() < bestStratOtherCost) {
//			return bestStrategyAgent.getValue();
//		} else {
//			blockingAgent.applyStrategy(bestStrategyOther);
//			return waitA;
//		}
	
		return bestStrategyAgent.getValue();
	}
	
	
	/**
	 * Returns the best strategy when the agent is facing a blocked path.
	 * 
	 * @param blockingAgent
	 *            The {@link TrainAgent} that blocks the path
	 * @param blockedPath
	 *            The blocked {@link Path}
	 * @param currentPosition
	 *            The {@link Node} this {@link TrainAgent} is currently at
	 * @param goal
	 *            The {@link Node} this {@link TrainAgent} wants to go to
	 * 
	 * @return the best {@link PathFindingStrategy}
	 */
	public PathFindingStrategy pathBlockedStrategy(
			TrainAgent blockingAgent,
			Path blockedPath,
			Node currentPosition,
			Node goal) {
		
		PathFindingStrategy s1 = new WaitForPathStrategy(agent, blockingAgent, blockedPath);
		long costS1 = s1.getCosts();

		PathFindingStrategy s2 = new FreeRouteStrategy(graph, agent, currentPosition, goal);
		long costS2 = s2.getCosts();
		
		//TODO: kosten bei s1 mit geschwindigkeitsdifferenz gewichten
		if (costS1 < costS2) {
			return s1;
		} else {
			return s2;
		}
		
	}
	
}
