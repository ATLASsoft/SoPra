package de.hohenheim.view.mobile.animation;

import java.util.ArrayList;
import java.util.Random;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.node.NodeFigure;
import de.hohenheim.view.path.PathFigure;

/**
 * Utility class for animation. 
 * @author Marc Fernandes
 *
 */
public class Utils {

	/**
	 * Randomly selects a path which begins at the specified node.
	 * @param {@link NodeMap} map - The nodeMap where the node is located. 
	 * @param {@link NodeFigure} start_node - The node is the starting point. 
	 * 		  We look randomly for a path which leads away from this node.
	 * @return {@link PathFigure} - The path we selected randomly.
	 */
	public static PathFigure getRandomPath(NodeMap map, NodeFigure start_node) {
		  ArrayList<PathFigure> edges = map.getPaths().get(start_node);	  
		  Random generator = new Random();
		  int index =  generator.nextInt(edges.size());	  
		  return (PathFigure)edges.get(index);
	}
	
	/**
	 * 1. Extracts segments out of one path. We use a {@link ManhattanConnectionRouter} to route the path
	 * so the polyline which is drawn as path can have orthogonal lines (with start and end point).This is why all these lines must be  
	 * separated into segments.
	 * 
	 * 2. If we have extracted the points, we calculate all points between all segments and add them as PointLists.
	 * We need this because we then can animate through every single point.
	 * 
	 * @param {@link PathFigure} path - The path from which we want to have the segments and PointLists of each segment.
	 * @param {@link NodeFigure} end_node - The RoomFigure to which the path leads. We need this because we need a direction.
	 * @param {@link Figure} mobile - the Figure we animate. 
	 * @return
	 */

	public static PointList getSegments(PathFigure path, NodeFigure end_node, Figure mobile) {		
		PointList pathPoints = new PointList();
		int x_offset = mobile.getBounds().width/2;
		int y_offset = mobile.getBounds().height/2;
		
		if(path == null) 
			return new PointList();
		
		Point start = path.getStart();
		Point end = path.getEnd();
		if(start==end)
			return new PointList();
		
		if(path.getSourceAnchor().getOwner()==end_node) {
			start=path.getEnd();
			end=path.getStart();
		}
		
		double x_start = start.preciseX();
		double y_start = start.preciseY();			
		double x_end = end.preciseX();
		double y_end = end.preciseY();
		
		double x_tot = Math.abs(x_end - x_start);
		double y_tot = Math.abs(y_end - y_start);			
		double alpha = Math.atan(x_tot/y_tot);
		
		double dx;
		double dy;
		int way=1;
		dx = way*Math.sin(alpha);
		dy = way*Math.cos(alpha);
		if(y_start>y_end) {
			dy=-dy;				
		} else if(y_start==y_end){
			dy=0;				
		}
		if(x_start>x_end) {
			dx=-dx;
		} else if(x_start==x_end) {
			dx=0;
		}
		
		double c = Math.sqrt(x_tot*x_tot + y_tot*y_tot)/way;
		long number = Math.round(c);
		
		for (long i=0; i<number;i++){
			Point tmp = new Point(x_start+(dx*i),y_start+(dy*i));
			tmp.translate(-x_offset,-y_offset);
			pathPoints.addPoint(tmp);
		}
		
		return pathPoints;
	}
}
