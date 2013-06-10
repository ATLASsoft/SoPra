package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.atlassoft.ui.MainWindow;

/**
 * This class is for creating a new traintype.
 * 
 * @author Silvan Haeussermann, Tobias Ilg
 */
public class TrainTypeDialog {
	
	private Shell shell;
	
	public TrainTypeDialog(Display display){
		shell = new Shell(display);
		shell.setText("Zugtyp erstellen");
		shell.setSize(450, 200);
		MainWindow.center(shell);
		shell.setLayout(new GridLayout(3, false));
		initUI();
		shell.open();
	}
	
	private void initUI(){
		new Label(shell, SWT.NONE).setText("Name: ");
		Text name = new Text(shell, SWT.BORDER);
		name.setText("Name des Fahrplans");;
		new Label(shell, SWT.NONE);
		
		new Label (shell, SWT.NONE).setText("Höchstgeschwindigkeit: ");
		Text geschwindigkeit = new Text(shell, SWT.BORDER);
		geschwindigkeit.setText("Bitte geben Sie Ganzzahlige Werte ein");
		new Label(shell, SWT.NONE).setText("km/h");
		
		new Label(shell, SWT.NONE).setText("Priorität: ");
		final String[] prioritaeten = { "1", "2", "3", "4", "5" };
		Combo combo = new Combo(shell, SWT.DROP_DOWN);
	    combo.setItems(prioritaeten);
		new Label(shell, SWT.NONE);
		
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Button speichern = new Button(shell, SWT.PUSH);
		speichern.setText("Speicher");
	    Image speichernBild = new Image(null, "img/greencheck.png");
	    speichern.setImage(speichernBild);
		new Label(shell, SWT.NONE);
		Button abbrechen = new Button(shell, SWT.PUSH);
		abbrechen.setText("Abbrechen");
	    Image abbrechenBild = new Image(null, "img/redX.png");
	    abbrechen.setImage(abbrechenBild);
		shell.pack();
	}
}
