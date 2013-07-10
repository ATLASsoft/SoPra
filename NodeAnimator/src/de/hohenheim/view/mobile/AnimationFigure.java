package de.hohenheim.view.mobile;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.graphics.Image;

import de.atlassoft.ai.TrainAgent;
import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.mobile.animation.AnimationFinishedQueueObserver;
import de.hohenheim.view.mobile.animation.Animator;
import de.hohenheim.view.mobile.animation.WalkToAnimator;
import de.hohenheim.view.mobile.animation.listeners.AnimationEventMulticaster;
import de.hohenheim.view.mobile.animation.listeners.AnimationFinishedEvent;
import de.hohenheim.view.mobile.animation.listeners.AnimationListener;
import de.hohenheim.view.mobile.animation.listeners.AnimationStartedEvent;
import de.hohenheim.view.node.NodeFigure;
import de.hohenheim.view.path.PathFigure;

/**
 * This class represents a Figure. To exist in the system a mobile object must 
 * be located in a RoomMap. This means either in a Room or on a Path between rooms.
 * 
 * When the mobileFigure Object is instantiated, this Object must be located in a room,
 * for example in the "entrance"-room of the RoomMap.
 * 
 * @author Marc Fernandes
 * @version 1.0
 */
public abstract class AnimationFigure extends Figure {
	Image img = null;
	NodeFigure room = null;
	NodeFigure direction_to_room = null;
	PathFigure path = null;
	Animator currentAnimation = null;
	ArrayList<Animator> animationList = new ArrayList<Animator>();
	Iterator<Animator> animationIterator = animationList.iterator();
	AnimationEventMulticaster multicaster = new AnimationEventMulticaster();
	NodeMap map;
	boolean showBusy=false;
	int figure_id;
	
	/**
	 * This is used if we want to add a modelObject to the figure.
	 * A modelObject can extend the viewpart with other information,  
	 * for example for state informations, capacity and so on
	 */
	TrainAgent modellObject = null;
	
	/**
	 * Constructor
	 * @param {@link NodeMap} map - the RoomMap the {@link AnimationFigure} is located.
	 * @param {@link NodeFigure} room - The room the {@link AnimationFigure} is located. 
	 */
	public AnimationFigure(NodeMap map, NodeFigure room, int id, TrainAgent modellObject) {
		setModelObject(modellObject);
		setNode(room);
		setRoomMap(map);
		this.figure_id=id;
	}	
	
	/**
	 * Returns the unique id of this fork truck.
	 * @return int
	 */
	public int getFigureId() {
		return this.figure_id;
	}
	
	/**
	 * Sets the Modelobject which belongs to this AnimationFigure.
	 * @param {@link Object} o - The Modelobject. 
	 */
	public void setModelObject(TrainAgent o) {
		this.modellObject=o;
	}
	
	/**
	 * Gets the Modelobject which belongs to this AnimationFigure.
	 * @return {@link Object} o - The Modelobject. 
	 */
	public TrainAgent getModelObject() {
		return this.modellObject;
	}
	
	/**
	 * Class to start all Listeners which are registered an catch AnimationFinishedEvents 
	 * @param {@link AnimationFinishedEvent} event
	 */
	public void notifyAnimationListener(AnimationFinishedEvent event) {
	   multicaster.animationFinished(event);	
	}
	/**
	 * Class to start all Listeners which are registered an catch AnimationStartedEvents 
	 * @param {@link AnimationStartedEvent} event 	  
	 */	
	public void notifyAnimationListener(AnimationStartedEvent event) {
		   multicaster.animationStarted(event);	
	}
	
	/**
	 * Sets the RoomMap for this figure, i.e. the map where this figure will be animated.
	 * @param map
	 */
	public void setRoomMap(NodeMap map) {
	  this.map=map;	
	}
	
	/**
	 * This method registers listeners which listen for animation events, such as animation finished and so on.
	 * @param listener
	 */
	public void addAnimationListener(AnimationListener listener) {
		multicaster.add(listener);
	}
	
	/**
	 * Returns the RoomMap of this figure.
	 */
	public NodeMap getRoomMap() {
		return this.map;
	}
	
	/**
	 * Setter method to set the room in which the mobile object is located in the moment.
	 * We must set the path to <code>null</code> when a room ist set. A mobile object can not
	 * be in a room and on a path.
	 *  
	 * @param NodeFigure Sets the Room where the mobile object should be located actually.
	 */
	public void setNode(NodeFigure room) {
		this.room = room;
		modellObject.block(room.getModellObject().getState());
		
		if (this.path != null) {
			modellObject.unBlock(path.getModellObject().getState());
			this.path = null;
		}
	}
	
	/**
	 * Sets the RoomFigure to which the current animated Figure is walking to and reserves it. 
	 * @param room RoomFigure, to which the mobile object is actually walking to.
	 */
	public void setDirection_to_node(NodeFigure room) {
		this.direction_to_room = room;
		modellObject.block(direction_to_room.getModellObject().getState());
	}
	
	/**
	 * Get the direction to which the current animated figure is walking to.
	 * 
	 * @return RoomFigure
	 */	 
	public NodeFigure getDirection_to_node() {
		return this.direction_to_room;
	}
	
	/**
	 * Getter method to get the room in which the mobile object is located in the moment.
	 * @return RoomFigure The Room where the mobile object is located actually.
	 */
	public NodeFigure getNodeFigure() {
		return this.room;
	}
	
	/**
	 * Setter Method. Sets the path where a mobile object is actually located. --> Room is then set to <code>null</code>.
	 * A mobile object can be inside a room or on a path.
	 * @param on_path
	 */
	public void setPath(PathFigure on_path) {
		this.path = on_path;
		modellObject.block(path.getModellObject().getState());
		
		if (this.room != null) {
			modellObject.unBlock(room.getModellObject().getState());
			this.room = null;
		}
	}
	
	/**
	 * The Path, if the mobileFigure is actually on a path and not in a room.
	 * 
	 * @return PathFigure
	 */
	public PathFigure getPath() {
		return this.path;
	}
	
	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	public abstract void paintFigure(Graphics g);
	
	/**
	 * An animation from the current position to a room.
	 * The mobile object must be in a room or on a path which is connected to the room it wants to go.
	 * 
	 * @param NodeMap The RoomMap where the animation should be done.
	 * @param NodeFigure The Room the mobile object shall go.
	 */
	public void walkTo(NodeMap map, NodeFigure room) {
		WalkToAnimator anim = new WalkToAnimator(map, this, room);	
		AnimationFinishedQueueObserver handler = new AnimationFinishedQueueObserver();
		anim.addObserver(handler);		
		addAnimation(anim);		
	}		
	
	/**
	 * Returns the current animation. Can be running or not.
	 * Can even be <code>null</code>
	 */
	public Animator getCurrentAnimation() {
		return this.currentAnimation;
	}
	
	/**
	 * Returns the list of all actual animations added to this MobileFigure.
	 * @return ArrayList<Animator>  
	 */
	public ArrayList<Animator> getAnimationList() {
		return this.animationList;
	}
	
	/**
	 * Returns the list of all actual animations added to this MobileFigure as an iterator.
	 * @return Iterator<Animator>
	 */
	public Iterator<Animator> getAnimationIterator() {
		return this.animationIterator;
	}
	
	/**
	 * Clears all Animations of the current MobileFigure.
	 */
	public void clearAnimations() {
		currentAnimation=null;
		  animationList.clear();
	}
	
	/**
	 * Adds an animation. It is necessary to call this method instead simply add it to the arraylist, 
	 * because we must also update the iterator.
	 * 
	 * @param animation
	 */
	protected void addAnimation(Animator animation) {
		this.animationList.add(animation);
		this.animationIterator =this.animationList.iterator();
	}
	
	/**
	 * Returns the next available animation.
	 * @return Animator
	 */
	public Animator nextAnimation() {
		this.currentAnimation = this.animationIterator.next();
		return this.currentAnimation;
	}
	
	/**
	 * Returns true if the AnimationIterator has at least one more element.
	 * @return boolean
	 */
	public boolean hasNextAnimation() {
		return this.animationIterator.hasNext();
	}
	
	/**
	 * Starts an animation: walkRandom or walkTo. 
	 */
	public void startAnimation() {
		if(this.currentAnimation!=null){
			this.currentAnimation.start();
		} else {
		  if(this.hasNextAnimation()) {
			this.nextAnimation().start();
		  }
		}
	}
	
	/**
	 * Stops the current animation. 
	 */
	public void stopAnimation() {
		if(currentAnimation!=null) {
			currentAnimation.stop();
		}
	}
	
	/**
	 * Sets the busy indicator to true or false. This method is used by an animator, e.g. to draw a 
	 * blinking rectangle which indicates if the figure is busy/waiting.
	 * @param load
	 */
	public void showBusy(boolean busy) {
		this.showBusy=busy;
		this.repaint();
	}
	
	/**
	 * Returns the current state of the busy indicator. i.e. if the 
	 * busyindicator is visible in the moment or not. This method is used by an animator, e.g. to draw a 
	 * blinking rectangle which indicates if the figure is busy/waiting.
	 * @return boolean "isBusyRectangleVisible"
	 */
	public boolean isShowBusy() {
		return this.showBusy;
	}
}