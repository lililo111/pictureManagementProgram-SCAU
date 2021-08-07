package address.model;
import java.io.File;
import java.net.URL;

// 图片文件类
public class ImageFile {
	private File imageFile;
	private String imageName;
	private URL imageURL;


	// 有参构造方法,参数为File类
	public ImageFile(File imageFile) {
		this.imageFile = imageFile; // 获取文件
		this.imageName = imageFile.getName(); // 获取文件名

		// 若文件名为空，则将其命名为文件路径
		if (imageName.equals("")) {
			this.imageName = imageFile.getPath();
		}
	}

	// 重载有参构造方法,参数为String类
	public ImageFile(String path) {
		this(new File(path)); // 将其转化为File类
	}

	// 获取图片文件列表的方法
	public ImageFile[] listImages() {
		File[] files = this.imageFile.listFiles(); // 获取当前文件下所有的文件和文件夹，listFiles方法返回类型为File[]
		// 对获取的文件进行处理
		// 若文件为空，返回null
		if (files == null) {
			return null;
		}
		ImageFile[] imageFiles = new ImageFile[files.length];
		// 将获取的文件转换为ImageFile类
		for (int i = 0; i < files.length; i++) {
			imageFiles[i] = new ImageFile(files[i]);
		}
		return imageFiles; // 返回文件列表
	}

	// isImage方法判断文件是否为图片
	public boolean isImage() {
		if (imageName.toLowerCase().endsWith(".jpg") || imageName.toLowerCase().endsWith(".jpge")
				|| imageName.toLowerCase().endsWith(".bmp") || imageName.toLowerCase().endsWith(".png")
				|| imageName.toLowerCase().endsWith(".gif")) {
			return true;
		} else {
			return false;
		}
	}

	// isFile方法判断是否为文件
	public boolean isFile() {
		return imageFile.isFile();
	}
	
	public File getFile() {
		return this.imageFile;
	}

	// isDirectory方法判断是否为文件夹
	public boolean isDirectory() {
		return imageFile.isDirectory();
	}

	// 判断文件是否被隐藏
	public boolean isHidden() {
		return imageFile.isHidden();
	}

	// 获取文件大小
	public long length() {
		return imageFile.length();
	}

	@Override
	public String toString() {
		return imageName;
	}

	// getting and setting
	public File getImageFile() {
		return imageFile;
	}

	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}

	public String getImageName() {
		return imageName;
	}
	public URL toURL() {
    	return imageURL;
    }

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
}
