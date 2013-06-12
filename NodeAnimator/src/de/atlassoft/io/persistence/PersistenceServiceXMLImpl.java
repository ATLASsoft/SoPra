package de.atlassoft.io.persistence;

import java.util.List;

import de.atlassoft.model.RailwaySystem;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.TrainType;

/**
 * Class which implements the PersistenceService
 * 
 * @author Linus and
 */
public class PersistenceServiceXMLImpl implements PersistenceService{

	@Override
	public void saveTrainType(TrainType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TrainType> getTrainTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteTrainType(TrainType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveSchedule(ScheduleScheme schedule) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ScheduleScheme> loadSchedules(String railSysID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteSchedules(String railSysID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveRailwaySystem(RailwaySystem railSys, String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RailwaySystem loadRailwaySystem(String railSysID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRailwaySystem(String railSysID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getRailwaySystemIDs() {
		// TODO Auto-generated method stub
		return null;
	}

}
