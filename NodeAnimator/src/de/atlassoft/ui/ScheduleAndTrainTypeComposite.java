package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;

public class ScheduleAndTrainTypeComposite {
	
	private Composite scheduleAndTrainTypeComposite;
	private I18NService I18N;
	private ApplicationService application;
	
	public ScheduleAndTrainTypeComposite(TabFolder tabFolder, ApplicationService applicationService) {
		I18N = I18NSingleton.getInstance();
		this.application = applicationService;
		this.scheduleAndTrainTypeComposite = new Composite (tabFolder, SWT.BORDER);
		scheduleAndTrainTypeComposite.setLayout(new RowLayout());
		initScheduleUI();
		initTrainTypeUI();
	}
	
	private void initScheduleUI(){
		// TODO: ImplementierenO
	}
	
	private void initTrainTypeUI(){
		// TODO: ImplementierenO
	}
	
	/**
	 * Returns the scheduleAndTrainTypeComposite
	 * 
	 * @return
	 * 		The composite
	 */
	public Composite getComposite(){
		return scheduleAndTrainTypeComposite;
	}
}
