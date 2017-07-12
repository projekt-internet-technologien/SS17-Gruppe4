package de.uniluebeck.gruppe4.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.uniluebeck.gruppe4.model.TemperatureData;

public class JSONParser {

	public static TemperatureData getTemperatureDataFromJSON(String json){
		double temp = getFromMain(json, "temp");
		double min = getFromMain(json, "temp_min");
		double max = getFromMain(json, "temp_max");
		
		TemperatureData data = new TemperatureData(temp, min, max);
		
		return data;
	}
	
	private static double getFromMain(String json, String field){
		JSONObject obj;
		String temp = null;
		try {
			obj = new JSONObject(json);
			temp = obj.getJSONObject("main").getString(field);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BigDecimal res = new BigDecimal(temp);
		
		return res.doubleValue();
	}
	
	
	/**
	 * Liefert die Temperaturen fuer die naechsten 3 Tage
	 * @param json
	 * @return
	 */
	public static Map<Date, TemperatureData> getForecastsTemperaturFromJSON(String json){
		 Date tomorrow = DateUtils.addDays(new Date(), 1);
		 Date day_after_tomorrow = DateUtils.addDays(new Date(), 2);
		 Date day_after_day_after_tomorrow = DateUtils.addDays(new Date(), 3);
		 
		 TemperatureData data1 = getTemperaturFromJSONForDay(json, tomorrow);
		 TemperatureData data2 = getTemperaturFromJSONForDay(json, day_after_tomorrow);
		 TemperatureData data3 = getTemperaturFromJSONForDay(json, day_after_day_after_tomorrow);
		 
		 Map<Date, TemperatureData> treeMap = new TreeMap<Date, TemperatureData>();
		 treeMap.put(tomorrow, data1);
		 treeMap.put(day_after_tomorrow, data2);
		 treeMap.put(day_after_day_after_tomorrow, data3);
		 
		 return treeMap;
	}
	
	/**
	 * Liefert Temperaturen fuer das angegeben Datum
	 * 
	 * @param json
	 * @return
	 */
	public static TemperatureData getTemperaturFromJSONForDay(String json, Date day){
		JSONObject obj;
		TemperatureData temperatureData = new TemperatureData();
		double tempForTomorrow = 0;
		double sumTemp = 0;
		int count = 1;
		try {
			obj = new JSONObject(json);
			JSONArray list = obj.getJSONArray("list");
			if(list != null){
				for (int i = 0; i < list.length(); i++){
				    String date_text = list.getJSONObject(i).getString("dt_txt");
				    
				    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				    Date date = null;
					try {
						date = simpleDateFormat.parse(date_text);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    
				    if(DateUtils.isSameDay(date, day)){
				    	//Wir wollen die Maximal-Temperatur ausgeben
				    	//Die erste Temperatur des neuen Tages (waere ja dann quasi 0:00 Uhr macht ja auch keinen Sinn)
				    	JSONObject tomorrowWeather = list.getJSONObject(i);
				    	
				    	TemperatureData temp = getTemperatureDataFromJSON(tomorrowWeather.toString());
				    	sumTemp = sumTemp + temp.getAverage();
				    	
				    	if(temperatureData.getMax() == 0){
				    		temperatureData.setMax(temp.getMax());
				    		temperatureData.setMin(temp.getMin());
				    	}
				    	if(temp.getMax() > temperatureData.getMax()){
				    		temperatureData.setMax(temp.getMax());
				    	}
				    	if(temp.getMin() < temperatureData.getMin()){
				    		temperatureData.setMin(temp.getMin());
				    	}
				    	
//				    	double temp = getMaxTemperaturFromJSON(tomorrowWeather.toString());
//				    	if(maxTempForTomorrow == 0){
//				    		maxTempForTomorrow = temp;
//				    	}
//				    	if(temp > maxTempForTomorrow){
//				    		maxTempForTomorrow = temp;
//				    	}
//				    	
//				    	temp = getMinTemperaturFromJSON(tomorrowWeather.toString());
//				    	if(minTempForTomorrow == 0){
//				    		minTempForTomorrow = temp;
//				    	}
//				    	if(temp < minTempForTomorrow){
//				    		minTempForTomorrow = temp;
//				    	}
				    	
				    	count++;
				    }
				}
				
				tempForTomorrow = sumTemp / (double) count;
				temperatureData.setAverage(tempForTomorrow);
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return temperatureData;
	}
}
