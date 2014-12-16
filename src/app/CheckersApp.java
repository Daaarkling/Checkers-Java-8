package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Jan Vaňura
 */
public class CheckersApp extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/Checkers.fxml"));
		final String uriCss = getClass().getResource("/gui/style.css").toExternalForm();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(uriCss);
		stage.setScene(scene);
		stage.setTitle("Checkers");
		stage.setResizable(false);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/gui/icon.png")));
		stage.show();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
}
