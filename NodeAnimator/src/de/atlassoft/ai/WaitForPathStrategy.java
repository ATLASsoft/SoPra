package de.atlassoft.ai;

import java.util.List;

import de.atlassoft.model.Node;
import de.atlassoft.model.Path;
import de.hohenheim.view.mobile.TrainFigure;
import de.hohenheim.view.mobile.animation.Animator;
import de.hohenheim.view.mobile.animation.SimpleWalkToAnimator;

public class WaitForPathStrategy extends PathFindingStrategy {

	private TrainAgent agent;
	private TrainFigure figure;
	
	private List<Node> currentPath;
	private TrainAgent blockingAgent;
	private Path blockedPath;
	
	private Node nextNodeOther;
	
	public WaitForPathStrategy(
			TrainAgent agent,
			TrainAgent blockingAgent,
			Path blockedPath) {
		this.agent = agent;
		this.figure = agent.getTrainFigure();
		this.currentPath = agent.getCurrentPath();
		this.blockedPath = blockedPath;
		this.blockingAgent = blockingAgent;
	}
	
	
	@Override
	long getCosts() {
		// compute time agent has to wait till it can move onto the blocked path
		if (currentPath.get(0).equals(blockedPath.getStart())) {
			nextNodeOther = blockedPath.getEnd();
		} else {
			nextNodeOther = blockedPath.getStart();
		}
		
		Long reservedTill = (nextNodeOther.getState().getTillRequest(blockingAgent));
		// other agent has not reserved nextNodeOther for whatever reasons, assign 0
		if (reservedTill == null) {
			return 0;
		} else {
			return reservedTill - agent.getSimTime();
		}
	}

	@Override
	SimpleWalkToAnimator execute() throws InterruptedException {
		if (nextNodeOther == null) {
			getCosts();
		}
		
		Animator anim = figure.waitFor(nextNodeOther.getState());
		figure.startAnimation();
		
		synchronized (anim) {
			while (!anim.isFinished()) {
				anim.wait();
			}
		}
		
		agent.updateReservations(0, 
				agent.getSchedule().getIdleTime(currentPath.get(currentPath.size() - 1)));
		anim = figure.walkAlong(transformPath(currentPath));
		anim.setTimeLapse(agent.getTimeLapse());
		figure.startAnimation();
		return null;
	}

}
