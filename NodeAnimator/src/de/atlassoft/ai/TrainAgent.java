package de.atlassoft.ai;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.ColorConstants;

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
	
	protected static enum Status {
		STATION_REACHED, STATION_REACHED_APPLY, TMP_TARGET_REACHED, PATH_BLOCKED, NODE_BLOCKED, APPLY_STRATEGY, UNKNOWN,
	}

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
	
	private volatile Status status;
	/**
	 * The current path from lastStation to nextStation.
	 */
	private List<Node>currentPath;
	private Node lastStation;
	private Node nextStation;
	private Node currentPosition;
	private Blockable blockade;
	private volatile PathFindingStrategy applyStrat;
	
	protected volatile boolean wait = true;
	
	
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
	
	
	
	protected void continueRide() {
		figure.startAnimation();
	}
	
	protected void pauseRide() {
		figure.stopAnimation();
	}
	
	protected void terminate() {
		executingThread.interrupt();
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
	
	protected void setAnimator(Animator anim) {
		this.anim = anim;
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
		if (status == Status.APPLY_STRATEGY) {
			// ignore other change since strategy has already been choosen
		}
		else if (blockade instanceof Path) {
			status = Status.PATH_BLOCKED;
		} else {
			status = Status.NODE_BLOCKED;
		}
		
		
		synchronized (schedule) {
			schedule.notifyAll();
		}
	}
	
	public void targetReached(Node target) {
		if (nextStation.equals(target)) {
			if (status == Status.APPLY_STRATEGY) {
				status = Status.STATION_REACHED_APPLY;
			} else {
				status = Status.STATION_REACHED;
			}
		} else {
			status = Status.TMP_TARGET_REACHED;//TODO: was macht man damit
		}
		
		synchronized(schedule) {
			schedule.notifyAll();
		}
	}
	
	/**
	 * Use the specified strategy at the next notify situation.
	 * 
	 * @param strategy {@link PathFindingStrategy} to use
	 */
	public void applyStrategy(PathFindingStrategy strategy) {
		status = Status.APPLY_STRATEGY;
		applyStrat = strategy;
	}
	
	
	@Override
	public void run() {
		executingThread = Thread.currentThread(); 
		
		try {
			for (int i = 0; i < schedule.getStations().length - 1; i++) {
				lastStation = schedule.getStations()[i];
				nextStation = schedule.getStations()[i + 1];

				currentPosition = figure.getNodeFigure().getModellObject();
				PathFindingStrategy strategy = factory.defaultStrategy(
						currentPosition, nextStation);
				anim = strategy.execute();


				// wait and maybe elude till next station is reached 
				while (!atNode(nextStation)) {

					// acquire lock in order to wait
					if (wait) {
						synchronized (schedule) {
							// wait till animation stops since it reached the next
							// station or
							// the path is blocked
							schedule.wait();
						}
					}
					wait = true;
					System.out.println("agent " + id + " has been notifed");
					observeEnvironment();

					switch (status) {

					// if next station is reached
					case STATION_REACHED:
					case STATION_REACHED_APPLY:
						System.out.println("agent + " + id + " has reached the next station");
						doStationReachedAction();
						if (status == Status.STATION_REACHED_APPLY) {
							status = Status.APPLY_STRATEGY;
						} else {
							status = Status.UNKNOWN;
						}
						break;
					
					// facing blocked path
					case PATH_BLOCKED:
						figure.stopAnimation();
						figure.clearAnimations();
						System.out.println("agent + " + id + " is facing blocked path, searches for best strategy");
						strategy = factory.pathBlockedStrategy(
								blockade.getState().getBlocker(),
								(Path) blockade,
								currentPosition,
								nextStation);
						
						anim = strategy.execute();
						status = Status.UNKNOWN;
						break;
						
					// facing blocked node	
					case NODE_BLOCKED:
						figure.stopAnimation();
						figure.clearAnimations();
						
						if (figure.getPath() == null) {
							System.out.println("agent " + id + " faces blocked node, seachres for alternative route");
							
							strategy = factory.nodeBlockedStrategy(
									currentPosition,
									blockade.getState().getBlocker(),
									(Node) blockade,
									currentPath.get(currentPath.size() - 1));
							anim = strategy.execute();
						}
						break;
						
					// apply the strategy introduced by another agent
					case APPLY_STRATEGY:
						anim = applyStrat.execute();System.out.println("apply");
						anim.setTimeLapse(timeLapse);
						status = Status.UNKNOWN;
						break;
						
					case UNKNOWN:
						break;
					
					}

				}
			}
		} catch (InterruptedException e) {
			System.out.println("train agent " + id + " has been interrupted, terminates");
			executingThread.interrupt();
			return;
		}
		removeFigure();
		aiPort.getLoop().activeAgents.remove(this);
		aiPort.getSimStat().addStatistic(statistic);
	}
	
	
	
	private void observeEnvironment() {
		NodeFigure curFig = figure.getNodeFigure();
		// at node
		if (curFig != null) {
			currentPosition = curFig.getModellObject();
			
			if (currentPath != null && currentPath.contains(currentPosition)) {
				currentPath = currentPath.subList(
						currentPath.indexOf(currentPosition),
						currentPath.size());
			}
		}
		
		// at path
		else {
			
		}
	}
	
	private void doStationReachedAction() throws InterruptedException {
		// compute delay and add it to the statistic
		long simTime = aiPort.getLoop().getSimTimeInMillis();
		int delay = (int) Math.round(((simTime - schedule.getArrivalTime(nextStation)) / 1000.0)); 
		statistic.addStop(figure.getNodeFigure().getModellObject(), delay);
		figure.stopAnimation();
		figure.clearAnimations();

		anim = figure.busy(schedule.getIdleTime(nextStation), ColorConstants.green);
		anim.setTimeLapse(timeLapse);
		figure.startAnimation();
		
		synchronized(anim) { // acquire monitor in order to wait
			while (!anim.isFinished()) {
				anim.wait();
			}
		}
		
		
		
		
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
		for (State s : requestedStates) {
			s.removeReservation(this);
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
	
	protected void updateReservations(int offset, int timeAtLast) {
		// clear reservations
		for (State state : requestedStates) {
			state.removeReservation(this);
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
