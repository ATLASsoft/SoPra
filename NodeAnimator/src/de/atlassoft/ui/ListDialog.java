package de.atlassoft.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * The class list dialog is for loading and deleting railsystems.
 * 
 * @author Silvan Haeussermann
 */
public class ListDialog {
	
	private Shell shell;
	
	public ListDialog(Display display, DialogMode dialogMode){
		//TODO: Ausprogrammieren
		shell = new Shell(display);
		shell.setText("Streckennetz laden");
		shell.setSize(200, 200);
		MainWindow.center(shell);
		shell.open();
	}
	
	private void initUI(){
		//TODO: implement method	
	}
}
