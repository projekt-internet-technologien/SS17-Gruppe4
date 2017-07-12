package de.uniluebeck.gruppe4.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import de.uniluebeck.gruppe4.model.TemperatureData;

public class MirrorFormatter {

	/**
	 * Formats a double to an String with no decimal Places with default rounding.
	 * e.g.: 	21.4 -> '21'
	 * 			21.6 -> '22'
	 * @param d
	 * @return String
	 */
	public static String fomratDoubleToZeroDecimalPlaces(double d){
		return new DecimalFormat("#", DecimalFormatSymbols.getInstance( Locale.GERMAN )).format(d);
	}
	
	/**
	 * Formats the TemperatureData to an output String of pattern Max C° / Min C°
	 * @param temp
	 */
	public static String formatTemperatureDataToMaxMin(TemperatureData temp){
		StringBuilder sb = new StringBuilder();
		sb.append(fomratDoubleToZeroDecimalPlaces(temp.getMax()));
		sb.append("°");
		sb.append("/");
		sb.append(fomratDoubleToZeroDecimalPlaces(temp.getMin()));
		sb.append("°");
		
		return sb.toString();
	}
}
