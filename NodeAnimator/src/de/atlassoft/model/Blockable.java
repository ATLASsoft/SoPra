package de.atlassoft.model;

/**
 * Interface for all objects that support the use of {@link State} objects to
 * indicate their state.
 * 
 * @author Alexander Balogh
 *
 */
public interface Blockable {

	/**
	 * Returns the {@link State} of this object.
	 * 
	 * @return The {@link State}
	 */
	State getState();
	
}
