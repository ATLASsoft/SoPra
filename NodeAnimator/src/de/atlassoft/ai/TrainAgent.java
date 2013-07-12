package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.atlassoft.model.Node;
import de.atlassoft.model.Schedule;
import de.atlassoft.model.State;
import de.atlassoft.model.TrainRideStatistic;
import de.hohenheim.view.FigureFactory;
import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.mobile.TrainFigure;
import de.hohenheim.view.mobile.animation.SimpleWalkToAnimator;
import de.hohenheim.view.node.NodeFigure;

//TODO: implementiern
public class TrainAgent implements Runnable {
	
	// constant attributes of this agent
	private final int id;
	private final TrainFigure figure;
	private final Schedule schedule;
	private final TrainRideStatistic statistic;
	
	private AIServiceImpl aiPort;
	private final Graph graph;
	private int timeLapse;
	private Thread runningThread;
	private SimpleWalkToAnimator anim;
	private Set<State> blockedState;
	private Set<State> requestedStates;
	
	/**
	 * The current path from lastStation to nextStation.
	 */
	private List<Node>currentPath;
	private Node lastStation;
	private Node nextStation;
	private Node currentPosition;
	
	
	protected TrainAgent(Graph graph, int id, Schedule schedule, AIServiceImpl aiPort) {
		this.statistic = new TrainRideStatistic(schedule.getScheme());
		this.schedule = schedule;
		this.id = id;
		this.graph = graph;
		this.aiPort = aiPort;
		this.blockedState = new HashSet<>();
		this.requestedStates = new HashSet<>();
		figure = FigureFactory.createTrainFigure(
				graph.getRailwaySystem().getNodeMap(),
				schedule.getStations()[0].getNodeFigure(),
				id,
				this);
	}
	
	
	
	public void block(State state) {
		state.setState(State.BLOCKED, this);
		blockedState.add(state);
	}
	
	public void unBlock(State state) {
		state.setState(State.UNBLOCKED, null);
		blockedState.remove(state);
	}
	
	
	
	protected void continueRide() { //TODO: wenn nur start/stop aufgerufen wird überflüssig, da des auch in der loop gemacht werden kann
		figure.startAnimation();
	}
	
	protected void pauseRide() {
		figure.stopAnimation();
	}
	
	protected synchronized void setTimeLapse(int timeLapse) {
		this.timeLapse = timeLapse;
		if (anim != null) {
			anim.setTimeLapse(timeLapse);
		}
	}
	
	protected synchronized int getTimeLapse() {
		return timeLapse;
	}
	
	protected long getSimTime() {
		return aiPort.getLoop().getSimTimeInMillis();
	}
	
	protected List<Node> getCurrentPath() {
		return currentPath;
	}
	
	protected void setCurrentPath(List<Node> path) {
		currentPath = path;
	}
	
	protected TrainFigure getTrainFigure() {
		return figure;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	protected TrainRideStatistic getTrainRideStatistic() {
		return statistic;
	}

	protected int getID() {
		return id;
	}
	
	
	
	@Override
	public void run() {
		runningThread = Thread.currentThread(); //TODO: nicht thread safe
		
		for (int i = 0; i < schedule.getStations().length - 1; i++) {
			lastStation = schedule.getStations()[i];
			nextStation = schedule.getStations()[i+1];
			
			// standard path finding algorithm
			setCurrentPath(graph.getShortestPath(lastStation, nextStation,
					schedule.getScheme().getTrainType().getTopSpeed()));
			currentPosition = currentPath.get(0);
			updateRequests(0);
			
			anim = figure.walkAlong(transformPath(currentPath));
			anim.setTimeLapse(timeLapse);
			figure.startAnimation();	
			
			// wait till animation ends or interrupted
			try {
				// wait and maybe elude till next station is reached 
				while (!atNode(nextStation)) {
					
					// acquire lock in order to wait
					synchronized (anim) {
					// wait till animation stops since it reached the next station or
					// the path is blocked
					anim.wait();
					}
					
					System.out.println("agent " + id + " has been notifed");
					
					// animation is finished, next station reached
					if (anim.isFinished()) {
						// compute delay and add it to the statistic
						long simTime = aiPort.getLoop().getSimTimeInMillis();
						int delay = (int) Math.round(((simTime - schedule.getArrivalTime(nextStation)) / 1000.0)); 
						statistic.addStop(figure.getNodeFigure().getModellObject(), delay);
						figure.stopAnimation();
						figure.clearAnimations();

						//TODO: warten (idle time)
					}
					
					// animation has been aborted before reaching goal,
					// search for best alternative strategy
					else {
						System.out.println("agent " + id + " seachres for alternative route");
						figure.stopAnimation();    
						figure.clearAnimations();
						
						// observe environment
						NodeFigure curPosFig = figure.getNodeFigure();
						if (curPosFig != null) { // agent is on node
							currentPosition = curPosFig.getModellObject();
						} else { // agent is on path
							//TODO: agent ist auf path
						}
						
						Node blockedNode = currentPath.get
								(1 + currentPath.indexOf(currentPosition));
						TrainAgent blockingAgent = blockedNode.getState().getBlocker();
						
						// strategy 1: wait till other agent has passed
//							List<Node> pathOther = blockingAgent.getCurrentPath();
//							Node nextAfterBlockO =
//									pathOther.get(1 + pathOther.indexOf(blockedNode));
//							if (!nextAfterBlockO.equals(currentPosition)) {
//								figure.waitFor(blockedNode.getState());
//								
//								// chop current path
//								currentPath = currentPath.subList(currentPath.indexOf(currentPosition), currentPath.size());
//								anim = figure.walkAlong(this.transformPath(currentPath));
//								anim.setTimeLapse(timeLapse);
//							}
						PathFindingStrategy strategy = new WaitForUnblockStrategy(this, currentPosition, blockingAgent, blockedNode, graph);
						PathFindingStrategy s2 = new AlternativeRouteStrategy(graph, this, blockedNode);
						
						if (strategy.getCosts() < s2.getCosts()) {
							anim = strategy.execute();
						} else {
							anim = s2.execute();
						}
						
						anim.setTimeLapse(timeLapse);
						// strategy 2
						// strategy 3 compute alternative route
						// strategy 4 draw back and let other pass
						
//							figure.startAnimation();
					}
				}
			} catch (InterruptedException e) {
				System.out.println("train agent " + id + " has been interrupted, terminates");
				return;
			}
			
			
		}
		removeFigure();//TODO: animation/warten bevor die züge verschwinden, sound
		aiPort.getLoop().activeAgents.remove(this);
		aiPort.getSimStat().addStatistic(statistic);
	}
	
	protected void terminate() {
		runningThread.interrupt(); //TODO: states cleanen
		removeFigure();
	}
	
	private void removeFigure() {
		figure.stopAnimation();
		figure.clearAnimations();
		for (State s : blockedState) {
			s.setState(State.UNBLOCKED, null);
		}
		final NodeMap map = graph.getRailwaySystem().getNodeMap();
		map.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				map.getAnimationLayer().remove(figure);
				map.getMobileObjects().remove("" + id);
			}
		});
	}
	
	/**
	 * Transforms list of {@link Node}s into a list of {@link NodeFigure}s. Also
	 * the first element of the list will be left out and therefore the size of
	 * new list is decreases by one.
	 * 
	 * @param walkingPath
	 *            The list of {@link Node}s to be transformed
	 * @return Transformed list
	 */
	private List<NodeFigure> transformPath(List<Node> walkingPath) {
		Iterator<Node> it = walkingPath.iterator();
		List<NodeFigure> figurePath = new ArrayList<>();
		it.next(); // skip first element
		while (it.hasNext()) {
			figurePath.add(it.next().getNodeFigure());
		}
		return figurePath;
	}

	private boolean atNode(Node node) {
		NodeFigure currentNodeFigure = figure.getNodeFigure();
		if (currentNodeFigure == null) {
			return false;
		}
		return (currentNodeFigure.getModellObject().equals(node));
	}
	
	protected void updateRequests(int offset) {
		List<Node> toWalk = currentPath.subList(
				currentPath.indexOf(currentPosition), currentPath.size());
		
		for (State state : requestedStates) {
			state.removeRequest(this);
		}
		requestedStates.clear();
		
		long simTime = aiPort.getLoop().getSimTimeInMillis();
		List<Double> costList = graph.getCosts(toWalk, schedule.getScheme()
				.getTrainType().getTopSpeed());
		double fromD;
		long fromL;
		State state;
		
		// add first
		state = toWalk.get(0).getState();
		state.request(this, simTime, simTime + offset);
		requestedStates.add(state);
		
		for (int i = 1; i < toWalk.size() - 1; i++) {
			state = toWalk.get(i).getState();
			fromD = simTime + offset + (costList.get(i) * 60 * 60 * 1000);
			fromL = ((long) fromD) + 1;
			state.request(this, fromL, fromL);
			requestedStates.add(state);
		}
		
		// add next station
		state = toWalk.get(toWalk.size() - 1).getState();
		fromD = simTime + offset + (costList.get(toWalk.size() - 1) * 60 * 60 * 1000);
		fromL = ((long) fromD) + 1;
		state.request(this, fromL, fromL + schedule.getIdleTime(nextStation));
		requestedStates.add(state);
	}
	
	
	@Override
	public String toString() {
		return "id: " + id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TrainAgent)) {
			return false;
		} else {
			return this.id == ((TrainAgent) obj).id;
		}
	}
	
	@Override
	public int hashCode() {
		return id;
	}

}
