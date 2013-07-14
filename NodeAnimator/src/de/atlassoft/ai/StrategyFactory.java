package de.atlassoft.ai;

import java.util.HashSet;

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
		PathFindingStrategy s1 = new WaitForUnblockStrategy(agent, currentPosition, blockingAgent, blockedNode, graph);
		long costWaitAgent = s1.getCosts();
		
		// find alternative route
		HashSet<Node> ignoredNodes = new HashSet<>();
		ignoredNodes.add(blockedNode);
		PathFindingStrategy s2 = new ShortestPathStrategy(
				graph,
				agent,
				currentPosition,
				goal,
				ignoredNodes,	
				new HashSet<Path>());   // do not ignore any paths
		long costAltRouteAgent = s2.getCosts();
		
		// draw back
		PathFindingStrategy s3 = new DrawBackStrategy(agent, graph, currentPosition, goal, blockingAgent);
		long costDrawBackAgent = s3.getCosts();
		
		
		
		
		
		
		
		if (costWaitAgent < costAltRouteAgent) {
			if (costWaitAgent < costDrawBackAgent) {
				return s1;
			} else {
				return s3;
			}
		} else {
			if (costAltRouteAgent < costDrawBackAgent) {
				return s2;
			} else {
				return s3;
			}
			
		}
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
		
		PathFindingStrategy s1 = new WaitForPathStrategy(agent, graph, blockingAgent, blockedPath);
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
