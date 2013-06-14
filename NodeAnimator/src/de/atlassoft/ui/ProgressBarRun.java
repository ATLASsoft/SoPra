package de.atlassoft.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * This class is for running the progressBar
 * 
 * @author Tobias Ilg
 *
 */

public class ProgressBarRun extends Thread {
	private Display display;
	private ProgressBar progressBar;
	private int duration;
	private Shell shell;

	/**
	 * Public constructor which gets the params
	 * 
	 * @param display
	 * @param progressBar
	 * @param shell
	 * @param duration
	 */
	
	public ProgressBarRun(Display display, ProgressBar progressBar,
			Shell shell, int duration) {
		this.display = display;
		this.progressBar = progressBar;
		this.duration = duration;
		this.shell = shell;
	}

	/**
	 * A method which will run the over hand progressBar
	 */
	
	public void run() {
		for (int i = 0; i < duration*100; i++) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			display.asyncExec(new Runnable() {
				public void run() {
					progressBar.setSelection(progressBar.getSelection() + 1);
					if (progressBar.getSelection() == progressBar.getMaximum()) {
						progressBar.dispose();
						shell.dispose();
						return;
					}
				}
			});
		}
	}
}
