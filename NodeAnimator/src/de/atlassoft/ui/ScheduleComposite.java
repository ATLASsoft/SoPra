package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;

/**
 * This class creates a composite for creating a new schedule.
 * 
 * @author Silvan Haeussermann
 */
public class ScheduleComposite {

	private Shell shell;
	private StackLayout layout;
	private Composite mainComposite;
	private Composite scheduleComposite;
	private I18NService I18N;
	
	/**
	 * Constructor for the ScheduleComposite class.
	 * 
	 * @param shell
	 * 		The current shell.
	 * @param mainComposite
	 * 		The composite of the main window.
	 * @param layout
	 * 		The stack layout of the main window.
	 */
	public ScheduleComposite(Shell shell, Composite mainComposite, StackLayout layout){
	
		this.shell = shell;
		this.layout = layout;
		this.mainComposite = mainComposite;
		I18N = I18NSingleton.getInstance();
		initUI();
	}
	
	/**
	 * Creates the UI elements of the ScheduleComposite
	 */
	private void initUI(){
		//TODO: noch etwas dran arbeiten
		scheduleComposite = new Composite (mainComposite, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		scheduleComposite.setLayout(gridLayout);
		
		Label nameLabel = new Label(scheduleComposite, SWT.NONE);
		nameLabel.setText("Name: ");
		
		Text nameField = new Text(scheduleComposite, SWT.SINGLE);
				
	}
	
	public Composite getComposite(){
		return scheduleComposite;
	}
}
