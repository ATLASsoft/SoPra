package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;

/**
 * This class creates a composite for the home screen.
 * 
 * @author Silvan Haeussermann
 */
public class HomeScreenComposite {
	
	private Shell shell;
	private StackLayout layout;
	private Composite mainComposite;
	private Composite homeScreenComposite;
	private I18NService I18N;
	
	/**
	 * The constructor for the HomeScreenComposite class.
	 * 	
	 * @param shell
	 * 			The main shell of the application.
	 * @param mainComposite
	 * 			The main composite of the application
	 * @return
	 * 			The homescreen composite.
	 */
	public HomeScreenComposite(Shell shell, Composite mainComposite, StackLayout layout) {
		
		I18N = I18NSingleton.getInstance();
		this.shell = shell;
		this.mainComposite = mainComposite;
		this.layout = layout;
		
		initUI();
	}
	
	/**
	 * Creates the UI elements.
	 */
	private void initUI(){
		
		homeScreenComposite = new Composite(mainComposite, SWT.BORDER);
		GridLayout gridLayout = new GridLayout();
		homeScreenComposite.setLayout(gridLayout);
		TabFolder tabFolder = new TabFolder (homeScreenComposite, SWT.NONE);
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		tabFolder.setLayoutData(gridData);
		TabItem trainTypeItem = new TabItem(tabFolder, SWT.NONE);
		trainTypeItem.setText("Train Type");
//		tabFolder.setSize(mainComposite.getSize().x-10, mainComposite.getSize().y-10);
		
	}
	
	public Composite getComposite(){
		return homeScreenComposite;
	}
	
	/**
	 * The method createHomeScreenComposite creates the composite for the
	 * homescreen, which is shown when the application starts.
	 * 	
	 * @param shell
	 * 			The main shell of the application.
	 * @param mainComposite
	 * 			The main composite of the application
	 * @return
	 * 			The homescreen composite.
	 */
	public static Composite createHomeScreenComposite(Shell shell, Composite mainComposite){
		//TODO: Ausprogrammieren

	    Composite homeScreenComposite = new Composite(mainComposite, SWT.NONE);
	    homeScreenComposite.setLayout(new RowLayout());
	    Label label = new Label(homeScreenComposite, SWT.NONE);
	    label.setText("homescreen");
	    label.pack();
		
		return homeScreenComposite;
	}
}
