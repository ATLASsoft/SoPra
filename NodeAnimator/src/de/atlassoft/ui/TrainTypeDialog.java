package de.atlassoft.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import de.atlassoft.ui.MainWindow;

/**
 * This class is for creating a new traintype.
 * 
 * @author Silvan Haeussermann
 */
public class TrainTypeDialog {
	
	private Shell shell;
	
	public TrainTypeDialog(Display display){
		//TODO: Ausprogrammieren
		shell = new Shell(display);
		shell.setText("Zugtyp erstellen");
		shell.setSize(200, 200);
		MainWindow.center(shell);
		shell.open();
	}
	
	private void initUI(){
		//TODO: implement method	
	}
}
