package address.action;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import address.ImageRead;
import address.model.ImageFile;
import address.view.MainController;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class SearchAction {
	private double imageLength;
	private Task<Integer> task;
	private int count;
	private ImageFile[] files;
	public static ArrayList<File> result = new ArrayList<File>();
	
	public SearchAction(MainController main) {
		// 若无图片则返回
		result = searchFiles(main.getImageFile().getFile(), main.getTextFiled().getText());
		if(result.isEmpty()) {
			return;
		}
		
//		if (main.getFlowpane().getChildren().size() <= 0) {
//			return;
//		}
		this.imageLength = 0;
		this.count = 0;
//		files = new ImageFile[main.getImages().size()];
		main.getFlowpane().getChildren().clear();
		main.getImages().clear();
		
		System.out.println(main.getTextFiled().getText());
		System.out.println(this.result.size());
//		
//		for (int i = 0; i < main.getImages().size(); i++) {
//				files[i] = main.getImages().get(i).getPictureFile();
//		}
		
		task = new Task<Integer>() { // task和线程相结合处理阻塞情况
			@Override
			protected Integer call() throws Exception {
				for (int i = 0; i < result.size(); i++) {
					File file1 = result.get(i);
					if (isCancelled()) {
						break;
					}

					Platform.runLater(() -> {
						if (this.isCancelled()) {
							return;
						}
						if ((file1.getName().endsWith(".jpg") || file1.getName().endsWith(".png")
								|| file1.getName().endsWith(".glf")) && file1.getName().contains(main.getTextFiled().getText())) {
							
							imageLength += file1.length();
							main.getLabel1().setText(
									String.format("共有%d张图片(%.2fMB)", ++count, imageLength / (1024 * 1024)));
							
							ImageFile file = new ImageFile(file1);
							ImageRead image = new ImageRead(file, main);
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
	
	public static ArrayList<File> searchFiles(File folder, final String keyword) {
		System.out.println("k");
		ArrayList<File> result = new ArrayList<File>();
		if (folder.isFile())
			result.add(folder);

		File[] subFolders = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				}
				if (file.getName().toLowerCase().contains(keyword)) {
					return true;
				}
				return false;
			}
		});

		if (subFolders != null) {
			for (File file : subFolders) {
				if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")
						|| file.getName().endsWith(".glf")) {
					// 如果是文件则将文件添加到结果列表中
					result.add(file);
				} else {
					// 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
					result.addAll(searchFiles(file, keyword));
				}
			}
		}

		return result;
	}
	
	
}
