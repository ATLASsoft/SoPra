package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.atlassoft.model.Blockable;
import de.atlassoft.model.Node;
import de.atlassoft.model.Path;
import de.atlassoft.model.Schedule;
import de.atlassoft.model.State;
import de.atlassoft.model.TrainRideStatistic;
import de.hohenheim.view.FigureFactory;
import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.mobile.TrainFigure;
import de.hohenheim.view.mobile.animation.Animator;
import de.hohenheim.view.node.NodeFigure;

/**
 * Represents the model and the logic of a train.
 * 
 * @author Alexander Balogh
 *
 */
public class TrainAgent implements Runnable {
	
	private static enum Status {STATION_REACHED, PATH_BLOCKED, UNKNOWN, }
	
	// constant attributes of this agent
	private final int id;
	private final TrainFigure figure;
	private final Schedule schedule;
	private final TrainRideStatistic statistic;
	
	private final AIServiceImpl aiPort;
	private final StrategyFactory factory;
	private final Graph graph;
	private int timeLapse;
	private Thread executingThread;
	private Animator anim;
	private Set<State> blockedState;
	private Set<State> requestedStates;
	
	private Status status;
	/**
	 * The current path from lastStation to nextStation.
	 */
	private List<Node>currentPath;
	private Node lastStation;
	private Node nextStation;
	private Node currentPosition;
	private Blockable blockade;
	
	
	protected TrainAgent(Graph graph, int id, Schedule schedule, AIServiceImpl aiPort) {
		this.statistic = new TrainRideStatistic(schedule.getScheme());
		this.schedule = schedule;
		this.id = id;
		this.graph = graph;
		this.aiPort = aiPort;
		this.status = Status.UNKNOWN;
		this.blockedState = new HashSet<>();
		this.requestedStates = new HashSet<>();
		this.factory = new StrategyFactory(this, graph);
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
	
	
	
	protected void continueRide() { //TODO: wenn nur start/stop aufgerufen wird �berfl�ssig, da des auch in der loop gemacht werden kann
		figure.startAnimation();
	}
	
	protected void pauseRide() {
		figure.stopAnimation();
	}
	
	protected void terminate() {
		executingThread.interrupt(); //TODO: states cleanen
		removeFigure();
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
	
	
	public void blockadeDetected(Blockable blockade) {
		this.blockade = blockade;
		if (blockade instanceof Path) {
			status = Status.PATH_BLOCKED;
		} else {
//			
		}
		
		
		synchronized (schedule) {
			schedule.notifyAll();
		}
	}
	
	public void targetReached() {
		status = Status.STATION_REACHED; //TODO: anderes target als station
		synchronized(schedule) {
			schedule.notifyAll();
		}
	}
	
	
	@Override
	public void run() {
		executingThread = Thread.currentThread(); //TODO: nicht thread safe
		
		for (int i = 0; i < schedule.getStations().length - 1; i++) {
			lastStation = schedule.getStations()[i];
			nextStation = schedule.getStations()[i+1];
			
			// standard path finding algorithm
			setCurrentPath(graph.getShortestPath(lastStation, nextStation,
					schedule.getScheme().getTrainType().getTopSpeed()));
			currentPosition = currentPath.get(0);
			updateRequests(0, schedule.getIdleTime(nextStation));
			
			anim = figure.walkAlong(transformPath(currentPath));
			anim.setTimeLapse(timeLapse);
			figure.startAnimation();	
			
			// wait till animation ends or interrupted
			try {
				// wait and maybe elude till next station is reached 
				while (!atNode(nextStation)) {
					
					// acquire lock in order to wait
					synchronized (schedule) {
					// wait till animation stops since it reached the next station or
					// the path is blocked
					schedule.wait();
					}
					
					System.out.println("agent " + id + " has been notifed");
					observeEnvironment();
					
					
					// if next station is reached
					if (status == Status.STATION_REACHED) {
						doStationReachedAction();
					}
					
					else if (status == Status.PATH_BLOCKED) {
						figure.stopAnimation();
						figure.clearAnimations();
						
						observeEnvironment();
						PathFindingStrategy strategy = factory.pathBlockedStrategy(
								blockade.getState().getBlocker(), (Path) blockade);
						anim = strategy.execute();
						status = Status.UNKNOWN;
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
						
						PathFindingStrategy s1 = new WaitForUnblockStrategy(this, currentPosition, blockingAgent, blockedNode, graph);
						PathFindingStrategy s2 = new AlternativeRouteStrategy(graph, this, currentPosition);
						PathFindingStrategy s3 = new DrawBackStrategy(this, graph, currentPosition, blockingAgent);
						
						long s1Costs = s1.getCosts();
						long s2Costs = s2.getCosts();
						long s3Costs = s3.getCosts();
						
						if (s1Costs < s2Costs) {
							if (s1Costs < s3Costs) {
								anim = s1.execute();
							} else {
								anim = s3.execute();
							}
						} else {
							if (s2Costs < s3Costs) {
								anim = s2.execute();
							} else {
								anim = s3.execute();
							} 
						}
						
						
						anim.setTimeLapse(timeLapse);
					
					}
				}
			} catch (InterruptedException e) {
				System.out.println("train agent " + id + " has been interrupted, terminates");
				executingThread.interrupt();
				return;
			}
			
			
		}
		removeFigure();//TODO: animation/warten bevor die z�ge verschwinden, sound
		aiPort.getLoop().activeAgents.remove(this);
		aiPort.getSimStat().addStatistic(statistic);
	}
	
	
	
	private void observeEnvironment() {
		NodeFigure curFig = figure.getNodeFigure();
		if (curFig != null) {
			currentPosition = curFig.getModellObject();
			
			if (currentPath != null && currentPath.contains(currentPosition)) {
				currentPath = currentPath.subList(
						currentPath.indexOf(currentPosition),
						currentPath.size());
			}
		}
		
		
	}
	
	private void doStationReachedAction() {
		// compute delay and add it to the statistic
		long simTime = aiPort.getLoop().getSimTimeInMillis();
		int delay = (int) Math.round(((simTime - schedule.getArrivalTime(nextStation)) / 1000.0)); 
		statistic.addStop(figure.getNodeFigure().getModellObject(), delay);
		figure.stopAnimation();
		figure.clearAnimations();

		anim = figure.busy(schedule.getIdleTime(nextStation));
		anim.setTimeLapse(timeLapse);
		figure.startAnimation();
		
		synchronized(anim) { // acquire monitor in order to wait
			while (!anim.isFinished()) {
				try {
					anim.wait();
				} catch (InterruptedException e) {
					System.out.println("train agent " + id + " has been interrupted, terminates");
					executingThread.interrupt();
					return;
				}
			}
			
		}
		
		status = Status.UNKNOWN;
	}
	
	
	
	
	
	/**
	 * Stops all animations of the {@link TrainAgent#figure TrainFigure}
	 * associated with this agent. Then removes the figure from the
	 * {@link NodeMap} and clears all states that are currently blocked or
	 * requested by this agent.
	 */
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

	
	
	
	
	
	
	
	
	
	/**
	 * Checks whether the {@link TrainFigure} is currently at the specified
	 * {@link Node}.
	 * 
	 * @param node
	 *            The node to check
	 * @return true if the {@link TrainFigure} is currently at <code>node</code>
	 *         , otherwise false
	 */
	private boolean atNode(Node node) {
		NodeFigure currentNodeFigure = figure.getNodeFigure();
		if (currentNodeFigure == null) {
			return false;
		}
		return (currentNodeFigure.getModellObject().equals(node));
	}
	
	protected void updateRequests(int offset, int timeAtLast) {
		// clear reservations
		for (State state : requestedStates) {
			state.removeRequest(this);
		}
		requestedStates.clear();
		
		long simTime = aiPort.getLoop().getSimTimeInMillis();
		List<Double> costList = graph.getCosts(currentPath, schedule.getScheme()
				.getTrainType().getTopSpeed());
		double fromDouble;
		long fromLong;
		State state;

		// add first
		state = currentPath.get(0).getState();
		state.reserve(this, simTime, simTime + offset);
		requestedStates.add(state);
		
		for (int i = 1; i < currentPath.size() - 1; i++) {
			state = currentPath.get(i).getState();
			fromDouble = simTime + offset + (costList.get(i) * 60 * 60 * 1000);
			fromLong = ((long) fromDouble) + 1;
			state.reserve(this, fromLong, fromLong);
			requestedStates.add(state);
		}
		
		// add next station
		state = currentPath.get(currentPath.size() - 1).getState();
		fromDouble = simTime + offset + (costList.get(currentPath.size() - 1) * 60 * 60 * 1000);
		fromLong = ((long) fromDouble) + 1;
		state.reserve(this, fromLong, fromLong + timeAtLast);
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
