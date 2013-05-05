package de.hohenheim.view.mobile.animation.exceptions;

public class PathNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;
	
    public PathNotFoundException() {
    	super("No path could be found between the two Nodes ...");
	}	
    
    public PathNotFoundException(String pathname1, String pathname2) {
    	super("No path could be found between the two Nodes "+pathname1+" and "+ pathname2);
    }
}