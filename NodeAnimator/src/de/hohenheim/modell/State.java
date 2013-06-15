package de.hohenheim.modell;

import java.util.HashMap;

public class State {
	
	/**
	 * Static Constant which can be used to assign a state object the value UNBLOCKED.
	 */
	public static final int UNBLOCKED = 0;
	/**
	 * static Constant which can be used to assign a state object the value BLOCKED.
	 */
	public static final int BLOCKED = 1;
	
	/**
	 * Static HashMap which contains all state objects. They are assigned automatically to this HashMap after creation.
	 */
	public static HashMap<String, State> statemap = new HashMap<String, State>();
	
	/**
	 * The state value of the current object. (0=UNBLOCKED, 1=BLOCKED)
	 */
	private int state;
	/**
	 * The object to which this state object belongs, e.g. a Room.
	 */
	private Object object;
	/**
	 * Constructor. Creates a new state object and assigns a object to this state object.
	 * The new state object is automatically added to the static HashMap statemap.
	 * @param {@link Object} o - the object to which this state belongs. 
	 */
	public State(Object o) {
	  state=0;	
	  this.object=o;
	  statemap.put(o.toString(), this);	  
	}
	
	/**
	 * Returns the state value.
	 * @return int - 0=UNBLOCKED, 1=BLOCKED
	 */
	public int geState() {
		return this.state;
	}
	
	/**
	 * Sets the state. 
	 * @param int state - only the values 0=BLOCKED or 1=UNBLOCKED are allowed.
	 * @throws IllegalArgumentException - if value is not 0 or 1.
	 */
	public void setState(int state) throws IllegalArgumentException {
		if(state>=0 || state<=1) {
		  this.state=state;			
		} else {
			throw new IllegalArgumentException("Values must be between 0 (UNBLOCKED) and 1(BLOCKED). Please use the static constants of this class.");
		}
	}
	/**
	 * Returns the object to which this state belongs.
	 * @return {@link Object} o
	 */
	public Object getObject() {
		return this.object;
	}
}
