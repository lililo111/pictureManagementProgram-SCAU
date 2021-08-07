package address;

import address.view.MainController;
import address.view.PPTviewController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

// 打开事件类
public class OpenAction {
	private int index;
	private MainController main;
	String[] photos;

	public OpenAction(MainController main) {
		this.main = main;
		// 如果图片存在
		if (main.getFlowpane().getChildren().size() != 0) {
			try {

				Stage stage = new Stage();
				photos = new String[main.getFlowpane().getChildren().size()];
				for (int i = 0; i < main.getFlowpane().getChildren().size(); i++) {
					String str = main.getImages().get(i).getURL();
					photos[i] = str;
				}
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("view/PPTview.fxml"));

				Parent root = (Parent) loader.load();
				Scene scene = new Scene(root);
				PPTviewController controller = (PPTviewController) loader.getController();

				for (ImageRead ig : main.getImages()){
					if (ig.selected.getValue() == true) {
						index = main.getImages().indexOf(ig);
//						System.out.println("index: "+index);
						break;
					}
				}
				controller.setIndex(index);
				controller.setPhotos(photos, main);
				
				scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
					public void handle(KeyEvent ke) {
						// ESC键关闭窗口
						if (ke.getCode() == KeyCode.ESCAPE) {
							// System.out.println("Key Pressed: " + ke.getCode());
							stage.close();
						}
					}
				});
				
				stage.setScene(scene);

//			stage.setFullScreen(true);
				stage.show();
				/*
				 * FXMLLoader loader = new FXMLLoader();
				 * loader.setLocation(getClass().getResource("/View/PPTView.fxml")); //
				 * 导入幻灯片FXML文件 Parent root = (Parent) loader.load(); Scene scene = new
				 * Scene(root); DataService.stage.setScene(scene);
				 * DataService.stage.setTitle("电子图片管理程序"); DataService.stage.show();
				 */
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.titleProperty().set("提示");
			alert.headerTextProperty().set("没有选中图片");
			alert.showAndWait();
		}
	}
}
