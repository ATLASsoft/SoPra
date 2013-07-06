package de.atlassoft.model;

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
	 */
	public synchronized void setState(int state) {
			this.state = state;
	}
	
	/**
	 * Returns the object this state belongs to.
	 * 
	 * @return the object
	 */
	public Object getObject() {
		return obj;
	}
}
