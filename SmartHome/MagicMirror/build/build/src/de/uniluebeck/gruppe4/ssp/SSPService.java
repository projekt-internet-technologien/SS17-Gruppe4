package de.uniluebeck.gruppe4.ssp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.uniluebeck.gruppe4.utils.SSPParser;

public class SSPService {

	private static final String IP = "141.83.151.196";
	private static final String PORT = "8080";
	private static final String ACCEPT = "text/csv";

	public String runFaceRecQuery() {
		String face = null;

		String sparql = "PREFIX rs: <http://example.org/road-sections#> \n"
				+ "PREFIX itm: <http://gruppe04.pit.itm.uni-luebeck.de/> \n"
				+ "PREFIX  pit: <https://pit.itm.uni-luebeck.de/>\n"
				+ "PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#> \n" + "\n" + "Select ?v  \n" + "Where{ \n"
				+ "?o pit:isType \"Kamera\"^^xsd:string . \n" + "?o pit:hasStatus ?p . \n" + "?p pit:hasValue ?v . \n"
				+ "}";

		LinkedList<String> ll;
		HashMap<String, Object> map = null;
		try {
			ll = getResult(sparql);
			map = new ObjectMapper().readValue(ll.removeFirst(), HashMap.class);

			String results = (String) map.get("results");
			if (results != null) {
				System.out.println("Map String: " + results);

				face = removeV(results);
				System.out.println("face: " + face);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return face;
	}

	public String removeV(String s) {
		String p = s.replace("v\r\n", "");
		String x = p.replace("\r\n", "");
		String y = x.replace("\"", "");
		System.out.println(y);

		return y;
	}

	public Double runTempQuery() {
		Double temp = null;

		String sparql = "PREFIX rs: <http://example.org/road-sections#> \n"
				+ "PREFIX itm: <http://gruppe01.pit.itm.uni-luebeck.de/> \n"
				+ "PREFIX  pit: <https://pit.itm.uni-luebeck.de/>\n"
				+ "PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#> \n" + "\n" + "Select ?v  \n" + "Where{ \n"
				+ "?o pit:isType \"TEMP\"^^xsd:string . \n" + "?o pit:hasStatus ?p . \n"
				+ "?p pit:hasScaleUnit \"Celcius\"^^xsd:string . \n" + "?p pit:hasValue ?v . \n" + "}";

		List<Double> results = executeQuery(sparql);
		if (CollectionUtils.isNotEmpty(results) && results.size() > 1) {
			temp = results.get(1);
		}

		return temp;
	}

	public Double runHumidityQuery() {
		Double humidity = null;

		String sparql = "PREFIX rs: <http://example.org/road-sections#> \n"
				+ "PREFIX itm: <http://gruppe01.pit.itm.uni-luebeck.de/> \n"
				+ "PREFIX  pit: <https://pit.itm.uni-luebeck.de/>\n"
				+ "PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#> \n" + "\n" + "Select ?v  \n" + "Where{ \n"
				+ "?o pit:isType \"HUMID\"^^xsd:string . \n" + "?o pit:hasStatus ?p . \n" + "?p pit:hasValue ?v . \n"
				+ "}";

		List<Double> results = executeQuery(sparql);
		if (CollectionUtils.isNotEmpty(results)) {
			humidity = results.get(0);
		}

		return humidity;
	}

	private List<Double> executeQuery(String sparql) {
		LinkedList<String> ll;
		HashMap<String, Object> map = null;
		try {
			ll = getResult(sparql);
			map = new ObjectMapper().readValue(ll.removeFirst(), HashMap.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return SSPParser.getDoubleResults(map);
	}

	public LinkedList<String> getResult(String sparqlQuery) throws Exception {

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost("http://" + IP + ":" + PORT + "/services/sparql-endpoint");

		post.addHeader("Accept", ACCEPT);
		post.addHeader("Content-Type", "multipart/form-data; boundary=DATA");

		String data = "--DATA\n" + "Content-Disposition: form-data; name=\"query\"\n\n" + sparqlQuery + "\n"
				+ "--DATA--";
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
}
