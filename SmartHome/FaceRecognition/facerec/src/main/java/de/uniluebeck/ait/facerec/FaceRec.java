package de.uniluebeck.ait.facerec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;

public class FaceRec implements Runnable {

	SimpleObservableFaceRecService faceRecService;

	public FaceRec(SimpleObservableFaceRecService faceRecService) {
		this.faceRecService = faceRecService;
	}

	public void run() {
		try {
			System.out.println("Start FaceRec");
			ProcessBuilder pb = new ProcessBuilder("python","-u", "/home/pi/facerec.py");
			pb.redirectOutput(Redirect.PIPE);
			Process p = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			
			while (true) {
				try {
					p.exitValue();
					break;
				} catch (Exception e) {
				}
				while ((line = reader.readLine()) != null) {
					faceRecService.setName(line);
				}
			}
			System.out.println("Stop FaceRec");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
