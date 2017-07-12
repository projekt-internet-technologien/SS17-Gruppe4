package de.uniluebeck.gruppe4.dataservices.menu;

import org.apache.log4j.Logger;

import java.io.IOException;
import org.jsoup.*;
import org.jsoup.nodes.*;

public class MensaService {

	private static final String BASE_URL = "http://www.studentenwerk.sh/de/essen/standorte/luebeck/mensa-luebeck/speiseplan.html";
	static Logger log = Logger.getLogger(MensaService.class.getName());
	
	
	public String getMenuData(){
		StringBuilder stringBuilder = new StringBuilder();
	
		try {
			Document doc = Jsoup.connect(BASE_URL).get();
			// select current menu
			Element daily_menu = doc.select("div.day.today").first();
			// parse it into strings
			try {
				for (Element food : daily_menu.select("strong")) {
					Document essen = Jsoup.parse(food.toString());
					stringBuilder.append(essen.text());
					stringBuilder.append("\n");
				}
			} catch (NullPointerException e) {
				log.error("failed to find specified html code", e);
			}
			

			return stringBuilder.toString();

		} catch (IOException e) {
			log.error("failed to resolve URL", e);
		} 
		return null;
	}	
}
