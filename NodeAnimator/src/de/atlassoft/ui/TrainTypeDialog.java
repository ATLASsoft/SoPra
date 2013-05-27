package de.atlassoft.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import de.atlassoft.ui.MainWindow;

public class TrainTypeDialog {
	
	private Shell shell;
	
	public TrainTypeDialog(Display display){
		
		shell = new Shell(display);
		shell.setText("Zugtyp erstellen");
		shell.setSize(200, 200);
		
		shell.open();
	}
	
	private void initUI(){
		//TODO: implement method	
	}
}
