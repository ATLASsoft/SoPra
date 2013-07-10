package de.hohenheim.modell;


import java.util.ArrayList;
import java.util.Iterator;

import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.mobile.TrainFigure;
import de.hohenheim.view.mobile.animation.listeners.AnimationFinishedEvent;
import de.hohenheim.view.mobile.animation.listeners.AnimationListener;
import de.hohenheim.view.mobile.animation.listeners.AnimationStartedEvent;
import de.hohenheim.view.node.NodeFigure;

public class Train {
	TrainFigure figure; 
	ArrayList<State> my_blocking_states_list = new ArrayList<State>();
	
	public Train(NodeMap map, NodeFigure in_room, int train_id) {
//		figure = FigureFactory.createTrainFigure(map, in_room, train_id, this);
	    figure.addAnimationListener(new AnimationListener() {
			
			public void animationFinished(AnimationFinishedEvent e) {
			  TrainFigure fig=(TrainFigure)e.getSource();	
			  System.out.print("Train " + fig.getFigureId()+" is finished with ");
			  switch (e.getAnimationtype()) {
			  	case AnimationFinishedEvent.BUSY_FINISHED:
			  		System.out.println("waiting ...");
			  		break;
			  	case AnimationFinishedEvent.MOVE_FINISHED:
			  		System.out.println("moving ...");
			  		break;				  	
			  	default:
			  		break;
			  }
			}
		
			public void animationStarted(AnimationStartedEvent e) {
			  TrainFigure fig=(TrainFigure)e.getSource();	
			  System.out.print("Train " + fig.getFigureId()+" started ");
			  switch (e.getAnimationtype()) {
				case AnimationStartedEvent.BUSY_STARTED:
					System.out.println("waiting ...");
					break;
				case AnimationStartedEvent.MOVE_STARTED:
					System.out.println("moving ...");
					break;					
				default:
					break;
			  }
			}		
	    });
	}
	
	public Iterator<State> getBlockedStates() {
		return this.my_blocking_states_list.iterator();
	}
	
}
