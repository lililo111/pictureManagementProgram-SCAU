package address.model;

import java.io.File;
import java.util.List;

import address.FlowpaneObservable;
import address.ImageRead;
import address.OpenAction;
import address.action.CopyAction;
import address.action.DeleteAction;
import address.action.PasteAction;
import address.action.RenameAction;
import address.view.MainController;
import javafx.collections.ObservableSet;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

// 右击出现菜单栏
public class RightClickMenu {
	ContextMenu rightClickMenu; // 创建右击弹出菜单
	MainController main;

	// 有参构造方法
	public RightClickMenu(MainController mainUI, Node node, boolean choosen) {
		this.main = mainUI;
		// 判断是否选中图片
		if (choosen) {
			ImageMenu(node);
		}
		otherClickMenu(node);
	}

	public void ImageMenu(Node node) {
		// 创建菜单项
		MenuItem open = new MenuItem("打开");
		MenuItem delete = new MenuItem("删除");
		MenuItem copy = new MenuItem("复制");
		MenuItem rename = new MenuItem("重命名");
		rightClickMenu = new ContextMenu(); // 创建右击菜单
		rightClickMenu.getItems().addAll(open, delete, copy, rename); // 添加菜单项

		// 设置菜单项的点击事件
		open.setOnAction(e -> {
			new OpenAction(this.main);
		});

		delete.setOnAction(e -> {
			new DeleteAction(main);
		});

		copy.setOnAction(e -> {
			new CopyAction();
		});

		rename.setOnAction(e -> {
			new RenameAction(main);
		});

		// 设置事件监听者
		node.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
			ObservableSet<ImageRead> thumbnails = FlowpaneObservable.selectedThumbnailsProperty().get();
			if (e.getButton() == MouseButton.SECONDARY) { // 鼠标右键
				if (thumbnails.size() <= 1)
					thumbnails.clear();
				int number = main.getFlowpane().getChildren().indexOf(node);
//				main.getImages().get(number).setSelected(true);

				thumbnails.add(main.getImages().get(number));
				rightClickMenu.show(node, e.getScreenX(), e.getScreenY()); // 出现右击菜单
			} else {
				if (rightClickMenu.isShowing()) {
					rightClickMenu.hide(); // 取消选中状态
				}
			}
		});

//		node.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
//			if (e.getButton() == MouseButton.SECONDARY) { // 鼠标右键
//
//				rightClickMenu.show(node, e.getScreenX(), e.getScreenY()); // 出现右击菜单
//			} else {
//				if (rightClickMenu.isShowing()) {
//					rightClickMenu.hide(); // 取消选中状态
//				}
//			}
//		});
	}

	// 空白处右击菜单
	public void otherClickMenu(Node node) {
		// 设置右击空白处的菜单项
		MenuItem paste = new MenuItem("粘贴");
		MenuItem allChoosen = new MenuItem("全选");
		ContextMenu mouseRightClickMenu = new ContextMenu(); // 设置右击空白处的弹出菜单
		mouseRightClickMenu.getItems().addAll(allChoosen, paste); // 添加菜单项

		// 对"粘贴"菜单项设置点击事件
		paste.setOnAction(e -> {
			new PasteAction(main);
		});

		// 对"全选"菜单项设置点击事件
		allChoosen.setOnAction(e -> {
			for (Node childNode : main.getFlowpane().getChildren()) {
				// 判断结点是否为图片结点
				if (childNode instanceof ImageRead) {
					((ImageRead) childNode).setSelected(true); // 设置图片为被选中状态
				}
			}
		});

		// 对空白处右击菜单中的每个菜单项设置事件监听者
		node.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
			Node clickNode = e.getPickResult().getIntersectedNode(); // 获取相交节点
			// 鼠标点击非图片结点
			if (clickNode instanceof FlowPane || clickNode instanceof Text) {
				ImageRead.clearSelected(); // 清空已选
				if (e.getButton() == MouseButton.SECONDARY) { // 鼠标右键
					Clipboard clipboard = Clipboard.getSystemClipboard(); // 获取系统剪贴板
					List<File> files = (List<File>) (clipboard.getContent(DataFormat.FILES)); // 获取剪贴板内容中的文件列表
					if (files.size() > 0) {
						paste.setDisable(false); // 若剪贴板中有文件，则可以粘贴
					} else {
						paste.setDisable(true); // 若剪贴板中没有文件，则不可以粘贴
					}
					mouseRightClickMenu.show(node, e.getScreenX(), e.getScreenY()); // 显示菜单
				} else {
					if (mouseRightClickMenu.isShowing()) {
						mouseRightClickMenu.hide(); // 取消选中状态
					}
				}
			} else {
				if (mouseRightClickMenu.isShowing()) {
					mouseRightClickMenu.hide(); // 取消选中状态
				}
			}
		});
	}
}
