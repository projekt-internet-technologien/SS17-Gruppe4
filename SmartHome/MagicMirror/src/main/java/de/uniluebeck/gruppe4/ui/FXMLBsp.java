
package de.uniluebeck.gruppe4.ui;

import org.apache.commons.lang3.StringUtils;

import de.uniluebeck.gruppe4.ssp.SSPService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMLBsp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = null;

		boolean isopen = false;
		System.out.println("starte ---  isopen: " + isopen);
		
		root = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLBsp.FXML"));
		primaryStage.setFullScreen(true);
		primaryStage.setTitle("FXML-Beispiel");
		primaryStage.setScene(new Scene(root, 1000, 500));
		//primaryStage.sizeToScene();	
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
