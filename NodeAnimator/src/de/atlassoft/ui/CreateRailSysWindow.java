package de.atlassoft.ui;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.hohenheim.view.map.NodeMap;

/**
 * This class is for creating a new railsystem.
 * 
 * @author Silvan Haeussermann
 */
public class CreateRailSysWindow {

	private Shell shell;
	private I18NService I18N;
	private Composite mainComposite;
	private StackLayout layout;
	private ApplicationService applicationService;
	
	/**
	 * Constructor for the CreateRailSysWindow Class
	 * 
	 * @param shell
	 * 			The current shell.
	 * @param mainComposite
	 * 		The composite of the main window.
	 * @param layout
	 * 		The stack layout of the main window.
	 * @param applicationService
	 * 		The ApplicationService of the program.
	 */
	public CreateRailSysWindow(Shell shell, Composite mainComposite, StackLayout layout, ApplicationService applicationService){
		this.applicationService = applicationService;
		this.shell = shell;
		this.layout = layout;
		this.mainComposite = mainComposite;
		I18N = I18NSingleton.getInstance();
		initUI();
	}
	
	/**
	 * Builds the elements of the UI
	 */
	private void initUI() {
	
	}
}
