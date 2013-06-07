package de.atlassoft.model;

/**
 * This class defines the state of a path. It could be
 * either 0 when it's unblocked or 1 when it's blocked
 * 
 * @author Silvan Haeussermann
 */
public class State {

	//TODO: Nochmal ankucken
	public static final int UNBLOCKED = 0;
	public static final int BLOCKED = 1;
	private int state;
	
	/**
	 * Returns the current state
	 * 
	 * @return
	 * 		The state
	 */
	public int getState() {
		return state;
	}
	
	/**
	 * Sets the state.
	 * 
	 * @param state
	 * 			only 1 or 0 are allowed
	 * @throws IllegalArgumentException
	 * 			if the given value is not 1 or 0
	 */
	public void setState(int state) {
		if (state == 0 | state == 1){
			this.state = state;
		} else {
			throw new IllegalArgumentException(
						"The given value must be 0 or 1");
		}		
	}
}
