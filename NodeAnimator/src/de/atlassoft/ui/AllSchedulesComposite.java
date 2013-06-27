package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
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

import de.atlassoft.application.ApplicationService;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
// TODO: Klasse umbennen in "ScheduleAndTrainTypeComposite"

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
	private ApplicationService applicationService;
	
	/**
	 * Constructor for the class AllScheduleComposite
	 * 
	 * @param tabFolder
	 */
	public AllSchedulesComposite(TabFolder tabFolder, ApplicationService applicationService){
		
		this.applicationService = applicationService;
		I18N = I18NSingleton.getInstance();
		allScheduleComposite = new Composite(tabFolder, SWT.BORDER);
		GridLayout allScheduleCompositeLayout = new GridLayout();
		allScheduleCompositeLayout.numColumns = 4;
		allScheduleComposite.setLayout(allScheduleCompositeLayout);
				
	    Label scheduleInformation = new Label(allScheduleComposite, SWT.BORDER);
	    GridData scheduleInformationData = new GridData();
	    scheduleInformationData.grabExcessHorizontalSpace = true;
	    scheduleInformationData.grabExcessVerticalSpace = true;
	    scheduleInformationData.verticalAlignment = SWT.FILL;
	    scheduleInformationData.horizontalAlignment = SWT.FILL;
	    scheduleInformation.setLayoutData(scheduleInformationData);
	    
//	    ActiveSchedules list
	    activeSchedules = new List(allScheduleComposite, SWT.BORDER|SWT.V_SCROLL|SWT.SINGLE);
	    for(ScheduleScheme schedule : applicationService.getModel().getActiveScheduleSchemes()) {
	    	activeSchedules.add(schedule.getID());
	    }
	    if (activeSchedules.getItemCount() < 0) {
	    	activeSchedules.select(0);	    	
	    }
	    
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
	    for (ScheduleScheme schedule : applicationService.getModel().getPassiveScheduleSchemes()) {
	    	passiveSchedules.add(schedule.getID());
	    }
	    if (passiveSchedules.getItemCount() > 0) {
	    	passiveSchedules.select(0);
	    }
	    
	    setDragDrop(passiveSchedules);
	    setDragDrop(activeSchedules);
	}
	
	/**
	 * Adds Drag and Drop Listeners to the given list.
	 * 
	 * @param list
	 * 			The list
	 */
	private void setDragDrop (final List list) {
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		
		DragSource source = new DragSource(list, DND.DROP_MOVE);
		source.setTransfer(types);
		source.addDragListener(new DragSourceListener() {
			@Override
			public void dragFinished(DragSourceEvent event) {
				if (event.detail == DND.DROP_MOVE){
					list.remove(list.getSelectionIndex());
					list.select(0);
				}
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				event.data = list.getItem(list.getSelectionIndex());
			}

			@Override
			public void dragStart(DragSourceEvent event) {
				if (list.getItemCount() == 0) {
					event.doit = false;
				}
			}			
		});
		
		DropTarget target = new DropTarget(list, DND.DROP_MOVE);
		target.setTransfer(types);
		target.addDropListener(new DropTargetAdapter() {
			@Override
			public void drop(DropTargetEvent event){
				list.add((String) event.data);
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
