package de.uniluebeck.ait.quickstart.restclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.PatternFilenameFilter;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class RestClient {

	private final static double DUNKELHEITS_GRENZE = 200;
	
	private String accept;
	private String ip;
	private String port;
	private GpioPinDigitalOutput pin;
	
	public RestClient(String ip, String port, String accept) {
		this.ip = ip;
		this.port = port;
		this.accept = accept;
		
		// get a handle to the GPIO controller
		final GpioController gpio = GpioFactory.getInstance();

		// creating the pin with parameter PinState.HIGH will instantly power up the pin
		this.pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25);
	}
	
	
	public LinkedList<String> getResult(String sparqlQuery) throws Exception {
	
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost("http://" + this.ip + ":" + this.port + "/services/sparql-endpoint");
		
		post.addHeader("Accept", this.accept);
		post.addHeader("Content-Type", "multipart/form-data; boundary=DATA");

		String data = "--DATA\n" +
				"Content-Disposition: form-data; name=\"query\"\n\n" +
				sparqlQuery + "\n" +
				"--DATA--";
		post.setEntity(new StringEntity(data));
		
		HttpResponse response = client.execute(post);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		LinkedList<String> lines = new LinkedList<String>();
		String line = "";
		while ((line = rd.readLine()) != null) {
			lines.add(line.trim());
		}
		return lines;

	}	
	
	public void runLuxAverageQuery() throws Exception {
		
		String sparql = "PREFIX rs: <http://example.org/road-sections#> \n" +
				"PREFIX itm: <http://gruppe04.pit.itm.uni-luebeck.de/> \n" +
				"PREFIX  pit: <https://pit.itm.uni-luebeck.de/>\n" +
				"\n" + 
				"Select (AVG(xsd:float(?v)) as ?luxAverage)  \n" + 
				"Where{ \n" +
				"?o pit:isType \"LDR\"^^xsd:string . \n" +
				"?o pit:hasStatus ?p . \n" +
				"?p pit:hasValue ?v . \n" +
				"}";
		
		while(true) {
			LinkedList<String> ll = getResult(sparql);
			HashMap<String, Object> map = new ObjectMapper().readValue(ll.removeFirst(), HashMap.class);

			// op\r\nVALUE\r\n
			String results = (String) map.get("results");
			if (results != null) {

				Pattern p = Pattern.compile("\\d+[.\\d+]*");
				Matcher m = p.matcher(results);

				if (m.find()) {
					double d = Double.parseDouble(m.group());
					turnLedOnOff(d);
				}
			}
			Thread.sleep(1000);
		}
	}
	
	
	public void turnLedOnOff(double lux){

		if(DUNKELHEITS_GRENZE > lux){
			pin.high();
		}else{
			pin.low();
		}
		
		//Wohin?
		// release the GPIO controller resources
		//gpio.shutdown();
	}
	
}
