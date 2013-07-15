package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

import org.eclipse.draw2d.ColorConstants;

import de.atlassoft.model.Node;
import de.atlassoft.model.Path;
import de.hohenheim.view.mobile.TrainFigure;
import de.hohenheim.view.mobile.animation.Animator;
import de.hohenheim.view.mobile.animation.SimpleWalkToAnimator;
import de.hohenheim.view.node.NodeFigure;

public class DrawBackStrategy extends PathFindingStrategy {

	private TrainAgent agent;
	private TrainFigure figure;
	private TrainAgent blockingAgent;
	private Graph g;
	private Node currentPosition;
	private List<Node> pathToSafeSpot;
	private Node goal;
	private long actualToSafeSpotCost;
	
	public DrawBackStrategy(TrainAgent agent, Graph graph, Node currentPosition, Node goal, TrainAgent blockingAgent) {
		this.agent = agent;
		this.goal = goal;
		this.figure = agent.getTrainFigure();
		this.g = graph;
		this.currentPosition = currentPosition;
		this.blockingAgent = blockingAgent;
	}
	
	@Override
	long getCosts() {
		// avoid this nodes in order to get out of the other agents way
		List<Node> tabooNodes = blockingAgent.getCurrentPath();
		Node[] stationsO = blockingAgent.getSchedule().getStations();
		
		// if agent is not in the way of the other agent this strategy cannot be applied
		if (!tabooNodes.contains(currentPosition)) {
			return Long.MAX_VALUE;
		}
		
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
		
		// if the node agent would wait for in the safe spot a station and not the
		// last station do not use this strategy
		this.pathToSafeSpot = pathToSafeSpot;
		Node[] stationsOther = blockingAgent.getSchedule().getStations();
		Node nodeBeforeSpot = pathToSafeSpot.get(pathToSafeSpot.size() - 2); 
		if (nodeBeforeSpot.equals(tabooNodes.get(tabooNodes.size() - 1))
				&& !nodeBeforeSpot.equals(stationsOther[stationsOther.length - 1])) {
			return Long.MAX_VALUE;
		}
		
		
		long toSafeSpotCost = Math.round(dist[g.getVertex(safeSpot).id] * 60 * 60 * 1000);
		long simTime = agent.getSimTime();
		
		// time point agent can leave the safe spot if other agent can move as planned
		Long waitTill = pathToSafeSpot.get(pathToSafeSpot.size() - 2)
				.getState().getTillRequest(blockingAgent);
		if (waitTill == null) {
		}
		long toSafeOther = waitTill - simTime;
		actualToSafeSpotCost = Math.max(toSafeSpotCost, toSafeOther);
		
		// costs as the time till both agents are at the safe spot + the time back
		// to the initial point + time from the initial point to the next station
		// on the normal route
		return actualToSafeSpotCost + toSafeSpotCost;
		
	}

	@Override
	Animator execute() throws InterruptedException {
		System.out.println("agent " + agent.getID() + " choose DrawBackStrategy");
		if (pathToSafeSpot == null) {
			getCosts();
		}
		
		Animator anim = figure.walkAlong(transformPath(pathToSafeSpot));
		agent.setAnimator(anim);
		agent.setCurrentPath(pathToSafeSpot);
		agent.updateReservations(0, (int) actualToSafeSpotCost);
		anim.setTimeLapse(agent.getTimeLapse());
		figure.startAnimation();
		
		synchronized (agent.getSchedule()) {
			agent.getSchedule().wait();
			
			// collision while trying to reach safe spot
			if (!anim.isFinished()) {
				agent.wait = false;
				return (SimpleWalkToAnimator) anim;
			}
		}
		
		// wait till other train has passed by
		Long tillOther = pathToSafeSpot.get(pathToSafeSpot.size() - 2)
				.getState().getTillRequest(blockingAgent);
		long waitTime = 0;
		if (tillOther != null) {
			waitTime = tillOther - agent.getSimTime();
		}
		System.out.println(tillOther);
		System.out.println(agent.getSimTime());
		System.out.println(waitTime);
		if (waitTime > 0) {
			anim = figure.busy((int) waitTime, ColorConstants.red);
			anim.setTimeLapse(agent.getTimeLapse());
			agent.setAnimator(anim);
		}
		
		Object lock = figure.waitFor(pathToSafeSpot.get(pathToSafeSpot.size() - 2).getState());
		figure.startAnimation();
		
		// wait till state is clear, ignore other agents
		synchronized (lock) {
			lock.wait();
		}
		
		// find route to the next station
		PathFindingStrategy s = new ShortestPathStrategy(
				g, 
				agent,
				pathToSafeSpot.get(pathToSafeSpot.size() - 1),
				goal,
				new HashSet<Node>(),
				new HashSet<Path>());
		s.getCosts();
		
		return s.execute();
		
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
