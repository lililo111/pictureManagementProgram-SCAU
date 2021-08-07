package address;
import java.io.File;
import java.util.ArrayList;

import javafx.stage.Stage;

// 提供数据服务，各种数据调用均在此类中
public class DataService {
	public static Stage stage;
	public static File file;
	public static ArrayList<File> files;

	protected static ArrayList<File> selectedImageFiles = new ArrayList<>();
	protected static ArrayList<ImageRead> selectedImages = new ArrayList<>();
	protected static ArrayList<ImageRead> cutedImages = new ArrayList<>();
}
