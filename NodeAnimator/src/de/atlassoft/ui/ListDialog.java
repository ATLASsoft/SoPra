package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.atlassoft.util.ImageHelper;

/**
 * The class list dialog is for loading and deleting railsystems.
 * 
 * @author Silvan Haeussermann
 */
public class ListDialog {
	
	private Shell shell;
	private Composite listComposite;
	private I18NService I18N;
	
	public ListDialog(DialogMode dialogMode) {
		
		I18N = I18NSingleton.getInstance();
		shell = new Shell(Display.getCurrent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Streckennetz laden");
		shell.setImage(ImageHelper.getImage("trainIcon"));
		shell.setSize(400, 300);
		GridLayout shellLayout = new GridLayout();
		shell.setLayout(shellLayout);
		GridData shellGridData = new GridData();
		shellGridData.verticalAlignment = SWT.FILL;
		shellGridData.horizontalAlignment = SWT.FILL;
		shell.setLayoutData(shellGridData);
		initUI();
		MainWindow.center(shell);
		shell.open();
	}
	
	private void initUI() {
		//TODO: implement method
		listComposite = new Composite(shell, SWT.NONE);
		GridLayout listGridLayout = new GridLayout();
		listGridLayout.numColumns = 2;
		listComposite.setLayout(listGridLayout);
		GridData listGridData = new GridData();
		listGridData.verticalAlignment = SWT.FILL;
		listGridData.horizontalAlignment = SWT.FILL;
		listGridData.grabExcessHorizontalSpace = true;
		listGridData.grabExcessVerticalSpace = true;
		listComposite.setLayoutData(listGridData);
		
		Table table = new Table(listComposite, SWT.NONE);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		GridData tableGridData = new GridData();
		tableGridData.verticalAlignment = SWT.FILL;
		tableGridData.horizontalAlignment = SWT.FILL;
		tableGridData.grabExcessHorizontalSpace = true;
		tableGridData.grabExcessVerticalSpace = true;
		tableGridData.horizontalSpan = 2;
		table.setLayoutData(tableGridData);
		
		TableColumn column = new TableColumn(table, SWT.LEFT);
		column.setText("Streckennetz");
		column.setWidth(400);
		
		for (int i=0; i<10; i++){
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText("test");
		}
		table.redraw();
		
		Composite buttonComposite = new Composite(listComposite, SWT.BORDER);
		GridData buttonGridData = new GridData();
		buttonGridData.horizontalAlignment = SWT.FILL;
		buttonGridData.grabExcessHorizontalSpace = true;
		buttonComposite.setLayoutData(buttonGridData);
		RowLayout buttonLayout = new RowLayout();
		buttonLayout.justify = true;
		buttonComposite.setLayout(buttonLayout);
		
		Button cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setText(I18N.getMessage("ListDialog.Cancel"));
		cancelButton.setImage(ImageHelper.getImage("cancelIcon"));
		GridData cancelGridData = new GridData();
		
		Button loadButton = new Button(buttonComposite, SWT.PUSH);
		loadButton.setText(I18N.getMessage("ListDialog.Load"));
		loadButton.setImage(ImageHelper.getImage("loadButton"));
		GridData loadGridData = new GridData();
		
		listComposite.layout();
	}
}
