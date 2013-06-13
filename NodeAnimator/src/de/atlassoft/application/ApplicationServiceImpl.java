package de.atlassoft.application;

import java.util.Calendar;

import de.atlassoft.model.ModelService;
import de.atlassoft.model.ModelServiceImpl;
import de.atlassoft.model.Node;
import de.atlassoft.model.RailwaySystem;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.SimulationStatistic;
import de.atlassoft.model.TrainType;
import de.atlassoft.ui.MainWindow;
import de.atlassoft.ui.SplashScreen;
import de.atlassoft.util.ImageHelper;

public class ApplicationServiceImpl implements ApplicationService {

	private ModelService model;
	private MainWindow window;
	
	
	protected ApplicationServiceImpl() {
		initialize();
	}
	
	@Override
	public void initialize() {
		model = new ModelServiceImpl();
		// TODO: unvollständig
		
		// load images
		
		ImageHelper.createImage("greenCheck", "img/greencheck.png");
		ImageHelper.createImage("questionMark", "img/fragezeichen.jpg");
		ImageHelper.createImage("loadButton", "img/loadButton.png");
		ImageHelper.createImage("trainIcon", "img/trainIcon.png");
		ImageHelper.createImage("cancelIcon", "img/redX.png");
		ImageHelper.createImage("scheduleIcon", "img/scheduleIcon.png");
		ImageHelper.createImage("railwaySysIcon", "img/railSysIcon.png");
		ImageHelper.createImage("playIcon", "img/playButton.png");
		ImageHelper.createImage("questionMarkIcon", "img/question_mark.png");
		ImageHelper.createImage("trashIcon", "img/trashIcon.png");
		ImageHelper.createImage("ATLASsoftLogo", "img/ATLASsoftLogo.gif");
		ImageHelper.createImage("settingsIcon", "img/settingsIcon.png");
		
		//TODO: Wieder aktivieren
//		SplashScreen.showSplashScreen(3);		
		window = new MainWindow(this);
	}

	@Override
	public void shutDown() {
		// TODO: unvollständig
		window.close();
		ImageHelper.disposeImages();
	}

	@Override
	public ModelService getModel() {
		return model;
	}

	@Override
	public void startSimulation(Calendar time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseSimulation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void continueSimulation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void quitSimulation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addScheduleScheme(ScheduleScheme scheduleScheme) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteScheduleScheme(ScheduleScheme scheduleScheme) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveRailwaySystem(RailwaySystem railSys) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRailwaySystem(RailwaySystem railSys) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActiveRailwaySystem(RailwaySystem railsSys) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTrainType(TrainType trainType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteTrainType(TrainType trainType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showStatisticDoc(SimulationStatistic statistic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shwoScheduleDoc(ScheduleScheme scheduleScheme) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showDepartureBoardDoc(Node station) {
		// TODO Auto-generated method stub
		
	}

}
