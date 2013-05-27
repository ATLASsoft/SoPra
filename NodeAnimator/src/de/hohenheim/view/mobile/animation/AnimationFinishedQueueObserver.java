package de.hohenheim.view.mobile.animation;

import java.util.Observable;
import java.util.Observer;

import de.hohenheim.view.mobile.AnimationFigure;
/**
 * This class observes animations for events which are fired when an animation is finished.
 * If there are more animations in an animationqueue the next animation will be started.
 * @author Marc Fernandes
 */
public class AnimationFinishedQueueObserver implements Observer {
	
	/**
	 * If an animation is finished we must check if the {@link AnimationFigure} to which this
	 * observer is assigned has more animations in his animationqueue. If so it starts the 
	 * next animation.
	 * @see Observer
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof AnimationFigure) {			
			AnimationFigure fig = (AnimationFigure)arg;
			if(fig.getCurrentAnimation().isFinished()) {				
				if(fig.hasNextAnimation()) {
					fig.nextAnimation().start();
				}				
			}
		} 
	}
}
