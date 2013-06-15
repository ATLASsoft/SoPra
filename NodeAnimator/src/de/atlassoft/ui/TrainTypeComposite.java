package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;

/**
 * This class creates the composite where you can see
 * all existing traintypes.
 * 
 * @author Silvan Haeussermann
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
		

	    Table infoTrainType = new Table(trainTypeComposite, SWT.BORDER);
	    TableColumn column1 = new TableColumn(infoTrainType, SWT.NONE);
	    TableColumn column2 = new TableColumn(infoTrainType, SWT.NONE);
		
		final String[] ITEMS = { "Zugtyp A", "Zugtyp B", "Zugtyp C", "Zugtyp D" };
		List trainType = new List(trainTypeComposite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		for (int i = 0, n = ITEMS.length; i < n; i++) {
		      trainType.add(ITEMS[i]);
		}
		trainType.select(0);
		getInformation(infoTrainType, ITEMS, trainType.getSelectionIndex());
		
	}
	
	private static void getInformation(Table infoTrainType, String[] ITEMS, int Index){
		//TODO: XML auslesen und Infos erlangen
		TableItem item1 = new TableItem(infoTrainType, SWT.NONE);
	    item1.setText(0, "Name");
	    item1.setText(1, "Zugtyp A");
		TableItem item2 = new TableItem(infoTrainType, SWT.NONE);
	    item2.setText(0, "Höchstgeschwindigkeit");
	    item2.setText(1, "100" + " km/h");
		TableItem item3 = new TableItem(infoTrainType, SWT.NONE);
	    item3.setText(0, "Priorität");
	    item3.setText(1, "5");
	    infoTrainType.getColumn(0).pack ();
	    infoTrainType.getColumn(1).pack ();
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
