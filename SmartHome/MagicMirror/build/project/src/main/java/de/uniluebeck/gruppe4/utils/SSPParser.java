package de.uniluebeck.gruppe4.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SSPParser {

	public static List<Double> getDoubleResults(HashMap<String, Object> map){
		List<Double> doubleList = new ArrayList<Double>();
		
		// op\r\nVALUE\r\n
		String results = (String) map.get("results");
		if (results != null) {

			Pattern p = Pattern.compile("\\d+[.\\d+]*");
			Matcher m = p.matcher(results);

			while (m.find()) {
				doubleList.add(Double.parseDouble(m.group()));
			}
		}
		
		return doubleList;
	}
	
}
