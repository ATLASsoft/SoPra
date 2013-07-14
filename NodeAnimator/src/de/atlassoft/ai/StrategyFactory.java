package de.atlassoft.ai;

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
	
	public static PathFindingStrategy defaultStrategy() {
		return null;
	}
	
	/**
	 * Returns the best strategy when the agent is facing a blocked path.
	 * @param blockingAgent
	 * @param blockedPath
	 * @return
	 */
	public PathFindingStrategy pathBlockedStrategy(TrainAgent blockingAgent, Path blockedPath) {
		PathFindingStrategy s1 = new WaitForPathStrategy(agent, graph, blockingAgent, blockedPath);
		long costS1 = s1.getCosts();
		//TODO: alternativroute strategie
		
		return s1;
	}
	
}
