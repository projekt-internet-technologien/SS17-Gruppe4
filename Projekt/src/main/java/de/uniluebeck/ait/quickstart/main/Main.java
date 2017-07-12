package de.uniluebeck.ait.quickstart.main;

import org.apache.log4j.Logger;

import de.uniluebeck.ait.quickstart.coap.SSPExampleApplication;
import de.uniluebeck.ait.quickstart.restclient.RestClient;
import de.uzl.itm.ncoap.communication.blockwise.BlockSize;

public class Main {
	private static Logger LOG = Logger.getLogger(Main.class.getName());
    
	public static void main(String[] args) {

		SSPExampleApplication server = new SSPExampleApplication(BlockSize.SIZE_64, BlockSize.SIZE_64);
		server.run();

		try {
			String ip = "141.83.151.196";
			String port = "8080";
			
			RestClient rc = new RestClient(ip, port, "text/csv");
			
			rc.runLuxAverageQuery();
		} catch (Exception e) {
			LOG.error(e);
		}
	}

}
