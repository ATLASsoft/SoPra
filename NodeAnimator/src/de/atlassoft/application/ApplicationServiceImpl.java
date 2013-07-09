package de.atlassoft.application;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Observer;

import org.eclipse.swt.graphics.Image;

import de.atlassoft.ai.AIService;
import de.atlassoft.ai.AIServiceImpl;
import de.atlassoft.io.persistence.PersistenceService;
import de.atlassoft.io.persistence.PersistenceServiceImpl;
import de.atlassoft.model.ModelService;
import de.atlassoft.model.ModelServiceImpl;
import de.atlassoft.model.Node;
import de.atlassoft.model.RailwaySystem;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.SimulationStatistic;
import de.atlassoft.model.TrainType;
import de.atlassoft.ui.MainWindow;
import de.atlassoft.util.ErrorHelper;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.atlassoft.util.ImageHelper;

public class ApplicationServiceImpl implements ApplicationService {

	private ModelServiceImpl model;
	private PersistenceService persistence;
	private AIService ai;
	private I18NService I18N;
	
	
	protected ApplicationServiceImpl() {
		initialize();
	}
	
	
	
	
	@Override
	public void initialize() {
		// initialize service intances
		model = new ModelServiceImpl();
		persistence = new PersistenceServiceImpl();
		ai = new AIServiceImpl(this);
		I18N = I18NSingleton.getInstance();
		// TODO: unvollständig
		
		// load images
		ImageHelper.loadImage("greenCheck", "img/greencheck.png");
		ImageHelper.loadImage("questionMark", "img/fragezeichen.jpg");
		ImageHelper.loadImage("loadButton", "img/loadButton.png");
		ImageHelper.loadImage("trainIcon", "img/trainIcon.png");
		ImageHelper.loadImage("cancelIcon", "img/redX.png");
		ImageHelper.loadImage("scheduleIcon", "img/scheduleIcon.png");
		ImageHelper.loadImage("railwaySysIcon", "img/railSysIcon.png");
		ImageHelper.loadImage("playIcon", "img/playButton.png");
		ImageHelper.loadImage("questionMarkIcon", "img/question_mark.png");
		ImageHelper.loadImage("trashIcon", "img/trashIcon.png");
		ImageHelper.loadImage("ATLASsoftLogo", "img/ATLASsoftLogo.gif");
		ImageHelper.loadImage("settingsIcon", "img/settingsIcon.png");
		ImageHelper.loadImage("standardTrainIcon", "img/train.gif");
		ImageHelper.loadImage("questionMarkSmall", "img/questionMarkSmall.png");
		ImageHelper.loadImage("crosshair", "img/crosshair.png");
		ImageHelper.loadImage("trashIconSmall", "img/trashIconSmall.png");
		ImageHelper.loadImage("playIconSmall", "img/playIconSmall.png");
		ImageHelper.loadImage("pauseIconSmall", "img/pauseIconSmall.png");
		ImageHelper.loadImage("stopIconSmall", "img/stopIconSmall.png");
		ImageHelper.loadImage("pdfIcon", "img/pdfIcon.png");
		
		// load train types
		try {
			for (String temp: persistence.getRailwaySystemIDs()){
				model.addRailwaySystemID(temp);
			}
			List<TrainType> trainTypes = persistence.loadTrainTypes();
			for (TrainType type : trainTypes) {
				model.addTrainType(type);
			}
		} catch (IOException e) {
			e.printStackTrace();
			ErrorHelper.createErrorMessage(
					I18N.getMessage("ApplicationService.LoadErrorTitle"),
					I18N.getMessage("ApplicationService.TrainTypeErrorMsg"));
		}
		
		//TODO: Wieder aktivieren
//		SplashScreen.showSplashScreen(3);		
		new MainWindow(this);
	}

	@Override
	public void shutDown() {
		// TODO: unvollständig
		if (ai != null && ai.isRunning()) {
			ai.finishSimulation();
		}
		ImageHelper.disposeImages();
	}

	@Override
	public ModelService getModel() {
		return model;
	}

	@Override
	public void startSimulation(Calendar time, Observer o) {
		RailwaySystem railSys = model.getActiveRailwaySys();
		List<ScheduleScheme> schemes = model.getActiveScheduleSchemes();
		if (!ai.isRunning() && railSys != null) {
			ai.startSimulation(time, railSys, schemes, o);
		}
		
	}

	@Override
	public void pauseSimulation() {
		ai.pauseSimulation();
	}

	@Override
	public void continueSimulation() {
		ai.continueSimulation();
	}

	@Override
	public void quitSimulation() {
		ai.finishSimulation(); //TODO: statistik
	}

	@Override
	public void setTimeLapse(int timeLapse) {
		if (timeLapse < 1 || timeLapse > 3000) {
			throw new IllegalArgumentException("timeLapse must be between 1 and 3000, is: " + timeLapse);
		}
		ai.setTimeLapse(timeLapse);
	}
	
	@Override
	public void addScheduleScheme(ScheduleScheme scheduleScheme) {
		try {
			persistence.saveSchedule(scheduleScheme);
			model.addActiveScheduleScheme(scheduleScheme);
		} catch (IOException e) {
			e.printStackTrace();
			ErrorHelper.createErrorMessage(
					I18N.getMessage("ApplicationService.SaveErrorTitle"),
					I18N.getMessage("ApplicationService.SaveSchemeErrorMsg"));
		}
		
	}

	@Override
	public void deleteScheduleScheme(ScheduleScheme scheduleScheme) {
		try {
			persistence.deleteSingleSchedule(scheduleScheme);
			model.removeActiveScheduleScheme(scheduleScheme);
			model.removePassiveScheduleScheme(scheduleScheme);
		} catch (IOException e) {
			e.printStackTrace();
			ErrorHelper.createErrorMessage(
					I18N.getMessage("ApplicationService.DeleteErrorTitle"),
					I18N.getMessage("ApplicationService.DeleteSchemeErrorMsg"));
		}
	}

	@Override
	public void saveRailwaySystem(RailwaySystem railSys) {
		try {
			persistence.saveRailwaySystem(railSys);
			model.addRailwaySystemID(railSys.getID());
			setActiveRailwaySystem(railSys.getID());
		} catch (IOException e) {
			e.printStackTrace();
			ErrorHelper.createErrorMessage(
					I18N.getMessage("ApplicationService.SaveErrorTitle"),
					I18N.getMessage("ApplicationService.SaveRailSysErrorMsg"));
		}
	}

	@Override
	public void deleteRailwaySystem(String railSysID) {
		try {
			persistence.deleteRailwaySystem(railSysID);
			persistence.deleteSchedules(railSysID);
			model.removeRailwaySystemID(railSysID);
			
			RailwaySystem activeRailSys = model.getActiveRailwaySys();
			if (activeRailSys != null && activeRailSys.getID().equals(railSysID)) {
				model.setActiveRailwaySys(null);
			}
		} catch (IOException e) {
			e.printStackTrace();
			ErrorHelper.createErrorMessage(
					I18N.getMessage("ApplicationService.DeleteErrorTitle"),
					I18N.getMessage("ApplicationService.DeleteRailSysErrorMsg"));
		}
	}

	@Override
	public void setActiveRailwaySystem(String railsSysID) {
		try {
			RailwaySystem railSys = persistence.loadRailwaySystem(railsSysID);
			model.setActiveRailwaySys(railSys);
			List<ScheduleScheme> schemes = persistence.loadSchedules(railSys, model.getTrainTypes());
			for (ScheduleScheme s : schemes) {
				model.addActiveScheduleScheme(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
			ErrorHelper.createErrorMessage(
					I18N.getMessage("ApplicationService.LoadErrorTitle"),
					I18N.getMessage("ApplicationService.LoadRailSysErrorMsg"));
		}
	}

	@Override
	public void addTrainType(TrainType trainType) {
		try {
			persistence.saveTrainType(trainType);
			model.addTrainType(trainType);
		} catch (IOException e) {
			e.printStackTrace();
			ErrorHelper.createErrorMessage(
					I18N.getMessage("ApplicationService.SaveErrorTitle"),
					I18N.getMessage("ApplicationService.SaveTrainTypeErrorMsg"));
		}
	}

	@Override
	public void deleteTrainType(TrainType trainType) {
		try {
			persistence.deleteTrainType(trainType);
			persistence.deleteSchedules(trainType);
			model.deleteTrainType(trainType);
		} catch (IOException e) {
			e.printStackTrace();
			ErrorHelper.createErrorMessage(
					I18N.getMessage("ApplicationService.DeleteErrorTitle"),
					I18N.getMessage("ApplicationService.DeleteTrainTypeErrorMsg"));
		}
	}

	@Override
	public void showStatisticDoc(SimulationStatistic statistic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showScheduleDoc(ScheduleScheme scheduleScheme) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showDepartureBoardDoc(Node station) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isConnected(RailwaySystem railSys) {
		if (railSys == null) {
			throw new IllegalArgumentException("railSys must not be null");
		}
		return ai.isConnected(railSys);
	}


	@Override
	public int getFastestTravelTime(RailwaySystem railSys, Node start, Node goal, double topSpeed) {
		return ai.fastestArrival(railSys, start, goal, topSpeed);
	}

}
