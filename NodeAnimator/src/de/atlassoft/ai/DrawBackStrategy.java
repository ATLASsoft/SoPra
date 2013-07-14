package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import de.atlassoft.model.Node;
import de.hohenheim.view.mobile.TrainFigure;
import de.hohenheim.view.mobile.animation.SimpleWalkToAnimator;
import de.hohenheim.view.node.NodeFigure;

public class DrawBackStrategy extends PathFindingStrategy {

	private TrainAgent agent;
	private TrainFigure figure;
	private TrainAgent blockingAgent;
	private Graph g;
	private Node currentPosition;
	private List<Node> pathToSafeSpot;
	
	
	public DrawBackStrategy(TrainAgent agent, Graph graph, Node currentPosition, TrainAgent blockingAgent) {
		this.agent = agent;
		this.figure = agent.getTrainFigure();
		this.g = graph;
		this.currentPosition = currentPosition;
		this.blockingAgent = blockingAgent;
	}
	
	@Override
	long getCosts() {
		// avoid this nodes in order to get out of the other agents way
		List<Node> tabooNodes = blockingAgent.getCurrentPath();
		
		// compute the closet safe spot i.e. the closest node to fall back to
		double[] dist = new double[g.getVertexes().length];
		Vertex[] predecessor = new Vertex[g.getVertexes().length];
		
		Node safeSpot = closestSafeSpot(
				currentPosition,
				agent.getSchedule().getScheme().getTrainType().getTopSpeed(),
				predecessor,
				dist,
				tabooNodes);
		
		// no safe spot could be found
		if (safeSpot == null) {
			return Long.MAX_VALUE;
		}
		
		List<Node> pathToSafeSpot = new ArrayList<>();
		List<NodeFigure> figurePathToSafeSpot = new ArrayList<>();
		pathToSafeSpot.add(safeSpot);
		figurePathToSafeSpot.add(safeSpot.getNodeFigure());

		int i = 0;
		Vertex pre;
		while ((pre = predecessor[g.getVertex(pathToSafeSpot.get(i)).id]) != null) {
			pathToSafeSpot.add(pre.getModelObject());
			figurePathToSafeSpot.add(pre.getModelObject().getNodeFigure());
			i++;
		}
		
		Collections.reverse(pathToSafeSpot);
		Collections.reverse(figurePathToSafeSpot);
		figurePathToSafeSpot.remove(0);
		
		
		
		// original path from the current node through the blocked node to the next station
		List<Node> currentPath = agent.getCurrentPath();
		List<Node> originalPathToGoal = currentPath.subList(
				currentPath.indexOf(currentPosition), currentPath.size());
		
		
		this.pathToSafeSpot = pathToSafeSpot;
		
		long toSafeSpotCost = Math.round(dist[g.getVertex(safeSpot).id] * 60 * 60 * 1000);
		long simTime = agent.getSimTime();
		
		// time point agent can leave the safe spot if other agent can move as planned
		Long waitTill = pathToSafeSpot.get(pathToSafeSpot.size() - 2)
				.getState().getTillRequest(blockingAgent);
		if (waitTill == null) { //TODO: kein request
			
		}
		long waitCost = waitTill - simTime;
		
		// costs as the time till both agents are at the safe spot + the time back
		// to the initial point + time from the initial point to the next station
		// on the normal route
		return Math.max(toSafeSpotCost, waitCost) + toSafeSpotCost
				+ routeCost(originalPathToGoal, g, agent);
		
	}

	@Override
	SimpleWalkToAnimator execute() {
		if (pathToSafeSpot == null) {
			getCosts();
		}
		
		SimpleWalkToAnimator anim = figure.walkAlong(transformPath(pathToSafeSpot));
		anim.setTimeLapse(agent.getTimeLapse());
		figure.startAnimation();
		
//		figure.waitFor(pathToSafeSpot.get(pathToSafeSpot.size() - 2).getState()); //TODO: waitfor
//		Collections.reverse(pathToSafeSpot);
//		figure.walkAlong(transformPath(pathToSafeSpot));
		
		System.out.println("agent " + agent.getID() + " choose DrawBackStrategy");
		return anim;
	}

	/**
	 * 
	 * @param source
	 * @param trainTopSpeed
	 * @param predecessor
	 * @param dist
	 * @param tabooNodes
	 * @return
	 */
	protected Node closestSafeSpot(Node source, double trainTopSpeed,
			Vertex[] predecessor, double[] dist, List<Node> tabooNodes) {
		
		Vertex start = g.vertexMap.get(source);
		
		// init dist array
		for (int i = 0; i < dist.length; i++) {
			dist[i] = Double.POSITIVE_INFINITY;
		}

		// init open and closed list
		PriorityQueue<Vertex> openList = new PriorityQueue<>(dist.length, g.new VertexComperator(dist));
		boolean[] closedList = new boolean[g.vertexes.length];
		dist[start.id] = 0.0;
		openList.offer(start);
		
		// compute shortest paths from start
		Vertex v, u;
		double d;
		while (!openList.isEmpty()) {
			v = openList.poll();
			closedList[v.id] = true;;
			for (Edge e : v.getOutgoingEdges()) {
				u = e.getEnd();

				if (closedList[u.id]) {
					continue;
				}
				if (e.isBlocked() || u.isBlocked()) {
					continue;
				}
				
				d = e.getActualCost(trainTopSpeed) + dist[v.id];
				if (d < dist[u.id]) {
					openList.remove(u);
					dist[u.id] = d;
					openList.offer(u);
					predecessor[u.id] = v;
				}
				
				if (!tabooNodes.contains(u.getModelObject())) {
					return u.getModelObject();
				}
			}
		}
	
		// no save spot could be found
		return null;
		
	}
	
	
	

}
