package de.hohenheim.view.mobile.animation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.geometry.PointList;

import de.atlassoft.ai.TrainAgent;
import de.atlassoft.model.State;
import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.mobile.AnimationFigure;
import de.hohenheim.view.mobile.animation.exceptions.PathNotFoundException;
import de.hohenheim.view.mobile.animation.listeners.AnimationFinishedEvent;
import de.hohenheim.view.mobile.animation.listeners.AnimationStartedEvent;
import de.hohenheim.view.node.NodeFigure;
import de.hohenheim.view.path.PathFigure;

/**
 * This class animates a {@link AnimationFigure}. The figure moves form its
 * current position along to specified nodes.
 * 
 * @author Alexander Balogh
 * 
 */
public class SimpleWalkToAnimator extends Observable implements Runnable, Animator {
	/**
	 * The {@link NodeMap} where the animation takes place.
	 */
	NodeMap map;
	/**
	 * The figure to animate.
	 */
	AnimationFigure animationFigure;
	
	/**
	 * Milliseconds since the last execution of run.
	 */
	private int delta;
	
	/**
	 * Auxiliary variable to compute delta. Time stamp of the last execution of run.
	 */
	private long last;
	
	/**
	 * Number of segments to move in this execution of run in theory. May be no whole
	 * number.
	 */
	double pixelToMoveT;
	
	/**
	 * Number of segments to really move in this execution of run.
	 */
	int pixelToMoveP;
	
	/**
	 * Number of segments that should have been moved in the last execution of run.
	 * (Margin between {@link WalkToAnimator#pixelToMoveT} and
	 * {@link WalkToAnimator#pixelToMoveP}).
	 */
	private double pixelCarry;
	
	NodeFigure start_node; //TODO: scheint man nicht zu brauchen
	
	/**
	 * The room where the figure should go. this is a temporal position at a moment during the animation
	 * because along a path a figure can visit a lot of rooms.
	 */
	NodeFigure end_node;

	/**
	 * Indicates if animation started for the first time for one direct path.
	 * This means: if the animation has more then one direct paths from one room to another
	 * then for each inner animation init must be set to true.
	 */
	boolean init;
	
	/**
	 * The parent of the animated figure. We need that to repaint the parent.
	 */
	IFigure parent;
	
	/**
	 * We use a {@link ManhattanConnectionRouter}. So there can be rectangular branches
	 * along a path between two rooms. To animate the figure along this paths we separate
	 * all horizontal and vertical lines as segments to change direction (e.g. left then up). 
	 */
	PointList segments;

	/**
	 * The actual segment along the path which is animated in the moment.
	 */
	int segment_nr = 0;	
	
	/**
	 * Indicates if the animation is stopped.
	 */
	boolean stopped;
	
	/**
	 * Indicates that a stopaction has started. Problem is that we must wait till it is really stopped!
	 */
	boolean stop;
	
	/**
	 * A list with all rooms where we want to go.
	 */
	Iterator<NodeFigure> walking_path;	
	/**
	 * indicates if the animation is finished.
	 */
	boolean finished;
	
	/**
	 * Real time velocity this figure drives with at the moment. (Without
	 * considering time lapse) in pixel per millisecond.
	 */
	double velocity;
	
	/**
	 * Time lapse of the simulation.
	 */
	int timeLapse;
	
	/**
	 * Constructor. creates the Animation.
	 * 
	 * @param {@link NodeMap} map - The NodeMap where the animation takes place.
	 * @param {@link AnimationFigure} figure - the figure we want to animate
	 *        itself.
	 * @param {@link NodeFigure} end_node - The node where we want to go to.
	 */
	public SimpleWalkToAnimator(NodeMap map, AnimationFigure figure,
			List<NodeFigure> path) {
		this.animationFigure = figure;
		this.init = true;
		this.walking_path = path.iterator();
		this.map = map;

		stop = false;
		stopped = false;
		finished = false;
	}
	
	
	
	/**
	 * Sets the time lapse of this animation.
	 * 
	 * @param timeLapse
	 */
	public void setTimeLapse(int timeLapse) {
		this.timeLapse = timeLapse;
	}
	
	/**
	 * Helper method to get a path from the figures ({@link AnimationFigure}) current position to the specified node.
	 * @param {@link NodeFigure} end_node - The node we want to have a direct path to. 
	 * @return {@link PathFigure}.
	 * @throws PathNotFoundException if no path can be found.
	 */
	private PathFigure getPathTo(NodeFigure end_node) {
	  ArrayList<PathFigure> edges_end = map.getPaths().get(end_node);
	  
	  if(animationFigure.getPath() != null) {
		 for(int i=0; i< edges_end.size();i++) {
			 if(edges_end.get(i).equals(animationFigure.getPath())) {
				 return animationFigure.getPath(); 
			 }
		 }
	     return null;
	  }
	  	
	  NodeFigure start_node = this.animationFigure.getNodeFigure();
	  ArrayList<PathFigure> edges_start = map.getPaths().get(start_node);
	  
	  if(start_node.equals(end_node)) {		  
		  return null;
	  }
	  for(int i=0; i<edges_start.size(); i++) {
		  for(int j=0; j<edges_end.size();j++) {
			  if(edges_start.get(i).equals(edges_end.get(j))) {
				  return edges_start.get(i);
			  }
		  }
	  }
	  return null;
	}
	
	/**
	 * Indicates if the animation is finished.
	 */
	public boolean isFinished() {
		return this.finished;
	}
	
	/**
	 * Indicates if the current animation is stopped successfully.
	 */
	public boolean isStopped() {
		return this.stopped;
	}
	
	/**
	 * runs the animation and restarts itself as long as the animation is not finished.
	 * After it is finished the observer listening for the finished event is notified.
	 * Don't use this method to start the animation!
	 */
	public void run() {
		
		
		if (init) {			
			PathFigure path;
			start_node = animationFigure.getNodeFigure();			
			
			if (start_node == null) {
				// TODO: wurde aufgerufen während sich die animationFigure auf einem Pfad aufgehalten hat (nur interessant beim ersten durchlauf)
				throw new RuntimeException("unklar");
			}
			
			if (!walking_path.hasNext()) {
				this.finished=true;
				//Inform the observer that animation is finished.
				//this is needed, so that we can e.g. start a new animation after this one is finished.
//				animationFigure.setNode(end_node); //TODO: auskommentiert damit nicht doppelt gezählt wird für heapmap, ausprobieren ob noch alles klappt
				
				//notify Listeners
				animationFigure.notifyAnimationListener(new AnimationFinishedEvent(animationFigure, AnimationFinishedEvent.MOVE_FINISHED));
				
				//Notify Observers
				setChanged();				
				notifyObservers(animationFigure);
				
				//notify waiting threads
				synchronized (this) { // acquire lock
					this.notifyAll();
				}
				return;
			}
			
			end_node = walking_path.next();			
			path = getPathTo(end_node);
			
			// set speed as maximal possible speed (minimum of train and path
			// top speed) on the current path
			velocity = Math.min(path.getModellObject().getTopSpeed(),
					((TrainAgent) animationFigure.getModelObject())
							.getSchedule().getScheme().getTrainType()
							.getTopSpeed());
			velocity = velocity / 360_000.0; // convert from km/h to pixel/ms
			
			segments = Utils.getSegments(path, end_node, animationFigure);
			
			//TODO: versteh ich noch nicht
			if (segments.size() == 0) {
				
				//Inform the observer that animation is finished.
				//this is needed, so that we can e.g. start a new animation after this one is finished.
				animationFigure.setNode(end_node);
				
				//notify Listeners
				animationFigure.notifyAnimationListener(new AnimationFinishedEvent(animationFigure, AnimationFinishedEvent.MOVE_FINISHED));
				
				//Notify Observers
				setChanged();				
				notifyObservers(animationFigure);
				
				//notify waiting threads
				synchronized (this) { // acquire lock
					this.notifyAll();
				}
				return;
			}
			
			// path is blocked TODO: besser lösen, synchronisieren, reicht nicht auch der node check auch bei reservierung wecken?
			if ((path.getModellObject().getState().getState() != State.UNBLOCKED) || end_node.getModellObject().getState().getState() != State.UNBLOCKED)  {
				synchronized (this) { // acquire lock
					this.notifyAll();
				}
				return;
			}
			//TODO: überprüfen und laufen muss aufjedenfall synchronisiert werden, vllt über state
			animationFigure.setPath(path);
			animationFigure.setDirection_to_node(end_node);
			
			parent = animationFigure.getParent();
			init = false;		
		}
		
		// compute delta
		delta = (int) (System.currentTimeMillis() - last);
		last = System.currentTimeMillis();
		
		pixelToMoveT = (velocity * timeLapse * delta) + pixelCarry;   //(velocity * delta) + pixelCarry;
		pixelToMoveP = (int) pixelToMoveT; // round down to next lower integer
		pixelCarry = pixelToMoveT - pixelToMoveP;
		segment_nr += pixelToMoveP;
		
		// end_node not reached yet
		if (segment_nr < segments.size()) {
			animationFigure.setLocation(segments.getPoint(segment_nr));
		}
		// end_node reached
		else {
			//TODO: hier die zeit messen für statistik
			animationFigure.setLocation(segments.getPoint(segments.size() - 1));
			animationFigure.setNode(end_node);
			segment_nr = 0;
			init = true;
		}
		
		parent.repaint();
		
		if(stop) { 
			//Stop can cause problems when we restart it later again and the figur is not in a room and not 
			//on a direct path: means it is between a path and a room...
			if(this.animationFigure.getNodeFigure() == null) {
				NodeFigure next_node = this.animationFigure.getDirection_to_node();
				if(next_node.intersects(animationFigure.getBounds())) {
					this.animationFigure.setNode(next_node);
				}
			}	
			this.stopped=true;
			return;
		}
		map.getDisplay().timerExec(0, this);			
	}
	
	/**
	 * Starts the animation. Can also be used to restart the animation if it was stopped.
	 * To start(run) the animation only use this method!
	 */
	public void start() {
		this.stop = false;
		
		//notify Listeners
		animationFigure.notifyAnimationListener(new AnimationStartedEvent(animationFigure, AnimationStartedEvent.MOVE_STARTED));
		delta = 0;
		last = System.currentTimeMillis();
		map.getDisplay().asyncExec(this);
	}
	/**
	 * Stops the animation. The animation can be restarted with @see {@link #start()}
	 */
	public void stop() {
		this.stop=true;				
	}	
}



