package de.uniluebeck.gruppe4.ui;

import javafx.application.Platform;

public class FXMLBspTask implements Runnable {

	private FXMLBspController controller = null;

	public FXMLBspTask(FXMLBspController controller) {
		this.controller = controller;
	}

	@Override
	public void run() {

		while (true) {
			if (controller.refresh_done) {
				Runnable run = new Runnable() {
					@Override
					public void run() {
						controller.initialisiereAnzeigeDaten();
					}
				};
				Platform.runLater(run);
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
