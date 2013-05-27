package de.atlassoft.ui;


import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MainWindow {

	private Shell shell;
	
	public MainWindow(Display display){
		
		Shell shell = new Shell(display);
		shell.setText("Choo Choo Motherfucker");
		shell.setSize(640, 480);
		
		center(shell);
		
		shell.open();
		
		while(!shell.isDisposed()){
			if(!display.readAndDispatch()){
				display.sleep();
			}
		}		
	}
	
	public void center(Shell shell){
		
		Rectangle bdc = shell.getDisplay().getBounds();
		
		Point p = shell.getSize();
		
		int Left = (bdc.width - p.x) / 2;
		int Top = (bdc.height - p.y) / 2;
		
		shell.setBounds(Left, Top, p.x, p.y);
	}
	
	public static void main(String[] args){
		Display display = new Display();
		new MainWindow(display);
		display.dispose();
	}
}
