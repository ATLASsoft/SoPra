package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
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
	private Shell shell;
	private ApplicationService applicationService;
	
	/**
	 * Public constructor for the TrainType Dialog. Creates a new
	 * TrainTypeCreate shell and open it.
	 * 
	 * @param applicationService
	 * 			to access application layer
	 */
	
	public TrainTypeDialog(ApplicationService applicationService) {
		this.applicationService = applicationService;
		I18NService I18N = I18NSingleton.getInstance();
		
		Display display = Display.getCurrent();
		shell = new Shell(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(I18N.getMessage("TrainTypeDialog.titleGenerate"));
		shell.setSize(SWT.DEFAULT, SWT.DEFAULT);
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
	 */
	private void initUI() {
		final I18NService I18N = I18NSingleton.getInstance();
		final Display display = Display.getCurrent();
		
		// GridDatas to optimize the Layout
		GridData dataFill = new GridData(GridData.FILL_HORIZONTAL);
		GridData dataCenter = new GridData(GridData.FILL);
		dataCenter.horizontalAlignment = GridData.CENTER;
		
		// First row with a label and a text for the name
		new Label(shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.labelNameOfTrainType"));
		final Text name = new Text(shell, SWT.BORDER);
		name.setLayoutData(dataFill);
		new Label(shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.labelNameOfTrainTypeLength"));
		
		new Label(shell, SWT.NONE);
		final Label errorField = new Label(shell, SWT.NONE);
		errorField.setText(I18N.getMessage("TrainTypeDialog.ErrorField.NoName") + "      ");
		errorField.setForeground(display.getSystemColor(SWT.COLOR_RED));
		errorField.setSize(20, 100);
		errorField.setVisible(true);		
		name.setToolTipText(I18N.getMessage("TrainTypeDialog.textNameOfTrainType"));
		new Label(shell, SWT.NONE);
		
		// Second row with two labels and a text for the topSpeed
		new Label (shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.label1Speed"));
		final Text textSpeed = new Text(shell, SWT.BORDER);
		textSpeed.setLayoutData(dataFill);
		new Label(shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.label2Speed"));
		
		new Label(shell, SWT.NONE);
		final Label errorField2 = new Label(shell, SWT.NONE);
		errorField2.setText(I18N.getMessage("TrainTypeDialog.ErrorField2.NoSpeed"));
		errorField2.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		textSpeed.setToolTipText(I18N.getMessage("TrainTypeDialog.textSpeed"));
		// Only numbers are allowed to enter
		textSpeed.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if (!Character.isDigit(e.character) && !Character.isISOControl(e.character)) {
		          e.doit = false;
		          display.beep();
		        }
			}
		});
		new Label(shell, SWT.NONE);
		
		// Third row with a label and a combo for the Priority (1-5)
		new Label(shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.labelPriority"));
		final String[] prioritySelection = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
		final Combo comboPriority = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
	    comboPriority.setItems(prioritySelection);
	    comboPriority.select(0);
//	    comboPriority.setLayoutData(dataCenter);
		new Label(shell, SWT.NONE);
		comboPriority.setToolTipText(I18N.getMessage("TrainTypeDialog.textPriority"));
		
		// Third row with text and button, to get an image
		new Label(shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.labelImage1"));
		final Text textImage = new Text(shell, SWT.READ_ONLY | SWT.BORDER);
		textImage.setLayoutData(dataFill);
		textImage.setToolTipText(I18N.getMessage("TrainTypeDialog.toolTipImage"));
		new Label(shell, SWT.NONE).setText(I18N.getMessage("TrainTypeDialog.labelImage2"));
		
		// Search row for the optics
		new Label(shell, SWT.NONE);
		Button imageSearch = new Button(shell, SWT.NULL);
		imageSearch.setText(I18N.getMessage("TrainTypeDialog.buttonImage"));
		imageSearch.setLayoutData(dataFill);
		imageSearch.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	    		String selected;
	        	FileDialog fd = new FileDialog(shell, SWT.OPEN);
	            fd.setText(I18N.getMessage("TrainTypeDialog.openFileDialog"));
	            fd.setFilterPath("C:/");
	            String[] filterN = {"Alle Bilddateien", "Bitmap (*.bmp)", "JointPhotographersExpertGroup (*.jpg, *.jpeg, *.jpe)",
	            		"GraphicImageFormat (*.gif)", "Icon (*.ico)", "WindowsMetaFile (*.wmf)", "Komprimierte Bitmap (*.pcx)",
	            		"Portable Network Graphics (*.png)", "CorelDraw (*.cdr)", "CorelPaint (*.cpt)", "Picture-Bitmap-Date (*.pic)"};
	            fd.setFilterNames(filterN);
	            String[] filterExt = { "*.jpg; *.jpeg; *.jpe; *.tif; *.tiff; *.ico; " +
	            		"*.ani; *.wmf; *.pcx; *.cdr; *.cpt; *.pci; *.png; *.gif; *.bmp",
	            		"*.bmp", "*.jpg; *.jpeg; *.jpe", "*.gif", "*.ico", "*.wmf", "*.pcx",
	            		"*.png", "*.cdr", "*.cpt", "*.pci" };
	            fd.setFilterExtensions(filterExt);
	            while ((selected =fd.open()) != null) {
	            	Image imageTrainType = new Image (null, selected);
	            	Rectangle xy = imageTrainType.getBounds();
	            	if (xy.width == 32 && xy.height == 32) {
	            		textImage.setText(selected);
	            		break;
	            	} else {
	            		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
	            		messageBox.setText("TrainTypeDialog.errorWrongBoundsTitle");
	            		messageBox.setMessage(I18N.getMessage("TrainTypeDialog.errorWrongBoundsMessage"));
	            		messageBox.open();
	            	}
	            }
	        }
	    });
		new Label(shell, SWT.NONE);
		
		//Empty Row
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);		
		
		// Fourth row with three buttons for Save, Information and Cancel
		// Save Button
		final Button save = new Button(shell, SWT.PUSH);
		save.setText(I18N.getMessage("TrainTypeDialog.buttonSave"));
	    save.setImage(ImageHelper.getImage("greenCheck"));
	    save.setEnabled(false);
	    save.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	        	// read values
	        	final String nameOfTrainType = name.getText().trim();
	        	final String topSpeed = textSpeed.getText();
	        	final String priority = comboPriority.getText();	        	
	        	
	        	TrainType type = new TrainType(nameOfTrainType, Double.parseDouble(topSpeed), Integer.parseInt(priority));
	       		if (!textImage.getText().equals("")){
	       			Image img = new Image(null, textImage.getText());
	       			type.setImg(img);
	       		} else {
	       			type.setImg(null);
	       		}
	       		applicationService.addTrainType(type);
	       		MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
	       		messageBox.setText(I18N.getMessage("TrainTypeDialog.informationSaved"));
        	    messageBox.setMessage(I18N.getMessage("TrainTypeDialog.informationTrainTypeSaved"));
        	    shell.setVisible(false);
        	    messageBox.open();
	            shell.close();
	        }
	    });
	    
	    // Information Button
		Button help = new Button(shell, SWT.PUSH);
		help.setImage(ImageHelper.getImage("questionMark"));
	    help.setLayoutData(dataCenter);
		help.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	        	MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
        		messageBox.setText(I18N.getMessage("TrainTypeDialog.helpTitle"));
        	    messageBox.setMessage(I18N.getMessage("TrainTypeDialog.helpText"));
        	    messageBox.open();
	        }
	    });
		
		// Cancel Button
		Button cancel = new Button(shell, SWT.PUSH | SWT.RIGHT);
		cancel.setText(I18N.getMessage("TrainTypeDialog.buttonCancel"));
	    cancel.setImage(ImageHelper.getImage("cancelIcon"));
	    GridData dataCancel = new GridData(GridData.FILL);
		dataCancel.horizontalAlignment = GridData.END;
	    cancel.setLayoutData(dataCancel);
	    cancel.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	        	shell.close();
	        }
	    });
	    
	    name.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				// check if the entered name have less than 25 signs
				if (name.getText().length() >= 25) {
					if (e.keyCode == 8) {
						e.doit = true;
					} else {
						e.doit = false;
						display.beep();
					}
				}
			}
	    });
	    
	    // Name On the Fly Exception
		name.addListener(SWT.KeyUp, new Listener() {
			public void handleEvent(Event e) {
				// check whether the field is empty
				if (name.getText().trim().equals("")) {
					errorField.setText(I18N.getMessage("TrainTypeDialog.ErrorField.NoName"));
					errorField.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		    		save.setEnabled(false);
		    		errorField.setVisible(true);
		    		return;
		    	}				
				// check if the entered name is already allocate
				Boolean twice = false;
				final java.util.List<TrainType> trainTypes = applicationService.getModel().getTrainTypes();
		    	for (TrainType type : trainTypes) {
		    		// .replaceAll(" ", "")
		    		if (type.getName().toLowerCase().equals((name.getText()).toLowerCase().trim())) {
		   				twice = true;
		   			}
		   		}
		    	if (twice == true) {
		    		errorField.setText(I18N.getMessage("TrainTypeDialog.ErrorField.ExistingName"));
		    		errorField.setVisible(true);
		   		  	save.setEnabled(false);
		    	} else {
		    		errorField.setVisible(false);
		    		if (errorField2.getVisible() == false) {
		    			save.setEnabled(true);
		    		}
		    	}
			}
		});
		
		// Speed On the Fly Exception
		textSpeed.addListener(SWT.KeyUp, new Listener() {
			public void handleEvent(Event e) {
				// check whether the field is empty
				if (textSpeed.getText().trim().equals("")) {
					errorField2.setText(I18N.getMessage("TrainTypeDialog.ErrorField2.NoSpeed"));
					errorField2.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		    		save.setEnabled(false);
		    		errorField2.setVisible(true);
		    		return;
		    	// check if the entered number not start with a 0
				} else if (textSpeed.getText().substring(0, 1).equals("0")){
        			errorField2.setText(I18N.getMessage("TrainTypeDialog.ErrorField2.ZeroSpeed"));
					errorField2.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
					save.setEnabled(false);
		    		errorField2.setVisible(true);
		    	// if everything allrigth 
        		} else {
		    		errorField2.setVisible(false);
		    		if (errorField.getVisible() == false) {
        				save.setEnabled(true);
        			}
        		}
			}
		});
	    
		shell.pack();
	}
}
