
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

		while (true) {
			SSPService sspService = new SSPService();
			String face = sspService.runFaceRecQuery();
			
			if ((StringUtils.isBlank(face)) && isopen) {
				primaryStage.close();
				isopen = false;
			} else if (!isopen && (StringUtils.isNotBlank(face))) {
				root = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLBsp.FXML"));
				primaryStage.setFullScreen(true);
				primaryStage.setTitle("FXML-Beispiel");
				primaryStage.setScene(new Scene(root, 1000, 500));
				primaryStage.show();
				isopen = true;
			}
			Thread.sleep(2000);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
