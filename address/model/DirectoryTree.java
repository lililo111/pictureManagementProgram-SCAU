package address.model;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import address.ImageRead;
import address.view.MainController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class DirectoryTree {
	private double imageLength;
	private int count;
	private MainController main;
	private TreeItem<ImageFile> root;
	private TreeView<ImageFile> treeView;
	private final File[] rootPath = File.listRoots(); // 获取系统根目录
	private Task<Integer> task;


	// 有参构造方法
	public DirectoryTree(MainController main, TreeView<ImageFile> treeView) {
		this.imageLength = 0;
		this.count = 0;
		this.main = main;
		this.treeView = treeView;
		root = new TreeItem<ImageFile>(new ImageFile("root")); // 创建根目录，名为root
		root.setExpanded(true);
		treeView.setRoot(root);
		treeView.setShowRoot(false); // 不展示根目录root
		this.buildDirectoryTree(); // 创建目录树
		this.choose(); // 显示目录树下的图片
	}

	private void buildDirectoryTree() {
		for (int i = 0; i < rootPath.length; i++) {
			DirectoryTreeItem directoryTreeItem = new DirectoryTreeItem(new ImageFile(rootPath[i]));
			root.getChildren().add(directoryTreeItem); // 将子目录添加到根目录中
		}
	}

	// 显示目录文件下的图片
	private void choose() {
		treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (task != null && task.isRunning()) {
				task.cancel();
			}

			ImageFile imageFile = treeView.getSelectionModel().getSelectedItem().getValue(); // 创建左侧目录树的文件

			main.setImageFile(imageFile);
			System.out.println(imageFile.getImageName());

			MainController.filePath = imageFile.getImageFile().getAbsolutePath(); // 获取文件夹的路径
			main.clearImages();
			imageLength = 0;
			count = 0;
			main.getLabel1().setText(String.format("共有" + count + "张图片(" + imageLength + "MB)"));
			main.getLabel2().setText(String.format("已选择0张图片"));
			if (newValue != null) {
				main.getFlowpane().getChildren().remove(0, main.getFlowpane().getChildren().size()); // 清除FlowPane中所有孩子结点
				ImageFile file = newValue.getValue();
				main.getFlowpane().getChildren().clear();

				ImageFile[] files = file.listImages();
				task = new Task<Integer>() { // task和线程相结合处理阻塞情况
					@Override
					protected Integer call() throws Exception {
						for (ImageFile file1 : files) {
							if (isCancelled()) {
								break;
							}

							Platform.runLater(() -> {
								if (this.isCancelled()) {
									return;
								}
								if (file1.isImage()) {
									imageLength += file1.length();
									main.getLabel1().setText(
											String.format("共有%d张图片(%.2fMB)", ++count, imageLength / (1024 * 1024)));
									ImageRead image = new ImageRead(file1, main);
									main.getFlowpane().getChildren().add(image);
									main.addImages(image);
								}
							});

							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								if (this.isCancelled()) {
									break;
								}
							}
						}

						return count;
					}
				};

				new Thread(task).start();
			}
		});
	}


	

	// getting
	public TreeView<ImageFile> getTreeView() {
		return this.treeView;
	}
}
