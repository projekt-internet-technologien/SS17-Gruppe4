
package de.uniluebeck.ait.quickstart.coap;

import java.util.concurrent.ScheduledExecutorService;

import org.apache.log4j.Logger;

import de.uzl.itm.ncoap.message.CoapMessage;
import de.uzl.itm.ncoap.message.options.ContentFormat;

public class SimpleObservableTimeService extends SimpleObservableService {

    private static Logger LOG = Logger.getLogger(SimpleObservableTimeService.class.getName());

    static{
        payloadTemplates.put(
                ContentFormat.TEXT_PLAIN_UTF8,
                "The current time is %02d:%02d:%02d"
        );
        
        payloadTemplates.put(
	        ContentFormat.APP_TURTLE,
	        "@prefix itm: <http://gruppe04.pit.itm.uni-luebeck.de/>\n" +
	        "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
	        "\n" + 
	        "itm:time1 itm:hour \"%02d\"^^xsd:integer .\n" + 
	       	"itm:time1 itm:minute \"%02d\"^^xsd:integer .\n" + 
	       	"itm:time1 itm:seconds \"%02d\"^^xsd:integer ."
        );        
    }

    /**
     * Creates a new instance of {@link SimpleObservableTimeService}.
     *
     * @param path the path of this {@link SimpleObservableTimeService} (e.g. /utc-time)
     * @param updateInterval the interval (in seconds) for resource status updates (e.g. 5 for every 5 seconds).
     */
    public SimpleObservableTimeService(String path, int updateInterval, ScheduledExecutorService executor) {
        super(path, updateInterval, executor);
    }


    @Override
    public byte[] getSerializedResourceStatus(long contentFormat) {
        LOG.debug("Try to create payload (content format: " + contentFormat + ")");

        String template = payloadTemplates.get(contentFormat);
        if (template == null) {
            return null;
        } else {
            long time = getResourceStatus() % 86400000;
            long hours = time / 3600000;
            long remainder = time % 3600000;
            long minutes = remainder / 60000;
            long seconds = (remainder % 60000) / 1000;
            return String.format(template, hours, minutes, seconds).getBytes(CoapMessage.CHARSET);
        }
    }
}
