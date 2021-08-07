 package address;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

import address.model.ImageFile;
import address.model.RightClickMenu;
import address.view.MainController;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class ImageRead extends BorderPane {
	private ImageFile imageFile;
	private Label picName;
	private Pane imagePane;
	private Canvas canvas;
	private MainController main;
	private Image image;
	private ImageFile file;
	private ImageRead node;
	public BooleanProperty selected = new SimpleBooleanProperty();

	protected static ArrayList<File> selectedPictureFiles = new ArrayList<>();
	protected static ArrayList<ImageRead> selectedPictures = new ArrayList<>();
	protected static ArrayList<ImageRead> cutedPictures = new ArrayList<>();

	public ImageRead(ImageFile file, MainController main) {
		super();
		this.main = main;
		this.node = this;
		this.imageFile = file;
		this.file = file;
		initData();
		NodeListener();
		new RightClickMenu(main, this, true);
	}

	public void initData() {
		canvas = new Canvas(160, 120); // 创建绘制图片的画布
		GraphicsContext gc = canvas.getGraphicsContext2D();

		try {
			this.image = new Image(file.getImageFile().toURI().toURL().toString(), 160, 120, true, true);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		double x = (160 - image.getWidth()) / 2;
		double y = (120 - image.getHeight()) / 2;
		gc.drawImage(image, x, y);

		picName = new Label(this.file.getImageName());
		picName.prefWidthProperty().bind(canvas.widthProperty());
		picName.setAlignment(Pos.CENTER);
		picName.setTooltip(new Tooltip(this.file.getImageName()));

		imagePane = new Pane();
		imagePane.setStyle("-fx-background-color: lightgray;");
		imagePane.getChildren().add(canvas);

		this.setStyle("-fx-background-color: white; -fx-hover: lightblue;");
		this.setMaxWidth(160);
		this.setHover(true);
		this.setCenter(imagePane);
		this.setBottom(picName);

	}

	public void setSelected(boolean value) {
		boolean istrue = selected.get();
		selected.set(value);
		if (selected.get() && !istrue)
			selectedPictures.add(this);
		else if (istrue && !selected.get())
			selectedPictures.remove(this);
//		System.out.println(selectedPictures.size());
		this.main.getLabel2().setText(String.format("已选中" + selectedPictures.size() + "张图片"));
	}

	public void setSelected2(boolean value) {
		selected.set(value);
	}

	public static void clearSelected() {
		for (ImageRead pNode : selectedPictures) {
			pNode.selected.set(false);
		}
		selectedPictures.removeAll(selectedPictures);
	}

	// 选中的图片的list
	public static ArrayList<File> getSelectedPictureFiles() {
		return selectedPictureFiles;
	}

	public static ArrayList<ImageRead> getSelectedPictures() {
		return selectedPictures;
	}

	public static void setCutedPictures(ArrayList<ImageRead> cutedPictures) {
		ImageRead.cutedPictures = cutedPictures;
	}

	public static void addCutedPictures(ImageRead pNode) {
		ImageRead.cutedPictures.add(pNode);
	}

	public static ArrayList<ImageRead> getCutedPictures() {
		return cutedPictures;
	}

	public static void clearCutedPictures() {
		cutedPictures.removeAll(cutedPictures);
	}

	public String getURL() {
		try {
			return this.imageFile.getImageFile().toURI().toURL().toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void NodeListener() {

		selected.addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				if (selected.get()) {
					node.setStyle("-fx-background-color:gray;");
//					main.getLabel().setText("已选中 0  张图片");
//					mainScene.getTextTwo().setText("已选中 0  张图片");
				} else {
					node.setStyle("-fx-background-color:transparent;");
//					main.getLabel().setText("已选中 0  张图片");
//					mainScene.getTextTwo().setText("已选中 0  张图片");
				}
			}
		});

		this.setOnMouseEntered((MouseEvent e) -> { // 鼠标进入
			if (!selected.get())
				ImageRead.this.setStyle("-fx-background-color:#a7a7a7;");
//			    mainScene.getText().setText("");

		});
		this.setOnMouseExited((MouseEvent e) -> { // 鼠标移走
			if (!selected.get())
				this.setStyle("-fx-background-color:transparent;");

		});

		this.setOnMouseClicked(mouseEvent -> {
			// 鼠标事件
			ObservableSet<ImageRead> thumbnails = FlowpaneObservable.selectedThumbnailsProperty().get();
			if (mouseEvent.getButton() == MouseButton.PRIMARY && !mouseEvent.isControlDown()
					&& mouseEvent.getClickCount() == 1) { // 左键
				// System.out.println(thumbnails.contains(Thumbnail.this));
				if (thumbnails.contains(ImageRead.this)) {
					thumbnails.retainAll(FXCollections.observableSet(ImageRead.this));
				} else {
					thumbnails.clear();
					thumbnails.add(ImageRead.this);
				}
			} else if (mouseEvent.isControlDown()) { // 按了ctrl
				thumbnails.add(ImageRead.this);
				this.setSelected2(true);
			} else if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
				System.out.println(2);
				new OpenAction(this.main);
			} 
		});

	}

//	public void openAction() {
//		new OpenAction(this.main,this);
//	}

	public File getImageFile() {
		return this.imageFile.getImageFile();
	}

	public ImageFile getPictureFile() {
		return imageFile;
	}
}
