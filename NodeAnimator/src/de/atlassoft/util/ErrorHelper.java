package de.atlassoft.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * This class helps to create a error message which is shown as an
 * extra shell.
 * 
 * @author Silvan Haeussermann
 */
public class ErrorHelper {

	/**
	 * Creates a window with an error message and error title.
	 * 
	 * @param title
	 * 		The title for the window
	 * @param message
	 * 		The message that should be shown.
	 */
	public static void createErrorMessage(String title, String message) {
		
		MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR);
		messageBox.setText(title);
		messageBox.setMessage(message);
		messageBox.open();
		
	}
	
}
