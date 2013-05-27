package de.atlassoft.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SaveRailSysDialog {
	
	private Shell shell;
	
	public SaveRailSysDialog(Display display){
		
		shell = new Shell(display);
		shell.setText("Streckennetz speichern");
		shell.setSize(200, 200);
		
		shell.open();
	}
	
	private void initUI(){
		//TODO: implement method	
	}
}
