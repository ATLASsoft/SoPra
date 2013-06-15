package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

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
	
	/**
	 * Constructor for the TrainTypeComposite class
	 * 
	 * @param tabFolder
	 * 		The tab folder in which the composite should be placed.
	 */
	public TrainTypeComposite(TabFolder tabFolder){
		I18N = I18NSingleton.getInstance();
		this.trainTypeComposite = new Composite (tabFolder, SWT.BORDER);
		trainTypeComposite.setLayout(new FillLayout());
		initUI();
	}
	
	
	private void initUI(){
		// TODO: Buttons Funktionen implementieren und autoresize Table
		Composite trainTypeCompositeSplit1 = new Composite (trainTypeComposite, SWT.NULL);
		trainTypeCompositeSplit1.setLayout(new GridLayout(1, true));
		
		// Table with Information of selected TrainType
		final Table infoTrainTypeTable = new Table(trainTypeCompositeSplit1, SWT.BORDER);
		new TableColumn(infoTrainTypeTable, SWT.NONE);
		new TableColumn(infoTrainTypeTable, SWT.NONE);
	    infoTrainTypeTable.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	    
	    Composite trainTypeCompositeSplit2 = new Composite (trainTypeCompositeSplit1, SWT.NULL);
		trainTypeCompositeSplit2.setLayout(new FillLayout());
		
		// Cancel Button
		Button modify = new Button(trainTypeCompositeSplit2, SWT.PUSH);
		modify.setText(I18N.getMessage("TrainTypeComposite.buttonModify"));
		modify.setImage(ImageHelper.getImage("settingsIcon"));
	    
		// Cancel Button
	 	Button delete = new Button(trainTypeCompositeSplit2, SWT.PUSH);
	 	delete.setText(I18N.getMessage("TrainTypeComposite.buttonDelete"));
	 	delete.setImage(ImageHelper.getImage("trashIcon"));
		
	 	// List of TrainTypes
		final String[] ITEMS = { "Zugtyp A", "Zugtyp B", "Zugtyp C", "Zugtyp Deutschland" };
		final List trainTypeList = new List(trainTypeComposite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		for (int i = 0, n = ITEMS.length; i < n; i++) {
			trainTypeList.add(ITEMS[i]);
		}
		trainTypeList.select(0);
		getInformation(infoTrainTypeTable, ITEMS, 0);
		trainTypeList.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		        getInformation(infoTrainTypeTable, ITEMS, trainTypeList.getSelectionIndex());
		      }
		});
	}
	
	private static void getInformation(Table infoTrainTypeTable, String[] ITEMS, int Index){
		//TODO: XML auslesen und Infos erlangen
		infoTrainTypeTable.clearAll();
		infoTrainTypeTable.setItemCount(0);
		TableItem name = new TableItem(infoTrainTypeTable, SWT.NONE);
	    name.setText(0, "Name");
	    name.setText(1, ITEMS[Index]);
		TableItem topSpeed = new TableItem(infoTrainTypeTable, SWT.NONE);
	    topSpeed.setText(0, "Höchstgeschwindigkeit");
	    topSpeed.setText(1, "100" + " km/h");
		TableItem priority = new TableItem(infoTrainTypeTable, SWT.NONE);
	    priority.setText(0, "Priorität");
	    priority.setText(1, "5");
	    infoTrainTypeTable.getColumn(0).pack ();
	    infoTrainTypeTable.getColumn(1).pack ();
	    infoTrainTypeTable.update();
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
