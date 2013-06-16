package de.atlassoft.ui;



import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
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
 * you can create a new train type.
 * 
 * @author Tobias Ilg
 */
public class TrainTypeDialog {
	//TODO: Überprüfen ob TrainType Name bereits vergeben ist, Möglichkeit Bilder einzufügen und on the fly Korrektur statt Fehlermeldung bei ungültiger eingabe
	private Shell shell;
	private ApplicationService application;
	
	/**
	 * Public constructor for the TrainType Dialog. Creates a new
	 * about shell and open it.
	 * 
	 * @param application
	 * 			to access application layer
	 */
	
	public TrainTypeDialog(ApplicationService application) {
		this.application = application;
		I18NService I18N = I18NSingleton.getInstance();
		
		Display display = Display.getCurrent();
		shell = new Shell(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(I18N.getMessage("TrainTypeDialog.title"));
		shell.setSize(450, 200);
		shell.setImage(ImageHelper.getImage("trainIcon"));
		shell.setLayout(new GridLayout(3, false));
		
		initUI();
		MainWindow.center(shell);
		shell.open();
	}
	
	/**
	 * A method which creates the content, 
	 * label, text, buttons and tool tips of
	 * the shell of the TrainType Dialog.
	 * 
	 * @param display
	 * 			The currently used display.
	 */
	
	private void initUI() {
		final I18NService I18N = I18NSingleton.getInstance();
		final Display display = Display.getCurrent();
		
		// First row with a label and a text for the name
		new Label(shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.labelNameOfTrainType"));
		final Text name = new Text(shell, SWT.BORDER);
		new Label(shell, SWT.NONE);
		name.setToolTipText(I18N.getMessage("TrainTypeDialog.textNameOfTrainType"));
		// ALEX HELP! :D
//		final java.util.List<TrainType> trainTypes = application.getModel().getTrainTypes();
//		name.addListener(SWT.KeyDown, new Listener() {
//		      public void handleEvent(Event e) {
//		    	  for (TrainType type : trainTypes) {
//		    		  if (type.getName().equals(name.getText())) {
//		    			  Label error = new Label(shell, SWT.NONE);
//		    			  error.setText(I18N.getMessage("TrainTypeDialog.labelErrorName"));
//		    			  error.setForeground(display.getSystemColor(SWT.COLOR_RED));
//		    		  } else {
//		    			  new Label(shell, SWT.NONE);
//		    		  }
//		    	  }
//		      }
//		});

		
		// Second row with two labels and a text for the topSpeed
		new Label (shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.label1Speed"));
		final Text speed = new Text(shell, SWT.BORDER);
		// Only numbers are allowed to enter
		speed.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if (!Character.isDigit(e.character) && !Character.isISOControl(e.character)) {
		          e.doit = false;
		          display.beep();
		        }
		      }
		});
		new Label(shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.label2Speed"));
		speed.setToolTipText(I18N.getMessage("TrainTypeDialog.textSpeed"));
		
		// Third row with a label and a combo for the Priority (1-5)
		new Label(shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.labelPriority"));
		final String[] prioritySelection = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
		final Combo comboPriority = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
	    comboPriority.setItems(prioritySelection);
	    comboPriority.select(0);
		new Label(shell, SWT.NONE);
		comboPriority.setToolTipText(I18N.getMessage("TrainTypeDialog.textPriority"));
		
		// Third (empty) row for the optics
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		// Fourth row with three buttons for Save, Information and Cancel
		// Save Button
		Button save = new Button(shell, SWT.PUSH);
		save.setText(I18N.getMessage("TrainTypeDialog.buttonSave"));
	    save.setImage(ImageHelper.getImage("greenCheck"));
	    save.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	        	// read values
	        	final String nameOfTrainType = name.getText();
	        	final String topSpeed = speed.getText();
	        	final String priority = comboPriority.getText();	        	
	        	
	        	// check constraints
	        	List<String> errorMessages = new ArrayList<String>();
	        	
	        	if (name.getText().trim().equals("")) {
	        	    errorMessages.add(I18N.getMessage("TrainTypeDialog.errorEmptyName"));   	    
	        	}
	        	if (speed.getText().trim().equals("")) {
	        		errorMessages.add(I18N.getMessage("TrainTypeDialog.errorEmptySpeed"));
	        	}
	        	
	        	// generating and open ErrorMessageBox
	        	if (!errorMessages.isEmpty()) {
	        		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
	        	    messageBox.setText(I18N.getMessage("TrainTypeDialog.errorMissingInput"));
	        	    int number = errorMessages.size();
	        	    if (number == 2) {
	        	    	messageBox.setMessage(errorMessages.get(0) + "\n" + errorMessages.get(1));
		        	    messageBox.open();
	        	    } else {
	        	    	messageBox.setMessage(errorMessages.get(0));
		        	    messageBox.open();
	        	    }
	        	
	        	// surrender Inputs to ApplicationService and generating and open InformationMessageBox
	        	} else {
	        		TrainType type = new TrainType(nameOfTrainType, Double.parseDouble(topSpeed), Integer.parseInt(priority));
	        		application.getModel().addTrainType(type);
	        		MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
	        		messageBox.setText(I18N.getMessage("TrainTypeDialog.informationSaved"));
	        	    messageBox.setMessage(I18N.getMessage("TrainTypeDialog.informationTrainTypeSaved"));
	        	    shell.setVisible(false);
	        	    messageBox.open();
	        	    shell.close();
	        	}
	        }
	    });
	    
	    // Information Button
		Button help = new Button(shell, SWT.PUSH);
		help.setImage(ImageHelper.getImage("questionMark"));
		help.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	        	MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
        		messageBox.setText(I18N.getMessage("TrainTypeDialog.helpTitle"));
        	    messageBox.setMessage(I18N.getMessage("TrainTypeDialog.helpText"));
        	    messageBox.open();
	        }
	    });
		
		// Cancel Button
		Button cancel = new Button(shell, SWT.PUSH);
		cancel.setText(I18N.getMessage("TrainTypeDialog.buttonCancel"));
	    cancel.setImage(ImageHelper.getImage("cancelIcon"));
	    cancel.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	        	MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION |SWT.YES | SWT.NO);
	            messageBox.setMessage(I18N.getMessage("TrainTypeDialog.cancelQuestion"));
	            int rc = messageBox.open();
	            if (rc == SWT.YES) {
	            	shell.close();
	            }
	        }
	    });
	    
		shell.pack();
	}
}
