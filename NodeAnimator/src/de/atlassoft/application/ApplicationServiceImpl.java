package de.atlassoft.application;

import de.atlassoft.model.ModelService;
import de.atlassoft.model.ModelServiceImpl;
import de.atlassoft.ui.MainWindow;

public class ApplicationServiceImpl implements ApplicationService {

	private ModelService model;
	private MainWindow window;
	
	
	protected ApplicationServiceImpl() {
		
	}
	
	@Override
	public void initialize() {
		model = new ModelServiceImpl();
		// ... model mit werten füllen
		
		//window = new MainWindow(this);

	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub
		// window.dispose();
		
	}

	@Override
	public ModelService getModel() {
		return model;
	}

}
