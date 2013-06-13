package de.atlassoft.ui;



import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.atlassoft.util.ImageHelper;

/**
 * This is the class for the about dialog where the name of our
 * company and the date is shown.
 * 
 * @author Silvan Haeussermann
 */
public class AboutDialog {
	
	private Shell shell;
	private I18NService I18N;
	
	/**
	 * Public constructor for the AboutDialog. Creates a new
	 * about dialog.
	 * 
	 * @param display
	 * 			The currently used display.
	 */
	public AboutDialog(Display display){
		
		I18N = I18NSingleton.getInstance();
		shell = new Shell(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(I18N.getMessage("AboutDialog.title"));
		shell.setImage(ImageHelper.getImage("trainIcon"));
		shell.setSize(300, 300);
		MainWindow.center(shell);
		initUI(display);
		shell.open();
	}
	
	private void initUI(Display display){
		//TODO: Verschönern, falls die Zeit reicht
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginTop = 10;
		rowLayout.marginBottom = 10;
		rowLayout.marginLeft = 80;
		rowLayout.marginRight = 5;
		rowLayout.type = SWT.VERTICAL;
		rowLayout.justify = true;
		rowLayout.center = true;
		shell.setLayout(rowLayout);
		
		Label atlassoft = new Label(shell, SWT.NONE);
//		Font f = new Font (display,"Calibri", 26, SWT.BOLD);
//		atlassoft.setText("ATLASsoft");
//		atlassoft.setFont(f);
//		atlassoft.setSize(20, 10);
		atlassoft.setImage(ImageHelper.getImage("ATLASsoftLogo"));
		
		
		Label alex = new Label(shell, SWT.NONE);
		alex.setText("Alexander Balogh");
		
		Label tobi = new Label(shell, SWT.NONE);
		tobi.setText("Tobias Ilg");
		
		Label linus = new Label(shell, SWT.NONE);
		linus.setText("Linus Neßler");
		
		Label andi = new Label(shell, SWT.NONE);
		andi.setText("Andreas Szlatki");
		
		Label silvan = new Label(shell, SWT.NONE);
		silvan.setText("Silvan Häußermann");
		
		Label copyright = new Label(shell, SWT.NONE);
		copyright.setText("(c) 2013");
	}
}
