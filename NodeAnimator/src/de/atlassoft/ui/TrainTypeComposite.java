package de.atlassoft.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.model.ModelService;
import de.atlassoft.model.TrainType;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.atlassoft.util.ImageHelper;

/**
 * This class creates the composite where you can see
 * all existing train types.
 * 
 * @author Tobias Ilg
 */
public class TrainTypeComposite {

	private Composite trainTypeComposite;
	private I18NService I18N;
	private ApplicationService application;
	private List trainTypeList;
	
	/**
	 * Constructor for the TrainTypeComposite class
	 * 
	 * @param tabFolder
	 * 		The tab folder in which the composite should be placed.
	 */
	public TrainTypeComposite(TabFolder tabFolder, ApplicationService applicationService) {
		I18N = I18NSingleton.getInstance();
		this.application = applicationService;
		application.getModel().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ModelService.TRAIN_TYPES_PROPNAME)) {
					if (trainTypeList != null) {
						trainTypeList.removeAll();
						@SuppressWarnings("unchecked")
						java.util.List<TrainType> trainTypes = (java.util.List<TrainType>) evt.getNewValue();
						for (TrainType type : trainTypes) {
							trainTypeList.add(type.getName());
						}
						trainTypeList.setSelection(0);
					}
				}
			}
		});
		
		
		this.trainTypeComposite = new Composite (tabFolder, SWT.BORDER);
		trainTypeComposite.setLayout(new FillLayout());
		initUI();
	}
	
	/**
	 * A method which creates the content, 
	 * composites labels, buttons and a list of
	 * the composite of the TrainTypeComposite.
	 */
	
	private void initUI(){
		// TODO: Buttons Funktionen implementieren und autoresize Table
		//Left side of the composite
		Composite trainTypeCompositeLeft = new Composite (trainTypeComposite, SWT.NULL);
		trainTypeCompositeLeft.setLayout(new GridLayout(1, false));
		
		// Left Top side of the composite
		Composite trainTypeCompositeLeftTop = new Composite (trainTypeCompositeLeft, SWT.NULL);
		trainTypeCompositeLeftTop.setLayout(new GridLayout(2, false));
		
		// Label with Description
		final Label descriptionTrainTypeLabel = new Label(trainTypeCompositeLeftTop, SWT.LEFT);
		descriptionTrainTypeLabel.setText(I18N.getMessage("TrainTypeComposite.name"));
		descriptionTrainTypeLabel.setText(descriptionTrainTypeLabel.getText() + "\n");
		descriptionTrainTypeLabel.setText(descriptionTrainTypeLabel.getText() + I18N.getMessage("TrainTypeComposite.topSpeed") + "   ");
		descriptionTrainTypeLabel.setText(descriptionTrainTypeLabel.getText() + "\n");
		descriptionTrainTypeLabel.setText(descriptionTrainTypeLabel.getText() + I18N.getMessage("TrainTypeComposite.priority"));
		descriptionTrainTypeLabel.setText(descriptionTrainTypeLabel.getText() + "\n");
		
		// Label with Information of the selected TrainTyp
		final Label infoTrainTypeLabel = new Label(trainTypeCompositeLeftTop, SWT.RIGHT);
		
		// Left Bottom side of the Composite
	    Composite trainTypeCompositeLeftBottom = new Composite (trainTypeCompositeLeft, SWT.NULL);
		trainTypeCompositeLeftBottom.setLayout(new FillLayout());
		
		// Modify Button
		Button modify = new Button(trainTypeCompositeLeftBottom, SWT.PUSH);
		modify.setText(I18N.getMessage("TrainTypeComposite.buttonModify"));
		modify.setImage(ImageHelper.getImage("settingsIcon"));
	    
		// Delete Button
	 	Button delete = new Button(trainTypeCompositeLeftBottom, SWT.PUSH);
	 	delete.setText(I18N.getMessage("TrainTypeComposite.buttonDelete"));
	 	delete.setImage(ImageHelper.getImage("trashIcon"));
		
	 	// List of TrainTypes
	 	java.util.List<TrainType> trainTypes = application.getModel().getTrainTypes();
	 	trainTypeList = new List(trainTypeComposite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
	 	for (TrainType type : trainTypes) {
	 		trainTypeList.add(type.getName());
	 	}
	 	
		// Shows a text when no Traintype is selected
		getInformation(infoTrainTypeLabel, "Es wurde noch kein Zugtyp ausgewählt");
		// Shows the Information of te selected Traintype
		trainTypeList.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  String[] selection = trainTypeList.getSelection();
		    	  getInformation(infoTrainTypeLabel, selection[0]);
		      }
		});
	}
	
	/**
	 * 
	 * Creates the content of the infoTrainTypeLabel.
	 * First we read the Information, then we create
	 * the text.
	 * 
	 * @param infoTrainTypeLabel
	 * @param trainTypeName
	 */
	
	private void getInformation(Label infoTrainTypeLabel, String trainTypeName) {
		java.util.List<TrainType> trainTypes = application.getModel().getTrainTypes();
		for (TrainType type : trainTypes) {
			if (type.getName().equals(trainTypeName)) {
				// set values
				break;
			}
		}
		
		
		
//		infoTrainTypeLabel.setText(trainTypeName);
//		infoTrainTypeLabel.setText(infoTrainTypeLabel.getText() + "\n");
//		// no TrainType is selected
//		if (trainTypeName.equals("Es wurde noch kein Zugtyp ausgewählt")) {
//			infoTrainTypeLabel.setText(infoTrainTypeLabel.getText() + "N.A");
//			infoTrainTypeLabel.setText(infoTrainTypeLabel.getText() + " km/h");
//			infoTrainTypeLabel.setText(infoTrainTypeLabel.getText() + "\n");
//			infoTrainTypeLabel.setText(infoTrainTypeLabel.getText() + "N.A");
//			infoTrainTypeLabel.setText(infoTrainTypeLabel.getText() + "\n");	
//		    infoTrainTypeLabel.update();
//		// A TrainType is selected
//		} else {
//			infoTrainTypeLabel.setText(infoTrainTypeLabel.getText() + "100");
//			infoTrainTypeLabel.setText(infoTrainTypeLabel.getText() + " km/h");
//			infoTrainTypeLabel.setText(infoTrainTypeLabel.getText() + "\n");
//			infoTrainTypeLabel.setText(infoTrainTypeLabel.getText() + "5");
//			infoTrainTypeLabel.setText(infoTrainTypeLabel.getText() + "\n");	
//		    infoTrainTypeLabel.update();
//		}
	}
		
	/**
	 * Returns the trainTypeComposite
	 * 
	 * @return
	 * 		The composite
	 */
	public Composite getComposite(){
		return trainTypeComposite;
	}	
}
