package address;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import address.DataService;

public class MainRountine extends Application {

	public static Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		DataService.stage = primaryStage;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/Main.fxml"));
			Parent root = (Parent)loader.load();
			Scene scene = new Scene(root);
			DataService.stage.setScene(scene);
			DataService.stage.setTitle("电子图片管理程序"); // 标题
			DataService.stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
