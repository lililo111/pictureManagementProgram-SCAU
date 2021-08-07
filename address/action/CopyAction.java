package address.action;

import address.ImageRead;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

// 复制事件类（正确）
public class CopyAction {
	// 无参构造方法
	public CopyAction() {
		// 若无选中的图片则返回
		if (ImageRead.getSelectedPictures().size() <= 0) {
			return;
		}
		
		Clipboard clipboard = Clipboard.getSystemClipboard(); // 获取系统剪贴板
		ClipboardContent clipboardContent = new ClipboardContent();
		clipboardContent.clear(); // 清除剪贴板内容
		for (ImageRead eachImageNode : ImageRead.getSelectedPictures()) {
			ImageRead.getSelectedPictureFiles().add(eachImageNode.getImageFile());
		}
		clipboardContent.putFiles(ImageRead.getSelectedPictureFiles()); // 将复制的文件放入剪贴板中
		clipboard.setContent(clipboardContent);
		ImageRead.getSelectedPictureFiles().clear();

		// 清空剪贴板
//		clipboard = null;
//		clipboardContent = null;
	}
}
