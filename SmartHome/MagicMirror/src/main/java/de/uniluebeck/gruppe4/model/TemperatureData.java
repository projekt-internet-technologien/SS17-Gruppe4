package de.uniluebeck.gruppe4.model;

public class TemperatureData {
	private double average;
	private double min;
	private double max;
	
	public TemperatureData(){
		
	}
	
	public TemperatureData(double average, double min, double max){
		this.average = average;
		this.min = min;
		this.max = max;
	}

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	@Override
	public String toString() {
		return "TemperatureData [average=" + average + ", min=" + min + ", max=" + max + "]";
	}
	
}
