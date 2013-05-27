package de.atlassoft.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class ListDialog {
	private Shell shell;
	
	public ListDialog(Display display, DialogMode dialogMode){
		
		shell = new Shell(display);
		shell.setText("Streckennetz speichern");
		shell.setSize(200, 200);
		
		shell.open();
	}
	
	private void initUI(){
		//TODO: implement method	
	}
}
