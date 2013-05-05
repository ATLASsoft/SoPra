package de.hohenheim.view.mobile.animation.listeners;

import java.util.Vector;

/**
 * 
 * @author Marc Fernandes
 *
 */
public class AnimationEventMulticaster implements AnimationListener {

	protected Vector<AnimationListener> listeners = new Vector<AnimationListener>();
	
	public void add(AnimationListener listener) {
		if(!listeners.contains(listener)) {
		  this.listeners.add(listener);
		}
	}
	
	public void remove(AnimationListener listener) {
		this.listeners.remove(listener);
	}

	@Override
	public void animationFinished(AnimationFinishedEvent e) {
		for(int i=0; i<listeners.size(); i++) {
			listeners.elementAt(i).animationFinished(e);
		}		
	}
	@Override
	public void animationStarted(AnimationStartedEvent e) {
		for(int i=0; i<listeners.size(); i++) {
			listeners.elementAt(i).animationStarted(e);
		}		
	}
}
