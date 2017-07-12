

package de.uniluebeck.gruppe4.dataservices.weather;

import java.util.Date;
import java.util.Map;

import de.uniluebeck.gruppe4.model.TemperatureData;
import de.uniluebeck.gruppe4.model.WeatherData;
import de.uniluebeck.gruppe4.rest.RestClient;
import de.uniluebeck.gruppe4.utils.JSONParser;

public class WeatherService {

	static final String KEY = "&appid=da7c4d55907d8cff81b1e5a02bae88e6";
	static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
	static final String CELSIUS = "&units=metric";
	
	private String city;
	
	public WeatherService(String city){
		this.city = city;
	}
	
	public WeatherData getWeatherData(){
		String actualWeather = getActualWeather();
		String forecast = getForecast();
		
		TemperatureData actualTemp = JSONParser.getTemperatureDataFromJSON(actualWeather);
		Map<Date, TemperatureData> forecastTemp = JSONParser.getForecastsTemperaturFromJSON(forecast);
		
		WeatherData weatherData = new WeatherData(actualTemp, forecastTemp);
		
		return weatherData;
	}
	
	private String getActualWeather() {
		return RestClient.getRequest(BASE_URL + "weather?q=" + city + CELSIUS + KEY);
	}
	
	private String getForecast(){
		return RestClient.getRequest(BASE_URL + "forecast?q=" + city + CELSIUS + KEY);
	}
}

