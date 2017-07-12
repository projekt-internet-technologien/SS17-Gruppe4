package de.uniluebeck.gruppe4.ssp;

public class Main {

	public static void main(String[] args) {
		SSPService service = new SSPService();
		
		String res = service.runFaceRecQuery();
		
		System.out.println("result: " + res);

	}

}
