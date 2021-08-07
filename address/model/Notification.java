package address.model;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Notification {
	public static boolean showNotification(Stage stage) {
		Alert notification = new Alert(Alert.AlertType.CONFIRMATION);
		notification.setTitle("提示窗口");
		notification.setHeaderText("是否确认删除所选图片？"); // 设置对话框的头部文本
		notification.initOwner(stage);
		Optional<ButtonType> buttonType = notification.showAndWait(); // 在对话框消失以前不会执行之后的代码

		if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) { // 单击了确定按钮OK_DONE
			return true;
		} else {
			return false;
		}
	}
}

