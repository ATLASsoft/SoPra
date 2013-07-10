package de.atlassoft.model;

import de.atlassoft.ai.TrainAgent;

/**
 * Specifes the state of a train, node or path.
 * 
 * @author Alexander Balogh
 */
public class State {

	public static final int UNBLOCKED = 0;
	public static final int BLOCKED = 1;
	
	/**
	 * The state of this instance. 
	 */
	private int state;
	
	/**
	 * The object this {@link State} instance belongs to.
	 */
	private Object obj;

	/**
	 * The {@link TrainAgent} that owns the block.
	 */
	private TrainAgent owner;


	/**
	 * Creates a new instance of this class for the specified object.
	 * 
	 * @param obj Object this {@link State} belongs to
	 */
	public State(Object obj) {
		this.obj = obj;
		this.state = UNBLOCKED;
	}



	/**
	 * Returns the current state
	 * 
	 * @return The state
	 */
	public synchronized int getState() {
		return state;
	}
	
	/**
	 * Sets the state. Legal values are only the constants defined int this class.
	 * Nevertheless other values can be set.
	 * 
	 * @param state to be set
	 * @param owner Agent that owns the block
	 */
	public synchronized void setState(int state, TrainAgent owner) {
			this.state = state;
			this.owner = owner;
		System.out.println("agent " + owner + " has set the state of " + obj
				+ " to " + (state == 0 ? "UNBLOCKED" : "BLOCKED"));
	}
	
	/**
	 * Returns the object this state belongs to.
	 * 
	 * @return the object
	 */
	public Object getObject() {
		return obj;
	}
	
	/**
	 * Returns the {@link TrainAgent} that has set the current state of this
	 * object. May return null if state was never set, or if the current state
	 * is {@link State#UNBLOCKED}
	 * 
	 * @return {@link TrainAgent} that owns the block
	 */
	public TrainAgent getBlocker() {
		return owner;
	}
}
