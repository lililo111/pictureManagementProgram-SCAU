package address.action;

import address.ImageRead;
import address.MainRountine;
import address.model.Notification;
import address.view.*;

// 删除事件类
public class DeleteAction {
	MainController main;

	// 有参构造方法
	public DeleteAction(MainController main) {
		this.main = main;
		// 若没有选中图片则返回
		if (ImageRead.getSelectedPictures().size() <= 0) {
			return;
		}
		// 显示提示栏
		if (Notification.showNotification(MainRountine.primaryStage)) {
			for (ImageRead eachImageNode : ImageRead.getSelectedPictures()) {
				main.getFlowpane().getChildren().remove(eachImageNode); // 删除图片结点
				eachImageNode.getImageFile().delete(); // 删除图片文件
				main.removeImages(eachImageNode);
			}
			ImageRead.getSelectedPictureFiles().clear();
		} else {
			ImageRead.getSelectedPictureFiles().clear();
		}
		main.refresh();
		ImageRead.clearSelected();
	}
}
