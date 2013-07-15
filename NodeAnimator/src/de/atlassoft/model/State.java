package de.atlassoft.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.atlassoft.ai.TrainAgent;

/**
 * Specifies the state of a {@link Node} or {@link Path}.
 * 
 * @author Alexander Balogh
 */
public class State {

	/**
	 * State is unblocked.
	 */
	public static final int UNBLOCKED = 0;
	
	/**
	 * State is blocked.
	 */
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
	 * Map containing all reservations for this state. The key is the
	 * {@link TrainAgent} that has requested the state. The value is the start
	 * time of the request in milliseconds after the start of the simulation.
	 */
	private Map<TrainAgent, Long> reservedFrom;
	
	/**
	 * Map containing all reservations for this state. The key is the
	 * {@link TrainAgent} that has reserved the state. The value is the end
	 * time of the reservation in milliseconds after the start of the simulation.
	 */
	private Map<TrainAgent, Long> reservedTill;
	
	

	/**
	 * Creates a new instance of this class for the specified object.
	 * 
	 * @param obj Object this {@link State} belongs to
	 */
	public State(Object obj) {
		this.obj = obj;
		this.state = UNBLOCKED;
		
		reservedFrom = new HashMap<>();
		reservedTill = new HashMap<>();
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
	 * Sets the state. Legal values are only the constants defined in this class.
	 * Nevertheless other values can be set with unknown consequences.
	 * 
	 * @param state to be set
	 * @param owner Agent that owns the block
	 * @see {@link State#BLOCKED}, {@link State#UNBLOCKED}
	 */
	public synchronized void setState(int state, TrainAgent owner) {
			this.state = state;
			this.owner = owner;
		System.out.println("agent " + owner + " has set the state of " + obj
				+ " to " + (state == 0 ? "UNBLOCKED" : "BLOCKED"));
	}
	
	/**
	 * Reserve this state from <code>from</code> till <code>till</code> i.e.
	 * announce that the {@link TrainAgent} <code>owner</code> will probably
	 * block this state within the specified time. Other agents can consider
	 * this reservation during their path finding routine but have no obligation
	 * to to so. Multiple requests from the same agent are not supported, only
	 * the reservation will be saved.
	 * 
	 * @param owner
	 *            {@link TrainAgent} that will own the block
	 * @param from
	 *            Start time of the alleged block in milliseconds after the
	 *            start of the simulation (in simulation time)
	 * @param till
	 *            End time of the alleged block in milliseconds after the start
	 *            of the simulation (in simulation time)
	 */
	public synchronized void reserve(TrainAgent owner, long from, long till) {
		reservedFrom.put(owner, from);
		reservedTill.put(owner, till);
	}
	
	/**
	 * Removes the reservation associated with <code>owner</code>. If there is
	 * no such reservation no action is taken and no exception is thrown.
	 * 
	 * @param owner
	 *            Owner of the alleged reservation
	 */
	public synchronized void removeReservation(TrainAgent owner) {
		reservedFrom.remove(owner);
		reservedTill.remove(owner);
	}
	
	/**
	 * Removes all reservations.
	 */
	public synchronized void removeReservations() {
		reservedFrom.clear();
		reservedTill.clear();
	}
	
	/**
	 * Returns the start time of the reservation that the {@link TrainAgent}
	 * owner has made for this state. if <code>owner</code> has not made such a
	 * reservation, this method will return null.
	 * 
	 * @param owner
	 *            {@link TrainAgent} that made the reservation
	 * @return Start time of the reservation in milliseconds after the start of
	 *         the simulation (i simulation time), or null
	 */
	public synchronized Long getFromRequest(TrainAgent owner) {
		return reservedFrom.get(owner);
	}
	
	/**
	 * Returns the end time of the reservation that the {@link TrainAgent}
	 * owner has made for this state. if <code>owner</code> has not made such a
	 * reservation, this method will return null.
	 * 
	 * @param owner
	 *            {@link TrainAgent} that made the reservation
	 * @return End time of the reservation in milliseconds after the start of
	 *         the simulation (i simulation time), or null
	 */
	public synchronized Long getTillRequest(TrainAgent owner) {
		return reservedTill.get(owner);
	}
	
	/**
	 * Returns true when the state has not been reserved in the time period
	 * between <code>from</code> and <code>till</code>.
	 * 
	 * @param from
	 *            start of the period in milliseconds after the start of the
	 *            simulation (simulation time)
	 * @param till
	 *            end of the period in milliseconds after the start of the
	 *            simulation (simulation time)
	 * @return true if there is no reservation between from and till
	 */
	public synchronized boolean freeBetween(long from, long till) {
		
		for (Entry<TrainAgent, Long> entry : reservedFrom.entrySet()) {
			if (entry.getValue() <= till && reservedTill.get(entry.getKey()) >= from) {
				return false;
			}
		}
		
		return true;
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
