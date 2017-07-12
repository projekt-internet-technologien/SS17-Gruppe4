package de.uniluebeck.ait.quickstart.coap;

import java.util.concurrent.ScheduledExecutorService;

import org.apache.log4j.Logger;

import de.uzl.itm.ncoap.message.options.ContentFormat;

public class SimpleObservablePiService extends SimpleObservableService {

    private static Logger LOG = Logger.getLogger(SimpleObservablePiService.class.getName());

    static{
        payloadTemplates.put(
                ContentFormat.TEXT_PLAIN_UTF8,
                "The Ip is 141.83.175.238 \n" +
                "The group is Gruppe04 \n" +
                "The label is AIT Projekt \n" + 
                "Has Component ldr"
        );
        
        payloadTemplates.put(
	        ContentFormat.APP_TURTLE,
	        "@prefix itm: <http://gruppe04.pit.itm.uni-luebeck.de/>\n" +
	        "@prefix pit: <https://pit.itm.uni-luebeck.de/>\n" +
	        "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
	        "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n. " +
	        "\n" + 
	        "itm:device1 rdfs:Class itm:Device ." +	        
	        "\n" + 
	        "itm:device1 pit:hasIP \"141.83.175.238\"^^xsd:string ." +
	        "\n" + 
	        "itm:device1 pit:hasGroup \"Gruppe04\"^^xsd:string ." +
	        "\n" + 
	        "itm:device1 pit:hasLabel \"AIT Projekt\"^^xsd:string ." +
	        "\n" + 
	        "itm:device1 pit:hasComponent itm:ldr ." 	        
        
        );        
    }

    /**
     * Creates a new instance of {@link SimpleObservablePiService}.
     *
     * @param path the path of this {@link SimpleObservablePiService}
     * @param updateInterval the interval (in seconds) for resource status updates (e.g. 5 for every 5 seconds).
     */
    public SimpleObservablePiService(String path, int updateInterval, ScheduledExecutorService executor) {
        super(path, updateInterval, executor);
    }

    @Override
    public byte[] getSerializedResourceStatus(long contentFormat) {
        String template = payloadTemplates.get(contentFormat);

        return template.getBytes();
    }
}
