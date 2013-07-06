package de.atlassoft.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
import de.atlassoft.model.ModelService;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.atlassoft.util.ImageHelper;

/**
 * The class list dialog is for loading and deleting railway systems.
 * 
 * @author Silvan Haeussermann
 */
public class RailSysDialog implements PropertyChangeListener {
	//TODO: Fehler abfangen, wenn letztes Streckennetz gelöscht wird
	private Shell shell;
	private I18NService I18N;
	private List trainSysList;
	private ApplicationService applicationService;
	private ModelService model;
	
	public RailSysDialog(ApplicationService applicationService) {
		
		I18N = I18NSingleton.getInstance();
		this.applicationService = applicationService;
		model = applicationService.getModel();
		model.addPropertyChangeListener(this);
		
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
		shell.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				model.removePropertyChangeListener(RailSysDialog.this);
			}
		});
		
		initUI();
		MainWindow.center(shell);
		shell.open();
	}
	
	private void initUI() {
		
		Composite listComposite = new Composite(shell, SWT.NONE);
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
		java.util.List<String> railSysIDs = model.getRailwaySystemIDs();
		for (String id : railSysIDs) {
			trainSysList.add(id);
		}
		trainSysList.select(0);
		
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
				applicationService.setActiveRailwaySystem(trainSysList.getItem(trainSysList.getSelectionIndex()));
				shell.close();
				shell.dispose();
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
				applicationService.deleteRailwaySystem(trainSysList.getItem(trainSysList.getSelectionIndex()));
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
				shell.dispose();
			}
		});		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ModelService.RAILSYS_IDS_PROPNAME)) {
			trainSysList.removeAll();
			@SuppressWarnings("unchecked")
			java.util.List<String> railSysIDs = (java.util.List<String>) evt.getNewValue();
			for (String id : railSysIDs) {
				trainSysList.add(id);
			}
		}
	}
}
