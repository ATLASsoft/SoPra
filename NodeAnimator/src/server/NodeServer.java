package server;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.hohenheim.controller.ControllerCanvas;
import de.hohenheim.modell.Path;
import de.hohenheim.modell.RouteSectionMarker;
import de.hohenheim.modell.Train;
import de.hohenheim.view.map.NodeMap;

public class NodeServer {
	public static void main(String args[]) {
		
		Shell shell = new Shell();
		shell.setText("Train animation example");
		shell.setImage(new Image(null,"img/forklift-truck-logo.png"));
		shell.setSize(800, 550);
		shell.open();

		Composite nodeComposite = new Composite(shell, SWT.BORDER);
		nodeComposite.setLayout(new FillLayout());
		nodeComposite.setBackground(ColorConstants.black);
		nodeComposite.setBounds(10,10,600,500);
		Canvas c = new Canvas(nodeComposite, SWT.FILL);
		
		c.setBackground(ColorConstants.white);
		c.setBounds(0,0,600,500);
		NodeMap map = new NodeMap(c);				
		
		
	    RouteSectionMarker m_1     = new RouteSectionMarker(map,"1", 75, 25);
	    RouteSectionMarker m_2     = new RouteSectionMarker(map,"2", 25, 100);	    
	    RouteSectionMarker m_3     = new RouteSectionMarker(map,"3",25, 200);	    
	    RouteSectionMarker m_4     = new RouteSectionMarker(map,"4", 75, 300);	    
	    RouteSectionMarker m_5     = new RouteSectionMarker(map,"5", 75, 450);
	    RouteSectionMarker m_6 	   = new RouteSectionMarker(map,"6", 400, 150);
	    RouteSectionMarker m_7     = new RouteSectionMarker(map,"7", 500, 320);
	    RouteSectionMarker m_8     = new RouteSectionMarker(map,"8", 500, 220);
	    
	    new Path(map, m_1, m_2);	    
	    new Path(map, m_2, m_3);
	    new Path(map, m_1, m_4);
	    new Path(map, m_4, m_3);
	    new Path(map, m_4, m_5);
	    new Path(map, m_5, m_6);
	    new Path(map, m_7, m_6);			    
	    new Path(map, m_8, m_6);
	    
	    new Train(map, map.getNodes().get("1"), 1);
	    new Train(map, map.getNodes().get("2"), 2);

	    new ControllerCanvas(shell, SWT.FILL, map);
	    
		Display display = Display.getDefault();
		while (!shell.isDisposed()) {			
			if (!display.readAndDispatch()) {    	  			   
			   display.sleep();
		   }
		 }
	
	}
}

