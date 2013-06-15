package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;

/**
 * This class creates a composite where you can see
 * all the schedules of the current railway system.
 * 
 * @author Silvan Haeussermann
 */
public class AllSchedulesComposite {
	
	final private List activeSchedules;
	final private List passiveSchedules;
	private Composite allScheduleComposite;
	private I18NService I18N;
	
	/**
	 * Constructor for the class AllScheduleComposite
	 * 
	 * @param tabFolder
	 */
	public AllSchedulesComposite(TabFolder tabFolder){
		
		I18N = I18NSingleton.getInstance();
		allScheduleComposite = new Composite(tabFolder, SWT.BORDER);
		GridLayout allScheduleCompositeLayout = new GridLayout();
		allScheduleCompositeLayout.numColumns = 4;
		allScheduleComposite.setLayout(allScheduleCompositeLayout);
				
	    Label scheduleInformation = new Label(allScheduleComposite, SWT.BORDER);
	    GridData scheduleInformationData = new GridData();
//	    scheduleInformationData.grabExcessHorizontalSpace = true;
//	    scheduleInformationData.grabExcessVerticalSpace = true;
//	    scheduleInformationData.verticalAlignment = SWT.FILL;
//	    scheduleInformationData.horizontalAlignment = SWT.FILL;
	    scheduleInformation.setLayoutData(scheduleInformationData);
	    
//	    ActiveSchedules list
	    activeSchedules = new List(allScheduleComposite, SWT.BORDER|SWT.V_SCROLL|SWT.SINGLE);
	    activeSchedules.add("Fahrplan 1");
	    activeSchedules.add("Fahrplan 2");
	    activeSchedules.add("Fahrplan 3");
	    activeSchedules.add("Fahrplan 4");
	    activeSchedules.select(0);
	    DragSource drag = new DragSource(activeSchedules, DND.DROP_COPY|DND.DROP_MOVE);
	    drag.addDragListener(new DragSourceListener() {

			@Override
			public void dragFinished(DragSourceEvent event) {
				if (event.detail == DND.DROP_DEFAULT){
					System.out.println("Drag Finished");
				}
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				
			}

			@Override
			public void dragStart(DragSourceEvent event) {
				System.out.println("Drag Start");
			}	    	
	    });
	    
	    Composite buttonComposite = new Composite(allScheduleComposite, SWT.BORDER);
	    FillLayout buttonLayout = new FillLayout();
	    buttonComposite.setLayout(buttonLayout);
	    buttonLayout.type = SWT.VERTICAL;
	    
	    // <- Button
	    Button setScheduleActive = new Button(buttonComposite, SWT.PUSH);
	    setScheduleActive.setText("<-");
	    setScheduleActive.setToolTipText(I18N.getMessage("AllSchedulesComposite.SetActiveTooltip"));
	    setScheduleActive.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		if (passiveSchedules.getItemCount() == 0) {	    			
	    			MessageBox errorMessageBox = new MessageBox(new Shell(), SWT.ICON_ERROR);
	    			errorMessageBox.setText(I18N.getMessage("AllSchedulesComposite.ErrorTitle"));
	    			errorMessageBox.setMessage(I18N.getMessage("AllSchedulesComposite.NoPassiveError"));
	    			errorMessageBox.open();	    			
	    		} else {
	    			setActive();
	    			passiveSchedules.select(0);
	    		}
	    	}
	    });
	    
	    // -> Button
	    Button setSchedulePassive = new Button(buttonComposite, SWT.PUSH);
	    setSchedulePassive.setText("->");
	    setSchedulePassive.setToolTipText(I18N.getMessage("AllSchedulesComposite.SetPassiveTooltip"));
	    setSchedulePassive.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		if (activeSchedules.getItemCount() == 0) {
	    			MessageBox errorMessageBox = new MessageBox(new Shell(), SWT.ICON_ERROR);
	    			errorMessageBox.setText(I18N.getMessage("AllSchedulesComposite.ErrorTitle"));
	    			errorMessageBox.setMessage(I18N.getMessage("AllSchedulesComposite.NoActiveError"));
	    			errorMessageBox.open();
	    		} else {
		    		setPassive();
		    		activeSchedules.select(0);
	    		}    		
	    	}
	    });
	    
	    //passiveSchedules list
	    passiveSchedules = new List (allScheduleComposite, SWT.BORDER|SWT.V_SCROLL|SWT.SINGLE);
	    passiveSchedules.add("schedule 1");
	    passiveSchedules.add("schedule 2");
	    passiveSchedules.add("schedule 3");
	    passiveSchedules.add("schedule 4");
	    passiveSchedules.select(0);
	    DropTarget target = new DropTarget(passiveSchedules, DND.DROP_MOVE|DND.DROP_DEFAULT);
	    target.addDropListener(new DropTargetListener() {

			@Override
			public void dragEnter(DropTargetEvent event) {
				System.out.println("drag enter");
			}

			@Override
			public void dragLeave(DropTargetEvent event) {
			}

			@Override
			public void dragOperationChanged(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void dragOver(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void drop(DropTargetEvent event) {
				System.out.println("Ich wurde gedroppt");
			}

			@Override
			public void dropAccept(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}	    	
	    });	    
	}
	
	/**
	 * Moves the selected schedule from the passiveSchedules list to
	 * the activeSchedules list.
	 */
	private void setActive() {
		int select = passiveSchedules.getSelectionIndex();
		activeSchedules.add(passiveSchedules.getItem(passiveSchedules.getSelectionIndex()));
		passiveSchedules.remove(select);
		activeSchedules.select(select);
	}
	
	/**
	 * Moves the selected schedule from the activeSchedules list to
	 * the passiveSchedules list. 
	 */
	private void setPassive() {
		int select = activeSchedules.getSelectionIndex();
		passiveSchedules.add(activeSchedules.getItem(activeSchedules.getSelectionIndex()));
		activeSchedules.remove(select);
		passiveSchedules.select(select);
	}
	
	/**
	 * Returns the allScheduleComposite.
	 * 
	 * @return
	 * 		The composite.
	 */
	public Composite getComposite(){
		return allScheduleComposite;
	}
}
