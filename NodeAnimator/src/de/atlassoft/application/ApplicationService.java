package de.atlassoft.application;

import de.atlassoft.model.ModelService;

//TODO: Interface noch unvollst�ndig
public interface ApplicationService {

	void initialize();
	
	void shutDown();
	
	ModelService getModel();
}
