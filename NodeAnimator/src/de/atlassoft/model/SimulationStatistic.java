package de.atlassoft.model;

import java.util.ArrayList;

/**
 * Represents the statistic of the current continuous simulation
 * 
 * @author Tobias Ilg
 *
 */
public class SimulationStatistic {
	
	/**
	 * This List contains all trainRideStatistics
	 * 
	 */
	java.util.List<TrainRideStatistic> trainRideStatistic;
	
	/**
	 * This is the constructor where the List will initialized
	 * 
	 */
	public SimulationStatistic() {
		trainRideStatistic = new ArrayList <TrainRideStatistic>();
	}
	
	/**
	 * This method will add the current TrainRideStatistics to the List
	 * 
	 * @param statistic
	 */
	public void addStatistic(TrainRideStatistic statistic) {
		trainRideStatistic.add(statistic);
	}
	
	/**
	 * This method will calculate the mean Delay of the whole Simulation
	 * 
	 * @return meanDelay
	 * 
	 */
	public double getMeanDelay() {
		double meanDelay;
		meanDelay = getTotalDelay() / getNumberOfRides();
		return meanDelay;
	}
	
	/**
	 * This method will calculate the total Delay of the whole Simulation
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
	 * This method will calculate the mean Delay
	 * of an specify TrainType of the whole Simulation
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
	 * This method will calculate the mean Delay
	 * of an specify Node of the whole Simulation
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
	 * This method will calculate the mean Delay of
	 * an specify ScheduleScheme of the whole Simulation
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
	 * This method will calculate the mean Delay of
	 * an specify ScheduleScheme and an specify Node of the whole Simulation
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
	 * This method will get the number of all TrainRides
	 * of the whole Simulation
	 * 
	 * @return numberOfRides
	 */
	public int getNumberOfRides() {
		int numberOfRides = trainRideStatistic.size();
		return numberOfRides;
	}
	
	/**
	 * This method will get the number of all TrainRides
	 * of an specify TrainType of the whole Simulation
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
	 * This method will get the number of all TrainRides
	 * of an specify ScheduleScheme of the whole Simulation
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
	
}
