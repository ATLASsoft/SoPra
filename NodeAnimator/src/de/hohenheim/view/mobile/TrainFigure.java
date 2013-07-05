package de.hohenheim.view.mobile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import de.atlassoft.ai.TrainAgent;
import de.atlassoft.util.ImageHelper;
import de.hohenheim.modell.State;
import de.hohenheim.view.constants.Constants;
import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.mobile.animation.AnimationFinishedQueueObserver;
import de.hohenheim.view.mobile.animation.Animator;
import de.hohenheim.view.mobile.animation.BusyAnimator;
import de.hohenheim.view.mobile.animation.BusyWaitAnimator;
import de.hohenheim.view.mobile.animation.SimpleWalkToAnimator;
import de.hohenheim.view.mobile.animation.WalkToAnimator;
import de.hohenheim.view.node.NodeFigure;
import de.hohenheim.view.path.PathFigure;

/**
 * @author Marc Fernandes
 * @version 1.0
 * 
 * This class represents a ForkTruck. To exist in the system a mobile object must 
 * be located in a RoomMap. This means either in a Room or on a Path between rooms.
 * 
 * When the mobileFigure Object is instantiated, this Object must be located in a room,
 * for example in the "entrance" of the RoomMap.
 */
public class TrainFigure extends AnimationFigure {
	
	NodeFigure room = null;
	NodeFigure direction_to_room = null;
	PathFigure path = null;
	Animator currentAnimation = null;
	ArrayList<Animator> animationList = new ArrayList<Animator>();
	Iterator<Animator> animationIterator = animationList.iterator();
	private Image image;
	
	/**
	 * Constructor. Sets the mobile object we want to create inside a room (as starting point)
	 * @param {@link NodeMap} map - The map where the figure should be placed. 
	 * @param NodeFigure The Room where the mobile object is located when it is instantiated.
	 * @param int id - a unique id for the fork truck. 
	 */
	public TrainFigure(NodeMap map, NodeFigure in_room, int id, Object modellObject) {
		super(map,in_room, id, modellObject);
		this.setFont(Constants.TRAIN_FONT);
		image = ((TrainAgent) modellObject).getSchedule()
				.getScheme().getTrainType().getImg();
		if (image == null) {
			image = ImageHelper.getImage("standardTrainIcon");
		}
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
	
	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	public void paintFigure(Graphics g) {
	    Rectangle r = bounds;
	     
	    g.setForegroundColor(ColorConstants.white);	    
	    g.drawImage(image, r.x,r.y);
	    
	    Font f = this.getFont();
	    Dimension dim_text = FigureUtilities.getTextExtents(""+getFigureId(), f);
	    int x = r.width/2 - dim_text.width/2;
	    int y = r.height/2 - dim_text.height/2;
	    
	    g.drawText(""+getFigureId(),new Point(r.x+x+8, r.y+y+4));
	    if(showBusy) {
	    	g.setBackgroundColor(ColorConstants.red);
	    	g.fillRectangle(r.x+r.width-5, r.y+10, 5, 5);
	    }	   
	}
	
	/**
	 * An animation from the current position to a room.
	 * The mobile object must be in a room or on a path which is connected to the room it wants to go.
	 * 
	 * @param NodeMap The RoomMap where the animation should be done.
	 * @param NodeFigure The Room the mobile object shall go.
	 */
	public void walkTo(NodeFigure room) {
		WalkToAnimator anim = new WalkToAnimator(this.map, this, room);	
		AnimationFinishedQueueObserver handler = new AnimationFinishedQueueObserver();
		anim.addObserver(handler);		
		addAnimation(anim);		
	}
	
	public Animator walkAlong(List<NodeFigure> path) {
		SimpleWalkToAnimator anim = new SimpleWalkToAnimator(this.map, this, path);
		AnimationFinishedQueueObserver handler = new AnimationFinishedQueueObserver();
		anim.addObserver(handler);
		addAnimation(anim);
		return anim;
	}
	
	/**
	 * Starts an animation which shows that the fork truck is waiting/busy.
	 * Waits till the busy_time is over.
	 * @param int busy_time. The time the fork truck is waiting/busy.
	 */
	public void busy(int busy_time) {
		BusyAnimator anim = new BusyAnimator(this.map, this, busy_time);
		AnimationFinishedQueueObserver handler = new AnimationFinishedQueueObserver();
		anim.addObserver(handler);		
		addAnimation(anim);
	}		
	/**
	 * Starts an animation which shows that the fork truck is waiting/busy.
	 * Waits till the state object has the value UNBLOCKED.
	 * @param State state. The State of n object the fork truck is waiting for.
	 */
	public void waitFor(State state) {
		BusyWaitAnimator anim = new BusyWaitAnimator(this.map, this, state);
		AnimationFinishedQueueObserver handler = new AnimationFinishedQueueObserver();
		anim.addObserver(handler);		
		addAnimation(anim);
	}
	
}