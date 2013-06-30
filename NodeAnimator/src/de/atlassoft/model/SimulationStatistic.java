package de.atlassoft.model;
public class SimulationStatistic {

	java.util.List<TrainRideStatistic> trainRideStatistic;
	
	public SimulationStatistic() {
		
	}
	
	public void addStatistic(TrainRideStatistic statistic) {
		trainRideStatistic.add(statistic);
	}
	
	public double getMeanDelay() {
		double meanDelay;
		meanDelay = getTotalDelay() / getNumberOfRides();
		return meanDelay;
	}
	
	public double getTotalDelay() {
		double totalDelay = 0;
		for (TrainRideStatistic statistic : trainRideStatistic) {
			java.util.List<Node> stationList = statistic.getStations();
			for (Node station : stationList) {
				totalDelay = totalDelay + statistic.getDelay(station);
			}
 		}
		return totalDelay;
	}
	
	public double getMeanDelay(TrainType trainType) {
		double meanDelay = 0;
		for (TrainRideStatistic statistic : trainRideStatistic) {
			if (statistic.getScheduleScheme().getTrainType() == trainType) {
				java.util.List<Node> stationList = statistic.getStations();
				for (Node station : stationList) {
					meanDelay = meanDelay + statistic.getDelay(station);
				}
			}
		}
		meanDelay = meanDelay / getNumberOfRides(trainType);
		return meanDelay;
	}
	
	public double getMeanDelay(Node station) {
		double meanDelay = 0.0;
		int numberOfRides = 0;
		for (TrainRideStatistic statistic : trainRideStatistic) {
			java.util.List<Node> stationList = statistic.getStations();
			for (Node allStation : stationList) {
				if (allStation == station) {
					meanDelay = meanDelay + statistic.getDelay(station);
					numberOfRides++;
				}
			}
 		}
		meanDelay = meanDelay / numberOfRides;
		return meanDelay;
	}
	
	public double getMeanDelay(ScheduleScheme scheduleScheme) {
		double meanDelay = 0.0;
		for (TrainRideStatistic statistic : trainRideStatistic) {
			if (statistic.getScheduleScheme() == scheduleScheme) {
				java.util.List<Node> stationList = statistic.getStations();
				for (Node station : stationList) {
					meanDelay = meanDelay + statistic.getDelay(station);
				}
			}
 		}
		meanDelay = meanDelay / getNumberOfRides(scheduleScheme);
		return meanDelay;
	}
	
	public double getMeanDelay(ScheduleScheme scheduleScheme, Node station) {
		double meanDelay = 0.0;
		for (TrainRideStatistic statistic : trainRideStatistic) {
			if (statistic.getScheduleScheme() == scheduleScheme) {
				java.util.List<Node> stationList = statistic.getStations();
				for (Node allStation : stationList) {
					if (allStation == station) {
						meanDelay = meanDelay + statistic.getDelay(station);
					}
				}
			}
 		}
		meanDelay = meanDelay / getNumberOfRides(scheduleScheme);
		return meanDelay;
	}
	
	public int getNumberOfRides() {
		int numberOfRides;
		numberOfRides = trainRideStatistic.size();
		return numberOfRides;
	}
	
	public int getNumberOfRides(TrainType trainType) {
		int numberOfRides = 0;
		for (TrainRideStatistic statistic : trainRideStatistic){
			if (statistic.getScheduleScheme().getTrainType() == trainType) {
				numberOfRides++;
			}
		}
		return numberOfRides;
	}
	
	public int getNumberOfRides(ScheduleScheme scheduleScheme) {
		int numberOfRides = 0;
		for (TrainRideStatistic statistic : trainRideStatistic){
			if (statistic.getScheduleScheme() == scheduleScheme) {
				numberOfRides++;
			}
		}
		return numberOfRides;
	}
	
}
