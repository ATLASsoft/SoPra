package de.hohenheim.view.mobile.animation.listeners;

import java.util.EventListener;

public interface AnimationListener extends EventListener {
   
	public void animationStarted(AnimationStartedEvent e);
	public void animationFinished(AnimationFinishedEvent e);
	
}
