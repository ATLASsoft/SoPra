package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;


public class SplashScreen {
	
	public static void showSplashScreen(int duration) {
		Display display = Display.getCurrent();
		Shell shell = new Shell(display, SWT.NO_TRIM);
		shell.setLayout(new GridLayout());
		shell.setSize(450, 475);
		MainWindow.center(shell);
		
		final GifCLabel label = new GifCLabel(shell, SWT.CENTER);
		label.setGifImage("img/ATLASsoftLogo1.gif");
		
		ProgressBar pb1 = new ProgressBar(shell, SWT.HORIZONTAL | SWT.SMOOTH);
	    pb1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    pb1.setMinimum(0);
	    pb1.setMaximum(duration*100);
	    
	    shell.open();
	    ProgressBarRun run = new ProgressBarRun(display, pb1, shell, duration);
	    run.start();
		while(!shell.isDisposed()){
			if(!display.readAndDispatch()){
				display.sleep();
			}
		}
	}	
}