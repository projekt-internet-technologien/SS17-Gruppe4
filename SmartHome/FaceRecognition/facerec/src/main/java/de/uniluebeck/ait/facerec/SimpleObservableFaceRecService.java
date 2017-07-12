package de.uniluebeck.ait.facerec;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;

import de.uzl.itm.ncoap.message.CoapMessage;
import de.uzl.itm.ncoap.message.options.ContentFormat;

public class SimpleObservableFaceRecService extends SimpleObservableService {
	
	FaceRec faceRec;
	Instant lastSeen = Instant.now();
	
	static{
        payloadTemplates.put(
                ContentFormat.TEXT_PLAIN_UTF8,
                "%s"
        );

        payloadTemplates.put(
	        ContentFormat.APP_TURTLE,
	        "@prefix itm: <http://gruppe04.pit.itm.uni-luebeck.de/>\n" +
	        "@prefix pit: <https://pit.itm.uni-luebeck.de/>\n" +
	        "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
			"\n" + 
	        "itm:Cam pit:hasDescription \"Face Recognition Sensor\"^^xsd:string ." +
	        "\n" + 
			"itm:Cam pit:hasURL \"http://gruppe04.pit.itm.uni-luebeck.de/facerec\"^^xsd:anyURI ." +
			"\n" + 
			"itm:Cam pit:isActor \"false\"^^xsd:boolean ." +
			"\n" + 
			"itm:Cam pit:isType \"Kamera\"^^xsd:string ." +
			"\n" + 
	        "itm:Cam pit:hasStatus itm:CamStatusName ." +
			"\n" + 
			"itm:CamStatusName pit:hasValue \"%s\"^^xsd:string ." +
	        "\n" +
	        "itm:CamStatusName pit:lastModified \"%s\"^^xsd:dateTime ."
        );        
    }
	
	private String name = "";
	
	public SimpleObservableFaceRecService(String path, int updateInterval, ScheduledExecutorService executor) {
		super(path, updateInterval, executor);
		new Thread(new FaceRec(this)).start();
		System.out.println("FaceRec Service started");
	}
	
	public void setName(String name) {
		System.out.println("Set name FaceRecService to: "+name);
		Instant now = Instant.now();
		if (Duration.between(lastSeen, now).toMillis() > 10000 || name == this.name || name == "Unknown") {
			this.name = name;
			lastSeen = now;
		}
	}

	@Override
	public byte[] getSerializedResourceStatus(long contentFormat) {
        String template = payloadTemplates.get(contentFormat);
        Instant now = Instant.now();
		if (name != null && name != "" && (Duration.between(lastSeen, now).toMillis() > 20000)) {
			System.out.println("Name set to \"\". Last seen: "+lastSeen+"  Now: "+now);
			this.name = "";
		}
        
        if (template == null || name == null) {
            return null;
        } else {
        	System.out.println(name);
    		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
    		String s = sdf.format(new Date());
    		
    		return String.format(template, name, s).getBytes(CoapMessage.CHARSET);
        }
	}


}
