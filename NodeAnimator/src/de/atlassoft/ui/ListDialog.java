package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.atlassoft.util.ImageHelper;

/**
 * The class list dialog is for loading and deleting railwaysystems.
 * 
 * @author Silvan Haeussermann
 */
public class ListDialog {
	
	private Shell shell;
	private Composite listComposite;
	private I18NService I18N;
	private List trainSysList;
	private ApplicationService applicationService;
	
	public ListDialog(ApplicationService applicationService) {
		
		I18N = I18NSingleton.getInstance();
		this.applicationService = applicationService;
		shell = new Shell(Display.getCurrent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(I18N.getMessage("ListDialog.Title"));
		shell.setImage(ImageHelper.getImage("trainIcon"));
		shell.setSize(500, 260);
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
		
		listComposite = new Composite(shell, SWT.NONE);
		GridLayout listGridLayout = new GridLayout();
		listGridLayout.numColumns = 3;
		listComposite.setLayout(listGridLayout);
		GridData listCoompGridData = new GridData();
		listCoompGridData.verticalAlignment = SWT.FILL;
		listCoompGridData.horizontalAlignment = SWT.FILL;
		listCoompGridData.grabExcessHorizontalSpace = true;
		listCoompGridData.grabExcessVerticalSpace = true;
		listComposite.setLayoutData(listCoompGridData);
		
		Font listFont = new Font(Display.getCurrent(), "Arial", 14, SWT.NONE);
		
		trainSysList = new List(listComposite, SWT.BORDER|SWT.V_SCROLL|SWT.SINGLE);
		trainSysList.setFont(listFont);
		GridData listGridData = new GridData();
		listGridData.horizontalSpan = 3;
		listGridData.horizontalAlignment = SWT.FILL;
		listGridData.verticalAlignment = SWT.FILL;
		listGridData.grabExcessVerticalSpace = true;
		trainSysList.setLayoutData(listGridData);
		//TODO: Mit richtigen Daten füllen
		trainSysList.add("Streckennetz 1");
		trainSysList.add("Streckennetz 2");
		trainSysList.add("Streckennetz 3");
		trainSysList.add("Streckennetz 4");
		trainSysList.add("Streckennetz 5");
		trainSysList.add("Streckennetz 6");
		trainSysList.add("Streckennetz 7");
		trainSysList.add("Streckennetz 8");
		trainSysList.add("Streckennetz 9");
		trainSysList.add("Streckennetz 10");
		
		
		Composite buttonComposite = new Composite(listComposite, SWT.NONE);
		GridData buttonGridData = new GridData();
		buttonGridData.horizontalAlignment = SWT.FILL;
		buttonGridData.grabExcessHorizontalSpace = true;
		buttonComposite.setLayoutData(buttonGridData);
		RowLayout buttonLayout = new RowLayout();
		buttonLayout.justify = true;
		buttonComposite.setLayout(buttonLayout);
		
		Button loadButton = new Button(buttonComposite, SWT.PUSH);
		loadButton.setText(I18N.getMessage("ListDialog.Load"));
		loadButton.setImage(ImageHelper.getImage("loadButton"));
		RowData loadRowData = new RowData(130, 52);
		loadButton.setLayoutData(loadRowData);
		loadButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TODO: Mit richtigen Daten ausführen
				applicationService.setActiveRailwaySystem(null);
			}
		});

		Button deleteButton = new Button(buttonComposite, SWT.PUSH);
		deleteButton.setText(I18N.getMessage("ListDialog.Delete"));
		deleteButton.setImage(ImageHelper.getImage("trashIcon"));
		RowData deleteRowData = new RowData(130, 52);
		deleteButton.setLayoutData(deleteRowData);	
		deleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TODO: Mit richtigen Daten füllen
				applicationService.deleteRailwaySystem(null);
			}
		});
		
		Button cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setText(I18N.getMessage("ListDialog.Cancel"));
		cancelButton.setImage(ImageHelper.getImage("cancelIcon"));
		RowData cancelRowData = new RowData(130, 52);
		cancelButton.setLayoutData(cancelRowData);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});		
	}
}
