package address.action;


import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

import address.model.ImageFile;
import address.ImageRead;
import address.view.MainController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

// 重命名事件类
public class RenameAction {
	private int count = 0;
	private MainController main;
	private Label label;
	private Button submit;
	private GridPane gridPane;
	private Stage stage;
	final private TextField name = new TextField(); // 名称前缀
	final private TextField startNum = new TextField(); // 起始编号
	final private TextField bitNum = new TextField(); // 编号位数
	private boolean single; // 单张或多张图片的标志

	// 有参构造方法
	public RenameAction(MainController main) {
		this.main = main;
		// 判断选中单张还是多张图片
		if (ImageRead.getSelectedPictures().size() == 1) {
			single = true;
		} else {
			single = false;
		}
		label = new Label();
		submit = new Button("完成");
		gridPane = new GridPane();
		stage = new Stage();
		setStage();
	}

	// 布置新舞台
	private void setStage() {
		// 对网络面板的格式进行处理
		gridPane.setVgap(5);
		gridPane.setHgap(5);
		gridPane.setPadding(new Insets(10));

		Label label_1 = new Label("新文件名");
		GridPane.setConstraints(label_1, 0, 0); // 设定标签的位置
		name.setPrefColumnCount(20); // 设定文件名输入框的宽度为20列
		name.setPromptText("请输入新文件名");
		name.getText();
		GridPane.setConstraints(name, 1, 0); // 设文件名输入框的位置
		gridPane.getChildren().addAll(label_1, name);

		// 选中单张图片
		if (single) {
			GridPane.setConstraints(label, 0, 1);
			GridPane.setConstraints(submit, 0, 2);
			gridPane.getChildren().addAll(label, submit);
		} else { // 选中多张图片
			Label label_2 = new Label("起始编号");
			Label label_3 = new Label("编号位数");
			GridPane.setConstraints(label_2, 0, 1);
			GridPane.setConstraints(label_3, 0, 2);

			startNum.setPrefColumnCount(15);
			startNum.setPromptText("请输入起始编号");
			startNum.getText();
			bitNum.setPrefColumnCount(15);
			bitNum.setPromptText("请输入编号位数");
			bitNum.getText();

			// 放置控件
			GridPane.setConstraints(startNum, 1, 1);
			GridPane.setConstraints(bitNum, 1, 2);
			GridPane.setConstraints(label, 1, 3);
			GridPane.setConstraints(submit, 0, 4);
			gridPane.getChildren().addAll(label_2, startNum, label_3, bitNum, submit, label);
		}

		// 对完成按钮设置事件
		submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// 选中单张图片
				if (single) {
					// 判断文件名是否为空
					if ((name.getText() != null && !name.getText().isEmpty())) {
						if (renameSingle()) {
							stage.close();
						} else {
							label.setText("已有相同名字的图片存在，请重新输入！");
						}
					} else {
						label.setText("你还没有输入，请输入！");
					}
				} else {
					if (name.getText() != null && !name.getText().isEmpty()
							&& (startNum.getText() != null && !startNum.getText().isEmpty())
							&& (bitNum.getText() != null && !bitNum.getText().isEmpty())) {
						if (renameNotSingle()) {
							stage.close();
						} else {
							label.setText("错误！");
						}
					} else {
						label.setText("你还没有输入以上内容，请输入！");
					}
				}
			}
		});
		Scene scene = new Scene(gridPane);
		stage.setScene(scene);
		stage.setTitle("重命名");
		stage.show();
	}

	// 重命名单张图片
	private boolean renameSingle() {
		ImageRead imageNode = ImageRead.getSelectedPictures().get(0);
		File file = imageNode.getImageFile();
		String pre = file.getParent();
		String[] strings = file.getName().split("\\.");
		String suf = strings[strings.length - 1];
		File tmp = new File(pre + "\\" + name.getText() + "." + suf);
		if (!file.renameTo(tmp)) {
			tmp.delete();
			return false;
		}
		imageNode.setSelected(false);
		main.removeImages(imageNode);
		ImageRead aNode;
		try {
			aNode = new ImageRead(new ImageFile(tmp),main);
			aNode.setSelected(true);
			main.addImages(aNode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		main.refresh();
		return true;
	}

	// 重命名多张图片
	private boolean renameNotSingle() {
		File file;
		int id = Integer.valueOf(startNum.getText());
		int bit = Integer.valueOf(bitNum.getText());
		if (id < 0 || (id + ImageRead.getSelectedPictures().size()) >= (int) Math.pow(10, bit))
			return false;
		ArrayList<ImageRead> oldList = new ArrayList<>();
		ArrayList<ImageRead> newList = new ArrayList<>();
		for (ImageRead image : ImageRead.getSelectedPictures()) {
			file = image.getImageFile();
			String pre = file.getParent();
			String[] strings = file.getName().split("\\.");
			String suf = strings[strings.length - 1];
			String newName = createName(id, bit);

			File tmp = new File(pre + "\\" + newName + "." + suf);
			if (!file.renameTo(tmp)) {// 可能存在失败的情况,如名字重复
				tmp.delete();
				return false;
			}
			oldList.add(image);
			ImageRead newImage;
			try {
				newImage = new ImageRead(new ImageFile(tmp),main);
				newList.add(newImage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			id++;
		}
		for (int i = 0; i < oldList.size(); i++) {
			oldList.get(i).setSelected(false);
			main.removeImages(oldList.get(i));
			newList.get(i).setSelected(true);
			main.addImages(newList.get(i));
		}
		main.refresh();;
		return true;
	}

//	public void refresh() {
//		count = 0;
//		main.getFlowpane().getChildren().remove(0, main.getFlowpane().getChildren().size());
//		main.getLabel2().setText(String.format("已选中0张图片"));
//		Task<Integer> task = new Task<Integer>() { // task和线程相结合处理阻塞情况
//			@Override
//			protected Integer call() throws Exception {
//				for (ImageRead imageNode : main.getImages()) {
//					if (isCancelled()) {
//						break;
//					}
//
//					Platform.runLater(() -> {
//						if (this.isCancelled()) {
//							return;
//						}
//							main.getLabel1().setText(String.format("共有" + ++count + "张图片"));
//							main.getFlowpane().getChildren().add(imageNode);
//					
//					});
//
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						if (this.isCancelled()) {
//							break;
//						}
//					}
//				}
//
//				return count;
//			}
//		};
//		new Thread(task).start();
//	}
	
	// 新建文件名
	private String createName(int id, int bit) {
		String newName = name.getText();

		int d = id;
		int count = 0;
		while (d != 0) {
			count++;
			d /= 10;
		}
		if (id == 0)
			count++;
		while (bit > count) {
			newName += 0;
			count++;
		}
		newName += id;
		return newName;
	}
}
