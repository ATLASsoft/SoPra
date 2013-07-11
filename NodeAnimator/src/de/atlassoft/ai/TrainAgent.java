package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

	private int id;
	private TrainFigure figure;
	private Schedule schedule;
	private TrainRideStatistic statistic;
	private Graph graph;
	private List<State> blockedState;
	private int timeLapse;
	private SimpleWalkToAnimator anim;
	private Thread runningThread;
	private AIServiceImpl aiPort;
	private List<Node>currentPath;
	
	protected TrainAgent(Graph graph, int id, Schedule schedule, AIServiceImpl aiPort) {
		this.statistic = new TrainRideStatistic(schedule.getScheme());
		this.schedule = schedule;
		this.id = id;
		this.graph = graph;
		this.aiPort = aiPort;
		this.blockedState = new ArrayList<>();
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
	
	protected void setTimeLapse(int timeLapse) {
		this.timeLapse = timeLapse;
		if (anim != null) {
			anim.setTimeLapse(timeLapse);
		}
	}
	
	
	protected List<Node> getCurrentPath() {
		return currentPath;
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
		runningThread = Thread.currentThread();
		
		for (int i = 0; i < schedule.getStations().length - 1; i++) {
			currentPath = graph.getShortestPath(schedule.getStations()[i], schedule.getStations()[i + 1],
					schedule.getScheme().getTrainType().getTopSpeed());
			anim = figure.walkAlong(transformPath(currentPath));
			anim.setTimeLapse(timeLapse);
			figure.startAnimation();	
			
			// wait till animation ends or interrupted
			synchronized (anim) { // acquire lock in order to wait
				try {
					// wait and maybe elude till next station is reached 
					while (innerLoopCondition(i)) {
						
						// wait till animation stops since it reached the next station or
						// the path is blocked
						anim.wait();
						
						// animation is finished, next station reached
						if (anim.isFinished()) {
							
							// compute delay and add it to the statistic
							long simTime = aiPort.getLoop().getSimTimeInMillis();
							int delay = (int) Math.round(((simTime - schedule.getATs()[i+1]) / 1000.0)); 
							statistic.addStop(figure.getNodeFigure().getModellObject(), delay);
							figure.stopAnimation();
							figure.clearAnimations();
							
							
							//TODO: warten (idle time)
						}
						
						// animation has been aborted before reaching goal,
						// search for best alternative strategy
						else {
							System.out.println("train agent " + id + " waits");
							
							Node currentPosition = figure.getNodeFigure().getModellObject();
							Node goal = schedule.getStations()[i+1];
							Node blockedNode = currentPath.get
									(1 + currentPath.indexOf(currentPosition));
							TrainAgent blockingAgent = blockedNode.getState().getBlocker();
							
							// strategy 1: wait till other agent has passed
							List<Node> pathOther = blockingAgent.getCurrentPath();
							Node nextAfterBlockO =
									pathOther.get(1 + pathOther.indexOf(blockedNode));
							if (!nextAfterBlockO.equals(currentPosition)) {
								
							}
							
							
							// strategy 2 compute alternative route
							// strategy 3 draw back and let other pass
							
							
							
							
							
							figure.stopAnimation();//TODO: gscheit reagieren
							figure.clearAnimations();
							figure.busy(100);
							figure.startAnimation();
						}
					}
				} catch (InterruptedException e) {
					System.out.println("train agent " + id + " has been interrupted, terminates");
					return;
				}
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

	private boolean innerLoopCondition(int i) {
		NodeFigure currentNodeFigure = figure.getNodeFigure();
		if (currentNodeFigure == null) {
			return true;
		}
		return (!currentNodeFigure.getModellObject()
				.equals(schedule.getStations()[i + 1]));
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
