package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.atlassoft.model.Node;
import de.hohenheim.view.mobile.TrainFigure;
import de.hohenheim.view.mobile.animation.SimpleWalkToAnimator;
import de.hohenheim.view.node.NodeFigure;

/**
 * Finds the shortest route to the next station without using currently blocked paths
 * or nodes.
 * 
 * @author Alexander Balogh
 *
 */
public class FreeRouteStrategy extends PathFindingStrategy {

	private Graph g;
	private Node currentPosition;
	private Node goal;
	private TrainFigure figure;
	private TrainAgent agent;
	
	private List<NodeFigure> alternativeFigureRoute;
	private List<Node> alternativeRoute;
	
	public FreeRouteStrategy(Graph graph, TrainAgent agent, Node currentPosition, Node goal) {
		this.g = graph;
		this.agent = agent;
		this.goal = goal;
		this.currentPosition = currentPosition;
		this.figure = agent.getTrainFigure();
		alternativeRoute = new ArrayList<>(15);
	}

	@Override
	protected long getCosts() {
		
		// compute alternative route and its cost
		double[] dist = new double[g.getVertexes().length];
		Vertex[] predecessor = new Vertex[g.getVertexes().length];
		g.sssp_Dijkstra_considerBlocks(
				currentPosition,
				agent.getSchedule().getScheme().getTrainType().getTopSpeed(),
				predecessor,
				dist);
		
		// goal could not be reached, no alternative route exists
		if (predecessor[g.getVertex(goal).id] == null) {
			return Long.MAX_VALUE;
		}
		
		
		// construct walking path from predecessor array
		List<NodeFigure> walkingPath = new ArrayList<>();
		walkingPath.add(goal.getNodeFigure());
		alternativeRoute.add(goal);
		
		int i = 0;
		Vertex pre;
		while ((pre = predecessor[g.getVertex(walkingPath.get(i).getModellObject()).id]) != null) {
			walkingPath.add(pre.getModelObject().getNodeFigure());
			alternativeRoute.add(pre.getModelObject());
			i++;
		}
		
		Collections.reverse(walkingPath);
		Collections.reverse(alternativeRoute);
		walkingPath.remove(0);
		
		double alternativeRouteCost = dist[g.getVertex(goal).id] * 60 * 60 * 1000;
		this.alternativeFigureRoute = walkingPath;
		
		double fastestTime = g.getShortestTravelTime(currentPosition, goal,
				agent.getSchedule().getScheme().getTrainType().getTopSpeed()) * 60 * 60 * 1000;
		
		return Math.round(alternativeRouteCost - fastestTime);
	}

	@Override
	SimpleWalkToAnimator execute() {
		if (alternativeFigureRoute == null) {
			getCosts();
		}
		
		agent.setCurrentPath(alternativeRoute);
		agent.updateReservations(0,
				agent.getSchedule().getIdleTime(alternativeRoute.get(alternativeRoute.size() - 1)));
		
		SimpleWalkToAnimator anim = figure.walkAlong(alternativeFigureRoute);
		anim.setTimeLapse(agent.getTimeLapse());
		figure.startAnimation();
		System.out.println("agent " + agent.getID() + " choose AlternativeRouteStrategy");
		return anim;
		
	}

}
