package de.atlassoft.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.atlassoft.model.Node;
import de.hohenheim.view.mobile.animation.SimpleWalkToAnimator;
import de.hohenheim.view.node.NodeFigure;

/**
 * Abstract superclass for all path finding algorithms. Implementation of the
 * strategy design pattern.
 * 
 * @author Alexander Balogh
 * 
 */
public abstract class PathFindingStrategy {

	/**
	 * Returns the costs of this solution i.e. the time in milliseconds the train needs to move
	 * to the next station when applying this strategy.
	 *  
	 * @return Time to reach the next station in milliseconds
	 */
	abstract long getCosts();
	
	/**
	 * Executes this strategy.
	 * 
	 * @return The {@link SimpleWalkToAnimator} that will animate the move to
	 *         the next station.
	 */
	abstract SimpleWalkToAnimator execute();
	
	/**
	 * Transforms list of {@link Node}s into a list of {@link NodeFigure}s. Also
	 * the first element of the list will be left out and therefore the size of
	 * new list is decreases by one.
	 * 
	 * @param walkingPath
	 *            The list of {@link Node}s to be transformed
	 * @return Transformed list
	 */
	protected List<NodeFigure> transformPath(List<Node> walkingPath) {
		Iterator<Node> it = walkingPath.iterator();
		List<NodeFigure> figurePath = new ArrayList<>();
		it.next(); // skip first element
		while (it.hasNext()) {
			figurePath.add(it.next().getNodeFigure());
		}
		return figurePath;
	}
}
