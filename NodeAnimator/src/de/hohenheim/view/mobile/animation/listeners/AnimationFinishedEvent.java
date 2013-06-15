package de.hohenheim.view.mobile.animation.listeners;

import java.util.EventObject;

/**
 * 
 * @author Marc Fernandes
 */
public class AnimationFinishedEvent extends EventObject {

	private static final long serialVersionUID = 2L;
    
	public final static int MOVE_FINISHED      = 0;
    public final static int LOADING_FINISHED   = 1;
    public final static int UNLOADING_FINISHED = 2;
    public final static int BUSY_FINISHED=3;
	
	private int animationtype;
	
    public AnimationFinishedEvent(Object source, int animationtype) {
		super(source);
		this.animationtype=animationtype;
	}
	
    public int getAnimationtype() {
    	return this.animationtype;
    }
    
	
	public Object getSource() {
		return super.getSource();
	}

	public String toString() {
		return super.toString();
	}

	
}
