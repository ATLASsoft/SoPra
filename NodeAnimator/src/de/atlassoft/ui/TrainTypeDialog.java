package de.atlassoft.ui;



import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.model.TrainType;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.atlassoft.util.ImageHelper;

/**
 * This class is for the TrainType Dialog where
 * you can creating a new traintype.
 * 
 * @author Silvan Haeussermann, Tobias Ilg
 */
public class TrainTypeDialog {
	
	private Shell shell;
	private ApplicationService application;
	
	/**
	 * Public constructor for the TrainType Dialog. Creates a new
	 * about shell and open it.
	 * 
	 * @param application
	 * 			??
	 */
	
	public TrainTypeDialog(ApplicationService application) {
		this.application = application;
		Display display = Display.getCurrent();
//		Image appIcon = new Image (null, "img/trainTypeIcon.png");
		shell = new Shell(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		I18NService I18N = I18NSingleton.getInstance();
		shell.setText(I18N.getMessage("TrainTypeDialog.title"));
		shell.setSize(450, 200);
		shell.setImage(ImageHelper.getImage("trainIcon"));
		shell.setImage(ImageHelper.getImage("trainTypTitle"));
		shell.setLayout(new GridLayout(3, false));
		initUI(display);
		MainWindow.center(shell);
		shell.open();
	}
	
	/**
	 * A method which creates the contet, 
	 * label, text, buttons and tooltips of
	 * the shell of the TrainType Dialog.
	 * 
	 * @param display
	 * 			The currently used display.
	 */
	
	private void initUI(final Display display) {
		final I18NService I18N = I18NSingleton.getInstance();
		
		// First row
		new Label(shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.labelNameOfTrainType"));
		final Text name = new Text(shell, SWT.BORDER);
		new Label(shell, SWT.NONE);
		name.setToolTipText(I18N.getMessage("TrainTypeDialog.textNameOfTrainType"));
		
		// Second row
		new Label (shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.label1Speed"));
		final Text speed = new Text(shell, SWT.BORDER);
		// Only numbers are allowed to enter
		speed.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if (!Character.isDigit(e.character) && !Character.isISOControl(e.character)) {
		          e.doit = false;
		        }
		      }
		});
		new Label(shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.label2Speed"));
		speed.setToolTipText(I18N.getMessage("TrainTypeDialog.textSpeed"));
		
		// Third row
		new Label(shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.labelPriority"));
		final String[] prioritySelection = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		final Combo comboPriority = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
	    comboPriority.setItems(prioritySelection);
	    comboPriority.select(0);
		new Label(shell, SWT.NONE);
		comboPriority.setToolTipText(I18N.getMessage("TrainTypeDialog.textPriority"));
		
		
		// Empty row
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		// Last row
		Button save = new Button(shell, SWT.PUSH);
		save.setText(I18N.getMessage("TrainTypeDialog.buttonSave"));
	    Image saveBild = new Image(null, "img/greencheck.png");
	    save.setImage(saveBild);
	    save.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	        	final String nameOfTrainType = name.getText();
	        	final String topSpeed = speed.getText();
	        	final String priority = comboPriority.getText();
	        	//TODO: �bergabe an ApplicationService implementieren
	        	if (name.getText() == ""){
	        	    MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
	        	    messageBox.setText(I18N.getMessage("TrainTypeDialog.errorMissingInput"));
	        	    messageBox.setMessage(I18N.getMessage("TrainTypeDialog.errorEmptyTextfield"));
	        	    messageBox.open();	        	    
	        	}else if (speed.getText() == ""){
	        	    MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
	        	    messageBox.setText(I18N.getMessage("TrainTypeDialog.errorMissingInput"));
	        	    messageBox.setMessage(I18N.getMessage("TrainTypeDialog.errorEmptyTextfield"));
	        	    messageBox.open();	
	        	}else{	        		
	        		System.out.print(nameOfTrainType + " " + topSpeed + " " + priority);
	        		MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
	        		messageBox.setText(I18N.getMessage("TrainTypeDialog.informationSaved"));
	        	    messageBox.setMessage(I18N.getMessage("TrainTypeDialog.informationTrainTypeSaved"));
	        	    shell.close();
	        	    messageBox.open();
	        	}
	        }
	    });
	    
		new Label(shell, SWT.NONE);
		
		Button cancel = new Button(shell, SWT.PUSH);
		cancel.setText(I18N.getMessage("TrainTypeDialog.buttonCancel"));
	    Image cancelBild = new Image(null, "img/redX.png");
	    cancel.setImage(cancelBild);
	    cancel.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	          shell.close();
	        }
	    });
		shell.pack();
		
	}
}
