package address.model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

// 目录树项类
public class DirectoryTreeItem extends TreeItem<ImageFile> {
	private boolean isLeaf;
	private boolean isFirstTimeChildren = true;
	private boolean isFirstTimeLeaf = true;

	// 有参构造方法
	public DirectoryTreeItem(ImageFile imageFile) {
		super(imageFile); // 调用父类TreeItem的构造方法
	}

	// 重写getChildren方法
	@Override
	public ObservableList<TreeItem<ImageFile>> getChildren() {
		// 判断是否是第一个孩子
		if (isFirstTimeChildren) {
			isFirstTimeChildren = false;
			super.getChildren().setAll(buildChildren(this));
		}
		return super.getChildren();
	}

	// 重写isLeaf方法
	@Override
	public boolean isLeaf() {
		// 判断是否是第一个叶子结点
		if (isFirstTimeLeaf) {
			isFirstTimeLeaf = false;
			ImageFile imageFile = (ImageFile) super.getValue(); // 获取目录树项的值并转化为ImageFile类型
			ImageFile[] imageFiles = imageFile.listImages();
			if (imageFiles == null || imageFiles.length == 0) {
				isLeaf = true;
			} else {
				isLeaf = true;
				for (ImageFile eachImageFile : imageFiles) {
					// 如果该文件不为空，且子文件中含有文件夹则不是叶子结点
					if (eachImageFile.isDirectory()) {
						isLeaf = false;
					}
				}
			}
		}
		return isLeaf;
	}

	// buildChildren方法用于构造孩子结点
	private ObservableList<TreeItem<ImageFile>> buildChildren(TreeItem<ImageFile> TreeItem) {
		ImageFile imageFile = TreeItem.getValue();
		// 判断该文件是否为空，或是否为文件类型，若是则返回空列表
		if (imageFile == null || imageFile.isFile()) {
			return FXCollections.emptyObservableList();
		}
		ImageFile[] imageFiles = imageFile.listImages();
		if (imageFiles != null && imageFiles.length != 0) {
			ObservableList<TreeItem<ImageFile>> children = FXCollections.observableArrayList();
			for (ImageFile childFile : imageFiles) {
				// 若子文件不是文件夹则不加入子文件序列
				if (childFile.isFile()) {
					continue;
				}
				children.add(new DirectoryTreeItem(childFile));
			}
			return children;
		}
		return FXCollections.emptyObservableList();
	}
}

