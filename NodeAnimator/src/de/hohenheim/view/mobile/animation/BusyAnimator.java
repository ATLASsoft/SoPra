package de.hohenheim.view.mobile.animation;

import java.util.Observable;

import org.eclipse.swt.graphics.Color;

import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.mobile.AnimationFigure;
import de.hohenheim.view.mobile.TrainFigure;
import de.hohenheim.view.mobile.animation.listeners.AnimationFinishedEvent;
import de.hohenheim.view.mobile.animation.listeners.AnimationStartedEvent;

/**
 * This class let the busy indicator of the figure start to blink.
 * It also lets the figure wait for a "busy_time". In this time the figure
 * does nothing other then show a very small blinking rectangle.
 * @author Marc Fernandes
 *
 */
public class BusyAnimator extends Observable implements Runnable, Animator {
	NodeMap map;
	AnimationFigure animationFigure;
	boolean finished     = false;
	boolean stopped      = false;
	long busy_time;
	int time_left;
	volatile int timeLapse = 1;
	
	/**
	 * frequency of the pauses
	 */
	int waitTime;
	
	/**
	 * Color of the busy indicator
	 */
	Color color;
	
	/**
	 * 
	 * @param {@link NodeMap} map - The map where the animated figure is located. 
	 * @param {@link AnimationFigure} figure - The AnimationFigure itself.
	 * @param int busy_time - A time in milliseconds for which the truck is busy (in simulation time). 
	 */
	public BusyAnimator(NodeMap map, AnimationFigure figure, int busy_time, Color color) {
		this.animationFigure = figure;
		this.map             = map;
		this.busy_time       = busy_time;
		this.time_left       = busy_time;
		this.color = color;
	}
		
	/**
	 * Indicates if the animation is finished. 
	 * @return boolean - true if the animation is finished.
	 */
	public boolean isFinished() {
		return this.finished;
	}
	
	/**
	 * Runs the animation and restarts itself as long as the animation is not finished.
	 * After it is finished the observer listening for the finished event is notified.
	 * Don't use this method to start the animation!
	 */
	public void run() {		
		if(this.stopped)
			return;
		if(this.time_left < 0) {
			this.finished=true;
			this.animationFigure.showBusy(false);
			
			//notify Listeners
			animationFigure.notifyAnimationListener(new AnimationFinishedEvent(animationFigure, AnimationFinishedEvent.BUSY_FINISHED));
			
			//notify observers
			setChanged();
			notifyObservers(this.animationFigure);
			
			// notify waiting threads
			synchronized (this) {
				this.notifyAll();
			}
			return;
		}
		this.time_left -= 500 * timeLapse;
		this.animationFigure.showBusy(!this.animationFigure.isShowBusy());

		this.waitTime = 500;
		
		// less than 500 ms to wait
		if (time_left < 0) {
			waitTime = 500 + (time_left / timeLapse);
		}
		map.getDisplay().timerExec(waitTime, this);			
	}
	
	/**
	 * Starts the animation. Can also be used to restart the animation if it was stopped.
	 * To start(run) the animation only use this method!
	 */
	public void start() {
		//notify Listeners
		if (animationFigure instanceof TrainFigure) {
			((TrainFigure) animationFigure).setBusyColor(color);
		}
		
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
	
	public boolean isStopped() {
		return this.stopped;
	}
	
	@Override
	public void setTimeLapse(int timeLapse) {
		this.timeLapse = timeLapse;
	}
}

