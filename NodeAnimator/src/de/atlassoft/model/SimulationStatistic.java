package de.atlassoft.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the statistic of one simulation pass.
 * 
 * @author Tobias Ilg
 *
 */
public class SimulationStatistic {
	
	/**
	 * The {@link RailwaySystem} on witch the simulation, affiliated with this
	 * statistic took place.
	 */
	private RailwaySystem railSys;

	/**
	 * This List contains all {@link TrainRideStatistic}s.
	 */
	java.util.List<TrainRideStatistic> trainRideStatistic;



	/**
	 * This is the constructor where the List will initialized
	 */
	public SimulationStatistic(RailwaySystem railSys) {
		trainRideStatistic = new ArrayList<TrainRideStatistic>();
		this.railSys = railSys;
	}



	/**
	 * This method adds the specified {@link TrainRideStatistic} to this
	 * {@link SimulationStatistic}.
	 * 
	 * @param statistic {@link TrainRideStatistic} to be added
	 */
	public synchronized void addStatistic(TrainRideStatistic statistic) {
		trainRideStatistic.add(statistic);
	}

	/**
	 * Returns all {@link TrainRideStatistic} instances that have been added to
	 * this class.
	 * 
	 * @return Unmodifiable list of all {@link TrainRideStatistic} instances
	 */
	public List<TrainRideStatistic> getStatistics() {
		return Collections.unmodifiableList(trainRideStatistic);
	}

	/**
	 * This method will calculate the mean delay of the whole simulation.
	 * 
	 * @return meanDelay
	 */
	public double getMeanDelay() {
		double meanDelay;
		meanDelay = getTotalDelay() / getNumberOfRides();
		return meanDelay;
	}
	
	/**
	 * This method will calculate the total delay of the whole simulation.
	 * 
	 * @return totalDelay
	 */
	public double getTotalDelay() {
		double totalDelay = 0.0;
		for (TrainRideStatistic statistic : trainRideStatistic) {
			java.util.List<Node> stationList = statistic.getStations();
			for (Node station : stationList) {
				if (statistic.getDelay(station) > 0) {
					totalDelay = totalDelay + statistic.getDelay(station);
				}
			}
 		}
		return totalDelay;
	}
	
	/**
	 * This method will calculate the mean delay
	 * of the specified {@link TrainType} of the whole simulation.
	 * 
	 * @param trainType
	 * @return meanDelay
	 */
	public double getMeanDelay(TrainType trainType) {
		double meanDelay = 0.0;
		for (TrainRideStatistic statistic : trainRideStatistic) {
			if (statistic.getScheduleScheme().getTrainType().equals(trainType)) {
				java.util.List<Node> stationList = statistic.getStations();
				for (Node station : stationList) {
					if (statistic.getDelay(station) > 0) {
						meanDelay = meanDelay + statistic.getDelay(station);
					}
				}
			}
		}
		meanDelay = meanDelay / getNumberOfRides(trainType);
		return meanDelay;
	}
	
	/**
	 * This method will calculate the mean delay
	 * of the specified {@link Node} of the whole simulation
	 * 
	 * @param station
	 * @return meanDelay
	 */
	public double getMeanDelay(Node station) {
		double meanDelay = 0.0;
		int numberOfRides = 0;
		for (TrainRideStatistic statistic : trainRideStatistic) {
			java.util.List<Node> stationList = statistic.getStations();
			for (Node allStation : stationList) {
				if (allStation.equals(station)) {
					if (statistic.getDelay(station) > 0) {
						meanDelay = meanDelay + statistic.getDelay(station);
						numberOfRides++;
					}
				}
			}
 		}
		meanDelay = meanDelay / numberOfRides;
		return meanDelay;
	}
	
	/**
	 * This method will calculate the mean delay of
	 * the specified {@link ScheduleScheme} of the whole simulation.
	 * 
	 * @param scheduleScheme
	 * @return meanDelay
	 */
	public double getMeanDelay(ScheduleScheme scheduleScheme) {
		double meanDelay = 0.0;
		for (TrainRideStatistic statistic : trainRideStatistic) {
			if (statistic.getScheduleScheme().equals(scheduleScheme)) {
				java.util.List<Node> stationList = statistic.getStations();
				for (Node station : stationList) {
					if (statistic.getDelay(station) > 0) {
						meanDelay = meanDelay + statistic.getDelay(station);
					}
				}
			}
 		}
		meanDelay = meanDelay / getNumberOfRides(scheduleScheme);
		return meanDelay;
	}
	
	/**
	 * This method will calculate the mean delay of the specified
	 * {@link ScheduleScheme} and the specified {@link Node} of the whole
	 * simulation.
	 * 
	 * @param scheduleScheme
	 * @param station
	 * @return meanDelay
	 */
	public double getMeanDelay(ScheduleScheme scheduleScheme, Node station) {
		double meanDelay = 0.0;
		for (TrainRideStatistic statistic : trainRideStatistic) {
			if (statistic.getScheduleScheme().equals(scheduleScheme)) {
				java.util.List<Node> stationList = statistic.getStations();
				for (Node allStation : stationList) {
					if (allStation.equals(station)) {
						if (statistic.getDelay(station) > 0) {
							meanDelay = meanDelay + statistic.getDelay(station);
						}
					}
				}
			}
 		}
		meanDelay = meanDelay / getNumberOfRides(scheduleScheme);
		return meanDelay;
	}
	
	/**
	 * This method will get the number of all train rides
	 * of the whole simulation.
	 * 
	 * @return numberOfRides
	 */
	public int getNumberOfRides() {
		int numberOfRides = trainRideStatistic.size();
		return numberOfRides;
	}
	
	/**
	 * This method will get the number of all train rides
	 * of the specified {@link TrainType} of the whole simulation.
	 * 
	 * @param trainType
	 * @return numberOfRides
	 */
	public int getNumberOfRides(TrainType trainType) {
		int numberOfRides = 0;
		for (TrainRideStatistic statistic : trainRideStatistic){
			if (statistic.getScheduleScheme().getTrainType().equals(trainType)) {
				numberOfRides++;
			}
		}
		return numberOfRides;
	}
	
	/**
	 * This method will get the number of all train rides of the specified
	 * {@link ScheduleScheme} of the whole simulation.
	 * 
	 * @param scheduleScheme
	 * @return numberOfRides
	 */
	public int getNumberOfRides(ScheduleScheme scheduleScheme) {
		int numberOfRides = 0;
		for (TrainRideStatistic statistic : trainRideStatistic){
			if (statistic.getScheduleScheme().equals(scheduleScheme)) {
				numberOfRides++;
			}
		}
		return numberOfRides;
	}
	
	/**
	 * Returns a List of all ScheduleSchemes
	 *  
	 * @return trainTypes
	 * 			A List of all involved ScheduleSchemes
	 */
	public List <ScheduleScheme> getInvolvedScheduleSchemes () {
		java.util.List<ScheduleScheme> scheduleSchemes= new ArrayList <ScheduleScheme>();
		for (TrainRideStatistic statistic : trainRideStatistic) {
			if (!scheduleSchemes.contains(statistic.getScheduleScheme())){
				scheduleSchemes.add(statistic.getScheduleScheme());
			}
		}
		return scheduleSchemes;
		
	}	
	
	/**
	 * Returns a List of all TrainTypes
	 *  
	 * @return trainTypes
	 * 			A List of all involved TrainTypes
	 */
	public List <TrainType> getInvolvedTrainTypes() {
		java.util.List<TrainType> trainTypes= new ArrayList <TrainType>();
		for (ScheduleScheme scheduleScheme : getInvolvedScheduleSchemes()) {
			if (!trainTypes.contains(scheduleScheme.getTrainType())) {
				trainTypes.add(scheduleScheme.getTrainType());
			}
		}
		return trainTypes;
	}
	
	/**
	 * Returns a List of all Stations
	 *  
	 * @return nodes
	 * 			A List of all involved Stations
	 */
	public List <Node> getInvolvedNodes() {
		java.util.List<Node> nodes= new ArrayList <Node>();
		for (ScheduleScheme scheduleScheme : getInvolvedScheduleSchemes()) {
			java.util.List<Node> addNode = scheduleScheme.getStations();
			for (Node node : addNode) {
				if (!nodes.contains(node)) {
				nodes.add(node);
				}
			}
		}
		return nodes;
	}
	
	/**
	 * Returns the {@link RailwaySystem} affiliated with this
	 * {@link SimulationStatistic}.
	 * 
	 * @return The {@link RailwaySystem}
	 */
	public RailwaySystem getRailwaySystem() {
		return railSys;
	}
	
}
