package de.hohenheim.view.mobile.animation;

import java.util.Observable;

import de.atlassoft.model.State;
import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.mobile.AnimationFigure;
import de.hohenheim.view.mobile.TrainFigure;
import de.hohenheim.view.mobile.animation.listeners.AnimationFinishedEvent;
import de.hohenheim.view.mobile.animation.listeners.AnimationStartedEvent;

/**
 * This class let the busy indicator of the fork truck start to blink.
 * It also lets the fork truck wait till a State Object is unblocked. In this time the fork truck
 * does nothing other then show a very small blinking rectangle.
 * @author Marc Fernandes
 *
 */
public class BusyWaitAnimator extends Observable implements Runnable, Animator {
	NodeMap map;
	AnimationFigure animationFigure;
	boolean finished     = false;
	boolean stopped      = false;
	State state;
	
	/**
	 * 
	 * @param {@link NodeMap} map - The RoomMap where the fork truck is located. 
	 * @param {@link AnimationFigure} figure - The animated figure itself. 
	 * @param State state - A State Object. Typically of a node or a mobile object or a path. This Object
	 *        tells if the object is blocked or unblocked. E.g. a path can be blocked so
	 *        that a mobile object can not walk along the path, till the path is unblocked. 
	 */
	public BusyWaitAnimator(NodeMap map, AnimationFigure figure, State state) {
		this.animationFigure = figure;
		this.map             = map;
		this.state	         = state;
	}
		
	/**
	 * Indicates if the animation is finished. 
	 * @return boolean - true if the animation is finished.
	 */
	public boolean isFinished() {
		return this.finished;
	}
	
	/**
	 * runs the animation and restarts itself as long as the animation is not finished.
	 * After it is finished the observer listening for the finished event is notified.
	 * Don't use this method to start the animation!
	 */
	public void run() {		
		if(this.stopped) {
			this.animationFigure.showBusy(false);
			return;
		}
		if(this.state==null || this.state.getState() == State.UNBLOCKED) {
			this.finished=true;
		    this.animationFigure.showBusy(false);
			//notify Listeners and tell them that animation finished. This listeners are user defined listeners which will be informed.
			animationFigure.notifyAnimationListener(new AnimationFinishedEvent(animationFigure, AnimationFinishedEvent.BUSY_FINISHED));
			//notify observers, here the observer which listens if animation is finished. this Observer looks into the animationqueue 
			//after each finished 
			setChanged();
			notifyObservers(this.animationFigure);
		    return;
		}
		this.animationFigure.showBusy(!this.animationFigure.isShowBusy());
		map.getDisplay().timerExec(500, this);			
	}
	
	/**
	 * Starts the animation. Can also be used to restart the animation if it was stopped.
	 * To start(run) the animation only use this method!
	 */
	public void start() {
		if (animationFigure instanceof TrainFigure) {
			((TrainFigure) animationFigure).setBusyColor(org.eclipse.draw2d.ColorConstants.red);
		}
		
		//notify Listeners
		animationFigure.notifyAnimationListener(new AnimationStartedEvent(animationFigure, AnimationStartedEvent.BUSY_STARTED));
		this.stopped=false;
		map.getDisplay().asyncExec(this);
	}
	
	/**
	 * Stops the animation. The animation can be restarted with @see {@link #start()}
	 */
	public void stop() {
		this.stopped=true;		
		
	}
	
	/**
	 * Returns true, if the animation is stopped.
	 */
	public boolean isStopped() {
		return this.stopped;
	}
}

