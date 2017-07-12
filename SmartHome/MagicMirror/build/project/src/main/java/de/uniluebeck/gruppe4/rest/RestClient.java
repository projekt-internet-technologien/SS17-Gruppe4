

package de.uniluebeck.gruppe4.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class RestClient {

	public static String getRequest(String url) {
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet get = new HttpGet(url);
			get.addHeader("accept", "application/json");

			HttpResponse response = httpClient.execute(get);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				return line;
			}
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}

