package de.uniluebeck.gruppe4.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uniluebeck.gruppe4.model.TemperatureData;

public class JSONParserTest {

	String ACTUAL_WEATHER = "{\"coord\":{\"lon\":-0.13,\"lat\":51.51},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"base\":\"stations\",\"main\":{\"temp\":292.76,\"pressure\":1023,\"humidity\":56,\"temp_min\":291.15,\"temp_max\":294.15},\"visibility\":10000,\"wind\":{\"speed\":2.1},\"clouds\":{\"all\":75},\"dt\":1496233200,\"sys\":{\"type\":1,\"id\":5091,\"message\":0.0705,\"country\":\"GB\",\"sunrise\":1496202558,\"sunset\":1496261279},\"id\":2643743,\"name\":\"London\",\"cod\":200}";
	
	
	@Test
	public void getTemperaturFromJSON_test() {
		TemperatureData result = JSONParser.getTemperatureDataFromJSON(ACTUAL_WEATHER);
		
		assertEquals(292.76, result.getAverage(), 0);
		assertEquals(291.15, result.getMin(), 0);
		assertEquals(294.15, result.getMax(), 0);
	}

}
