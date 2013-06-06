package de.atlassoft.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This class is for saving a railsystem.
 * 
 * @author Silvan
 */
public class SaveRailSysDialog {
	
	private Shell shell;
	
	public SaveRailSysDialog(Display display){
		//TODO: Ausprogrammieren
		shell = new Shell(display);
		shell.setText("Streckennetz speichern");
		shell.setSize(200, 200);
		MainWindow.center(shell);
		shell.open();
	}
	
	private void initUI(){
		//TODO: implement method	
	}
}