package de.atlassoft.ui;



import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * This is the class for the about dialog where the name of our
 * company and the date is shown.
 * 
 * @author Silvan Haeussermann
 */
public class AboutDialog {
	
	private Shell shell;

	/**
	 * Public constructor for the AboutDialog. Creates a new
	 * about dialog.
	 * 
	 * @param display
	 * 			The currently used display.
	 */
	public AboutDialog(Display display){
		
		shell = new Shell(display);
		shell.setText("�ber");
		shell.setSize(300, 250);
		MainWindow.center(shell);
		initUI(display);
		shell.open();
	}
	
	private void initUI(Display display){
		//TODO: Versch�nern, falls die Zeit reicht
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
		Font f = new Font (display,"Calibri", 26, SWT.BOLD);
		atlassoft.setText("ATLASsoft");
		atlassoft.setFont(f);
		atlassoft.setSize(20, 10);
		
		Label alex = new Label(shell, SWT.NONE);
		alex.setText("Alexander Balogh");
		
		Label silvan = new Label(shell, SWT.NONE);
		silvan.setText("Silvan H�u�ermann");
		
		Label tobi = new Label(shell, SWT.NONE);
		tobi.setText("Tobias Ilg");
		
		Label linus = new Label(shell, SWT.NONE);
		linus.setText("Linus Ne�ler");
		
		Label andi = new Label(shell, SWT.NONE);
		andi.setText("Andreas Szlatki");
		
		Label copyright = new Label(shell, SWT.NONE);
		copyright.setText("(c) 2013");
	}
}