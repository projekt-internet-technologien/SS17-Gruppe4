
package de.uniluebeck.gruppe4.ui;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

import de.uniluebeck.gruppe4.dataservices.calendar.CalendarService;
import de.uniluebeck.gruppe4.dataservices.menu.MensaService;
import de.uniluebeck.gruppe4.dataservices.rssfeed.RssFeedService;
import de.uniluebeck.gruppe4.dataservices.weather.WeatherService;
import de.uniluebeck.gruppe4.model.RssFeedMessage;
import de.uniluebeck.gruppe4.model.TemperatureData;
import de.uniluebeck.gruppe4.model.WeatherData;
import de.uniluebeck.gruppe4.ssp.SSPService;
import de.uniluebeck.gruppe4.utils.MirrorFormatter;
import de.uniluebeck.gruppe4.utils.MirrorProperties;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;

public class FXMLBspController implements Initializable {

	WeatherData weatherData;

	List<Event> calendarData;

	RssFeedMessage feedMessage;

	String mensaMenu;

	@FXML
	private GridPane grid;

	@FXML
	private Label label_name;
	
	@FXML
	private Label label_date;

	@FXML
	private Label label_time;

	@FXML
	private Label label_calendar;

	@FXML
	private Label label_mensa;

	@FXML
	private Label label_newsfeed_title;

	@FXML
	private Label label_newsfeed_description;

	@FXML
	private Label label_weather_actual_temp;

	@FXML
	private Label label_weather_actual_hum;

	@FXML
	private Label label_weather_forecast_1;

	@FXML
	private Label label_weather_forecast_2;

	@FXML
	private Label label_weather_forecast_3;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		initialisiereAnzeigeDaten();

		setDateAndTimeLabel();
		setCalendarLabel();
		setMensaLabel();
		setRssFeedLabel();
		setTemperatureControls();

		setzeSpaltenBreiteFuerMittlereSpalte();
	}

	/**
	 * Berechnet und setzt die Breite fuer die mittlere Spalte. Die rechte und
	 * die linke Spalte haben jeweils eine fixe breite. Die mittlere Spalte soll
	 * den restlichen Platz auffuellen.
	 */
	private void setzeSpaltenBreiteFuerMittlereSpalte() {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double screenWidth = primaryScreenBounds.getWidth();

		// ObservableList<ColumnConstraints> constraints =
		// grid.getColumnConstraints();
		// ColumnConstraints column1 = constraints.get(0);
		// ColumnConstraints column2 = constraints.get(1);
		// ColumnConstraints column3 = constraints.get(2);
		//
		// double width2 = screenWidth - column1.getPrefWidth() -
		// column3.getPrefWidth();
		// column2.setPrefWidth(width2);
	}

	String face;

	public void initialisiereAnzeigeDaten() {

		SSPService sspService = new SSPService();
		face = sspService.runFaceRecQuery();

		// if face is null, use default properties
		if (StringUtils.isBlank(face)) {
			face = "default";
		}
		
		label_name.setText(face);

		String city = MirrorProperties.get(face, "weather.city");
		String feedURL = MirrorProperties.get(face, "feed.url");
		String calUser = MirrorProperties.get(face, "calendar.user");

		CalendarService calendarService = new CalendarService(calUser);
		try {
			calendarData = calendarService.getEventsFromNextTenDays();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MensaService mensaService = new MensaService();
		mensaMenu = mensaService.getMenuData();

		WeatherService weatherService = new WeatherService(city);
		this.weatherData = weatherService.getWeatherData();

		RssFeedService rssFeedService = new RssFeedService(feedURL);
		feedMessage = rssFeedService.getFeedData();
	}

	public void setDateAndTimeLabel() {
		DateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy");
		String dateString = formatter.format(new Date());
		label_date.setText(dateString);

		DateFormat timeFormatter = new SimpleDateFormat("hh:mm");
		String timeString = timeFormatter.format(new Date());
		label_time.setText(timeString);
	}

	public void setCalendarLabel() {
		StringBuilder calendarString = new StringBuilder();
		if (CollectionUtils.isNotEmpty(calendarData)) {
			for (Event event : calendarData) {
				DateFormat formatter = new SimpleDateFormat("dd.MM hh:mm");
				DateTime start = event.getStart().getDateTime();

				if (event.getStart().getDateTime() == null) {
					start = event.getStart().getDate();
					formatter = new SimpleDateFormat("dd.MM");
				}
				Date date = new Date(start.getValue());

				calendarString.append(
						formatter.format(date) + " " + event.getSummary() + " " + System.getProperty("line.separator"));
			}
		} else {
			calendarString.append("Keine anstehenden Termine");
		}
		label_calendar.setText(calendarString.toString());
	}

	public void setMensaLabel() {
		label_mensa.setText(mensaMenu);
	}

	public void setRssFeedLabel() {
		label_newsfeed_title.setText(feedMessage.getTitle());
		label_newsfeed_description.setText(feedMessage.getDescription());
		label_newsfeed_description.setMaxHeight(60);
		label_newsfeed_description.setWrapText(true);
	}

	public void setTemperatureControls() {

		SSPService sspService = new SSPService();

		Double temp = sspService.runTempQuery();
		Double humidity = sspService.runHumidityQuery();

		// Only if the SSP has the actual temperature then use it
		if (temp != null) {
			label_weather_actual_temp.setText(MirrorFormatter.fomratDoubleToZeroDecimalPlaces(temp) + " °C");
			label_weather_actual_hum.setText(MirrorFormatter.fomratDoubleToZeroDecimalPlaces(humidity) + " %");
		} else {
			// otherwise use actual temperature from weatherservice
			TemperatureData actual = weatherData.getActualTemp();
			label_weather_actual_temp.setText(MirrorFormatter.formatTemperatureDataToMaxMin(actual));
		}

		Map<Date, TemperatureData> forecast = weatherData.getForecast();
		Iterator<Entry<Date, TemperatureData>> it = forecast.entrySet().iterator();
		TemperatureData tomorrow = it.next().getValue();
		TemperatureData day_after_tommorow = it.next().getValue();
		TemperatureData day_after_day_after_tommorow = it.next().getValue();

		label_weather_forecast_1.setText(MirrorFormatter.formatTemperatureDataToMaxMin(tomorrow));
		label_weather_forecast_2.setText(MirrorFormatter.formatTemperatureDataToMaxMin(day_after_tommorow));
		label_weather_forecast_3.setText(MirrorFormatter.formatTemperatureDataToMaxMin(day_after_day_after_tommorow));
	}
}
