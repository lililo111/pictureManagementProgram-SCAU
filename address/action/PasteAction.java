package address.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import address.ImageRead;
import address.model.ImageFile;
import address.view.MainController;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

// 粘贴事件类
public class PasteAction {
	MainController main;

	// 有参构造方法
	public PasteAction(MainController main) {
		this.main = main;
		Clipboard clipboard = Clipboard.getSystemClipboard(); // 获取系统剪贴板
		List<File> files = (List<File>) (clipboard.getContent(DataFormat.FILES)); // 获取剪贴板中的文件内容
		// clipboard.clear(); // 清空剪贴板

		// 若文件为空则返回
		if (files.size() <= 0) {
			return;
		}

		for (File oldFile : files) {
			String newName = pastedName(main.filePath, oldFile.getName());
			File newFile = new File(main.filePath + File.separator + newName); // 创建文件
			try {
				newFile.createNewFile();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			// 判断新建的文件是否已存在
			if (newFile.exists()) {
				try {
					copyFile(oldFile, newFile);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			try {
				main.getFlowpane().getChildren().add(new ImageRead(new ImageFile(newFile), main));
				main.addImages(new ImageRead(new ImageFile(newFile), main));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		main.refresh();
	}

	// 修改粘贴后重复文件的名字
	private String pastedName(String filePath, String oldName) {
		File parentPathFile = new File(filePath);
		File[] filesInParent = parentPathFile.listFiles();
		String newName = oldName;
		int count = 0;
		int flag2 = 0;
		if (oldName.lastIndexOf("_") > 0) {
			flag2 = 1;
		}
		int dot = oldName.lastIndexOf("_");
		for (File fileInParent : filesInParent) {
			String fileName = fileInParent.getName();
			int flag = newName.compareTo(fileName);
			String tempName = new String();
			// 判断是否有同名文件
			if (flag == 0) {
				count++;
				int dot1 = newName.lastIndexOf("_");
				int dot2 = newName.lastIndexOf(".");

				if (dot1 > 0) {
					// newName = newName.substring(0, dot) + "_副本(" + count + ")" +
					// newName.substring(dot);\
					// tempName = newName.substring(0,)
					// tempName = newName.substring(dot2);
					if (flag2 == 0 || dot1 != dot)
						newName = newName.substring(0, dot1) + "_副本(" + count + ")" + newName.substring(dot2);
					else {
						newName = newName.substring(0, dot2) + "_副本(" + count + ")" + newName.substring(dot2);
					}
				} else {
					tempName = newName.substring(0, dot2);
					newName = tempName + "_副本(" + count + ")" + newName.substring(dot2);
				}

			}
		}
		return newName;
	}

	private void copyFile(File fromFile, File toFile) throws IOException {
		FileInputStream fis = new FileInputStream(fromFile);
		FileOutputStream fos = new FileOutputStream(toFile);
		byte[] bytes = new byte[1024];
		int byteLength;
		while ((byteLength = fis.read(bytes)) > 0) {
			fos.write(bytes, 0, byteLength);
		}
		fis.close();
		fos.close();
	}
}
