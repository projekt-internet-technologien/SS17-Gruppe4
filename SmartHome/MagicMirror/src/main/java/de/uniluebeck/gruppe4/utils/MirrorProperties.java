package de.uniluebeck.gruppe4.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MirrorProperties {

	public static String get(String configName, String propertiesKey) {

		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = MirrorProperties.class.getClassLoader().getResourceAsStream(configName + ".properties");
			if (input == null) {
				System.out.println(
						"Die Konfigurations-Datei: " + configName + ".properties konnte nicht gefunden werden!");
				return "";
			}

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			return prop.getProperty(propertiesKey);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}
}
