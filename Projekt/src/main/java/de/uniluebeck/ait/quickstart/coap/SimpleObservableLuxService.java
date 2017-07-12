/**
 * Copyright (c) 2016, Oliver Kleine, Institute of Telematics, University of Luebeck
 * All rights reserved
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *  - Redistributions of source messageCode must retain the above copyright notice, this list of conditions and the following
 *    disclaimer.
 *
 *  - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *  - Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 *    products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.uniluebeck.ait.quickstart.coap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.log4j.Logger;

import de.uniluebeck.ait.quickstart.SerialListener;
import de.uzl.itm.ncoap.message.CoapMessage;
import de.uzl.itm.ncoap.message.options.ContentFormat;


public class SimpleObservableLuxService extends SimpleObservableService {

    private static Logger LOG = Logger.getLogger(SimpleObservableLuxService.class.getName());
    
    private SerialListener serial;
    
    static{
        payloadTemplates.put(
                ContentFormat.TEXT_PLAIN_UTF8,
                "The current ldr is %a"
        );

        payloadTemplates.put(
	        ContentFormat.APP_TURTLE,
	        "@prefix itm: <http://gruppe04.pit.itm.uni-luebeck.de/>\n" +
	        "@prefix pit: <https://pit.itm.uni-luebeck.de/>\n" +
	        "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
			"\n" + 
	        "itm:ldr pit:hasDescription \"Sensor for Lux value\"^^xsd:string ." +
	        "\n" + 
			"itm:ldr pit:hasURL \"http://gruppe04.pit.itm.uni-luebeck.de/ldr\"^^xsd:anyURI ." +
			"\n" + 
			"itm:ldr pit:isActor \"false\"^^xsd:boolean ." +
			"\n" + 
			"itm:ldr pit:isType \"LDR\"^^xsd:string ." +
			"\n" + 
	        "itm:ldr pit:hasStatus itm:ldrStatus ." +
			"\n" + 
	        "itm:ldr pit:lastModified \"%s\"^^xsd:dateTime ." +			
	        "\n" + 
	        "itm:ldrStatus pit:hasScaleUnit \"Lux\"^^xsd:string ." +
	        "\n" +
	        "itm:ldrStatus pit:hasValue \"%s\"^^xsd:string ."
        );        
    }


    /**
     * Creates a new instance of {@link SimpleObservableLuxService}.
     *
     * @param path the path of this {@link SimpleObservableLuxService} (e.g. /utc-time)
     * @param updateInterval the interval (in seconds) for resource status updates (e.g. 5 for every 5 seconds).
     */
    public SimpleObservableLuxService(String path, int updateInterval, ScheduledExecutorService executor) {
        super(path, updateInterval, executor);
        
        startObservation();
    }


    @Override
    public byte[] getSerializedResourceStatus(long contentFormat) {
        String template = payloadTemplates.get(contentFormat);
        
        if (template == null) {
            return null;
        } else {
        	String lux = serial.getInputLine();
    		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
    		String s = sdf.format(new Date());
    		
    		return String.format(template, s, lux).getBytes(CoapMessage.CHARSET);
        }
    }
    
    public void startObservation(){
    	serial = new SerialListener();
    	serial.run();
    	serial.initialize();
    }
}
