package de.uniluebeck.gruppe4.model;

import java.util.Date;
import java.util.Map;

public class WeatherData {

	private TemperatureData actualTemp;
	
	private Map<Date, TemperatureData> forecast;
	
	public WeatherData(TemperatureData actualTemp, Map<Date, TemperatureData> forecast){
		this.actualTemp = actualTemp;
		this.forecast = forecast;
	}

	public TemperatureData getActualTemp() {
		return actualTemp;
	}

	public void setActualTemp(TemperatureData actualTemp) {
		this.actualTemp = actualTemp;
	}

	public Map<Date, TemperatureData> getForecast() {
		return forecast;
	}

	public void setForecast(Map<Date, TemperatureData> forecast) {
		this.forecast = forecast;
	}
}
