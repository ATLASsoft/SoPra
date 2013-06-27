package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
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
		scheduleAndTrainTypeComposite.setLayout(new FillLayout());
		initUI();
	}
	
	private void initUI(){
		// TODO: Implementieren
	}
	
	/**
	 * Returns the trainTypeComposite
	 * 
	 * @return
	 * 		The composite
	 */
	public Composite getComposite(){
		return scheduleAndTrainTypeComposite;
	}
}
