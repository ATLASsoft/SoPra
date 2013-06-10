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
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;

/**
 * This class is for creating a new traintype.
 * 
 * @author Silvan Haeussermann, Tobias Ilg
 */
public class TrainTypeDialog {
	
	private Shell shell;
	
	public TrainTypeDialog(Display display) {
		shell = new Shell(display, SWT.DIALOG_TRIM);
		I18NService I18N = I18NSingleton.getInstance();
		shell.setText(I18N.getMessage("TrainTypeDialog.title"));
		shell.setSize(450, 200);
		shell.setLayout(new GridLayout(3, false));
		initUI();
		MainWindow.center(shell);
		shell.open();
	}
	
	private void initUI() {
		I18NService I18N = I18NSingleton.getInstance();
		
		// First row
		new Label(shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.labelNameOfSchedule"));
		Text name = new Text(shell, SWT.BORDER);
		name.setText(I18N.getMessage("TrainTypeDialog.textNameOfSchedule"));
		name.selectAll();
		new Label(shell, SWT.NONE);
		
		// Second row
		new Label (shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.label1Speed"));
		Text geschwindigkeit = new Text(shell, SWT.BORDER);
		geschwindigkeit.setText(I18N.getMessage("TrainTypeDialog.textSpeed"));
		geschwindigkeit.selectAll();
		new Label(shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.label2Speed"));
		
		// Third row
		new Label(shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.labelPriority"));
		final String[] prioritaeten = { "1", "2", "3", "4", "5" };
		Combo combo = new Combo(shell, SWT.DROP_DOWN);
	    combo.setItems(prioritaeten);
		new Label(shell, SWT.NONE);
		
		// Empty row
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		// Last row
		Button speichern = new Button(shell, SWT.PUSH);
		speichern.setText(I18N.getMessage("TrainTypeDialog.buttonSave"));
	    Image speichernBild = new Image(null, "img/greencheck.png");
	    speichern.setImage(speichernBild);
		new Label(shell, SWT.NONE);
		Button abbrechen = new Button(shell, SWT.PUSH);
		abbrechen.setText(I18N.getMessage("TrainTypeDialog.buttonCancle"));
	    Image abbrechenBild = new Image(null, "img/redX.png");
	    abbrechen.setImage(abbrechenBild);
		shell.pack();
	}
}
