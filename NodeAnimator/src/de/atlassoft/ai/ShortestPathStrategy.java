package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.atlassoft.model.Node;
import de.atlassoft.model.Path;
import de.hohenheim.view.mobile.TrainFigure;
import de.hohenheim.view.mobile.animation.SimpleWalkToAnimator;

/**
 * Simply finds the shortest path. This strategy does not care whether a node or path is blocked
 * or not. All paths/nodes are considered but the once passed by the ignoreSets.
 * 
 * @author Alexander Balogh
 *
 */
public class ShortestPathStrategy extends PathFindingStrategy {

	private Graph g;
	private Node currentPosition;
	private Node goal;
	private TrainFigure figure;
	private TrainAgent agent;
	
	private Set<Node> ignoreNodes;
	private Set<Path> ignorePaths;
	
	private List<Node> route;

	public ShortestPathStrategy(
			Graph graph, 
			TrainAgent agent,
			Node currentPosition,
			Node goal,
			Set<Node> ignoreNodes,
			Set<Path> ignorePaths) {
		
		this.g = graph;
		this.agent = agent;
		this.currentPosition = currentPosition;
		this.goal = goal;
		
		this.figure = agent.getTrainFigure();
		this.ignoreNodes = ignoreNodes;
		this.ignorePaths = ignorePaths;
		
		route = new ArrayList<>(15);
	}
	
	
	@Override
	long getCosts() {
		// compute alternative route and its cost
		double[] dist = new double[g.getVertexes().length];
		Vertex[] predecessor = new Vertex[g.getVertexes().length];
		g.sssp_Dijkstra_ignoreSet(currentPosition, agent.getSchedule()
				.getScheme().getTrainType().getTopSpeed(), predecessor, dist,
				ignoreNodes, ignorePaths);
		
		// goal could not be reached, no alternative route exists
		if (predecessor[g.getVertex(goal).id] == null) {
			return Long.MAX_VALUE;
		}		
		
		// construct walking path from predecessor array
		route.add(goal);

		int i = 0;
		Vertex pre;
		while ((pre = predecessor[g.getVertex(route.get(i)).id]) != null) {
			route.add(pre.getModelObject());
			i++;
		}

		Collections.reverse(route);

		double routeCost = dist[g.getVertex(goal).id] * 60 * 60 * 1000;
		return Math.round(routeCost);
	}

	@Override
	SimpleWalkToAnimator execute() throws InterruptedException {
		
		agent.setCurrentPath(route);
		agent.updateRequests(0,
				agent.getSchedule().getIdleTime(route.get(route.size() - 1)));
		
		SimpleWalkToAnimator anim = figure.walkAlong(transformPath(route));
		anim.setTimeLapse(agent.getTimeLapse());
		figure.startAnimation();
		System.out.println("agent " + agent.getID() + " choose ShortestRouteStrategy");
		return anim;
	}

}
