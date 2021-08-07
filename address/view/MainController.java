package address.view;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

import address.DataService;
import address.FlowpaneObservable;
import address.ImageRead;
import address.OpenAction;
import address.action.CopyAction;
import address.action.DeleteAction;
import address.action.PasteAction;
import address.action.SearchAction;
import address.model.DirectoryTree;
import address.model.ImageFile;
import address.model.RightClickMenu;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeView;
import javafx.scene.layout.FlowPane;

public class MainController implements Initializable {
	private MainController main = this;
	private double imageLength = 0;
	private int count = 0;
	public static String filePath;
	@FXML
	private TreeView<ImageFile> treeView;
	@FXML
	private FlowPane flowpane;
	@FXML
	private Label label1;
	@FXML
	private Label label2;
	
	@FXML
	private TextField textFiled;
	@FXML
	private Button search_btn;

	@FXML
	private ToolBar toolBar;
	@FXML
	private Button open_btn;
	@FXML
	private Button delete_btn;
	@FXML
	private Button copy_btn;
	@FXML
	private Button paste_btn;
	@FXML
	private Button play_btn;

	private ArrayList<File> files;
	private ArrayList<ImageRead> images;
	public ArrayList<File> fs;
	public ImageFile imageFile;
	


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initData();
	}

	public void initData() {
		
		images = new ArrayList<>();
		new FlowpaneObservable(this, flowpane);
		treeView = new DirectoryTree(this, treeView).getTreeView();
		new RightClickMenu(this, flowpane, false);
		open_btn.setTooltip(new Tooltip("幻灯片播放"));
		delete_btn.setTooltip(new Tooltip("删除"));
		copy_btn.setTooltip(new Tooltip("复制"));
		paste_btn.setTooltip(new Tooltip("粘贴"));
	}


	public ImageFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(ImageFile imageFile) {
		this.imageFile = imageFile;
	}
	
	public ArrayList<File> getFs() {
		return fs;
	}

	public void setFs(ArrayList<File> fs) {
		this.fs = fs;
	}
	
	public ObservableList<Node> getChildren() {
		return flowpane.getChildren();
	}

	public TreeView<ImageFile> getTreeView() {
		return treeView;
	}

	public void setTreeView(TreeView<ImageFile> treeView) {
		this.treeView = treeView;
	}

	public FlowPane getFlowpane() {
		return flowpane;
	}

	public void setFlowpane(FlowPane flowpane) {
		this.flowpane = flowpane;
	}

	public Label getLabel1() {
		return label1;
	}

	public Label getLabel2() {
		return label2;
	}
	
	public TextField getTextFiled() {
		return textFiled;
	}

	public void setTextFiled(TextField textFiled) {
		this.textFiled = textFiled;
	}

	public Button getSearch_btn() {
		return search_btn;
	}

	public void setSearch_btn(Button search_btn) {
		this.search_btn = search_btn;
	}

	// 增加
	public void addImages(ImageRead imageNode) {
		this.images.add(imageNode);
	}

	// 清除
	public void clearImages() {
		this.images.clear();
	}

	public ArrayList<ImageRead> getImages() {
		return this.images;
	}

	public void removeImages(ImageRead imageNode) {
		for (ImageRead eachImageNode : images) {
			if (eachImageNode.equals(imageNode)) {
				images.remove(imageNode);
				break;
			}
		}
	}

	public void showImage() {

		this.getLabel1().setText("共有" + this.flowpane.getChildren().size() + "张图片");

		files = new ArrayList<File>();

		for (int i = 0; i < images.size(); i++) {
			files.add(images.get(i).getImageFile());
		}
		DataService.files = files;
	}

	public void refresh() { // 刷新图片
		Collections.sort(this.getImages(), new Comparator<ImageRead>() {

			@Override
			public int compare(ImageRead o1, ImageRead o2) {
				return o1.getImageFile().getName().compareTo(o2.getImageFile().getName());
			}
		});

		count = 0;
		this.getFlowpane().getChildren().remove(0, this.getFlowpane().getChildren().size());
		this.getLabel2().setText(String.format("已选中0张图片"));
		Task<Integer> task = new Task<Integer>() { // task和线程相结合处理阻塞情况
			@Override
			protected Integer call() throws Exception {
				for (ImageRead imageNode : getImages()) {
					if (isCancelled()) {
						break;
					}

					Platform.runLater(() -> {

						if (this.isCancelled()) {
							return;
						}
						imageLength += imageNode.getImageFile().length();
						main.getLabel1()
								.setText(String.format("共有%d张图片(%.2fMB)", ++count, imageLength / (1024 * 1024)));
						getFlowpane().getChildren().add(imageNode);

					});

					try {
						Thread.sleep(30);
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

	@FXML
	public void open_btnAction(ActionEvent event) {
		new OpenAction(this);
	}

	@FXML
	public void delete_btnAction(ActionEvent event) {
		new DeleteAction(this);
	}

	@FXML
	public void copy_btnAction(ActionEvent event) {
		new CopyAction();
	}

	@FXML
	public void paste_btnAction(ActionEvent event) {
		new PasteAction(this);
	}
	@FXML
	public void search_btnAction(ActionEvent event) {
		new SearchAction(this);
	}
}
