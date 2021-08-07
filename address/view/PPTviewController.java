package address.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import address.ImageRead;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class PPTviewController implements Initializable {
	MainController main;
	private int index;
	@FXML
	private Button bigger;

	@FXML
	private Button smaller;

	@FXML
	private Button begin;

	@FXML
	private Button stop;

	@FXML
	private BorderPane borderpane;

    @FXML
    private Button rotate;
    
	@FXML
	private Pagination pagination;
	private  String[] photos;
	Timeline timeline = new Timeline();

	private static int[] changeNum = new int[1000];

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initData();
	}

	private void initData() {
		// 初始化
		
		bigger.setTooltip(new Tooltip("放大"));
		smaller.setTooltip(new Tooltip("缩小"));
		begin.setTooltip(new Tooltip("开始播放"));
		stop.setTooltip(new Tooltip("暂停播放"));
		rotate.setTooltip(new Tooltip("旋转图片"));
		if (photos != null) {
			Pagination pagination1;
			pagination1 = new Pagination(photos.length, index);
			pagination1.setPageFactory((Integer pageIndex) -> {
				Image image = new Image(photos[pageIndex]);
				ImageView imageview =new ImageView(image);
				double width = image.getWidth();
				double height = image.getHeight();
				double numw = width/800;
				double numh = height/500;
				double fitwidth,fitheight;
				if(numw<=1&&numh<=1) {
					fitwidth=0;
					fitheight=0;
				}else if(numw<=1 && numh>1 ) {
					fitwidth = width /numh;
					fitheight = height / numh;
				}else {
					double num = Math.max(numw, numh);
					fitwidth = width/num;
					fitheight = height / num;
				}
				//System.out.println("fitwidth:"+fitwidth);
				//System.out.println("fitheight:"+fitheight);
				imageview.setFitWidth(fitwidth);
				imageview.setFitHeight(fitheight);
				return  imageview;
			});
			borderpane.setCenter(pagination1);
			pagination = pagination1;
			// 初始化大小数组changeNum
			for (int i = 0; i < changeNum.length; i++)
				changeNum[i] = 0;
		
		}
		borderpane.setPrefSize(900, 750);
	}

	@FXML
	void Begin(ActionEvent event) {
		System.out.println("Index:" + pagination.getCurrentPageIndex());
		System.out.println(timeline.getStatus());
		if (timeline.getStatus() == Animation.Status.STOPPED) {
			// 创建时间轴动画
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.setAutoReverse(true);// 自动无限循环播放
			timeline.getKeyFrames().add((new KeyFrame(Duration.seconds(1), event1 -> {
				int pos = (pagination.getCurrentPageIndex() + 1) % pagination.getPageCount();
				pagination.setCurrentPageIndex(pos);
				System.out.println("Pos:" + pagination.getCurrentPageIndex());
			})));
			timeline.play();
		}
		if (timeline.getStatus() == Animation.Status.PAUSED) {
			timeline.play();
		}

		System.out.println(timeline.getStatus());
	}

	@FXML
	void Stop(ActionEvent event) {
		if (timeline.getStatus() == Animation.Status.RUNNING)
			timeline.pause();

		System.out.println(timeline.getStatus());
	}

	@FXML
	void Bigger(ActionEvent event) {
		changeNum[pagination.getCurrentPageIndex()] += 1;
		ChangeSize();
	}

	@FXML
	void Smaller(ActionEvent event) {
		changeNum[pagination.getCurrentPageIndex()] -= 1;
		ChangeSize();
	}

	private void ChangeSize() {
		ImageView imageView = new ImageView();
		imageView = (ImageView) pagination.getPageFactory().call(pagination.getCurrentPageIndex());

		double prefWidth = 700 * (changeNum[pagination.getCurrentPageIndex()] * 0.1 + 1);
		double prefHeight = 500 * (changeNum[pagination.getCurrentPageIndex()] * 0.1 + 1);

		if (prefWidth > 800.0 || prefHeight > 510.0) {
			Alert alert1 = new Alert(AlertType.INFORMATION);
			alert1.titleProperty().set("提示");
			alert1.headerTextProperty().set("图片已放至最大");
			alert1.initStyle(StageStyle.TRANSPARENT);
			alert1.showAndWait();
			return;
		} else if (prefWidth < 79.0 || prefHeight < 79.0) {
			Alert alert2 = new Alert(AlertType.INFORMATION);
			alert2.titleProperty().set("提示");
			alert2.headerTextProperty().set("图片已缩小至最小");
			alert2.initStyle(StageStyle.TRANSPARENT);

			alert2.showAndWait();
			return;
		}
		//System.out.println("Width:" + prefWidth);
		//System.out.println("Height:" + prefHeight);
		imageView.setFitWidth(prefWidth);
		imageView.setFitHeight(prefHeight);

		imageView.setPreserveRatio(true);

		this.setNewPagination(imageView);
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPhotos(String[] pictures, MainController main) {
		main = main;
		photos = new String[pictures.length];
		for (int i = 0; i < pictures.length; i++) {
			photos[i] = pictures[i];
		}
		initData();
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	@FXML
    void Rotate(ActionEvent event) {
		ImageView imageView = new ImageView();
		imageView = (ImageView) pagination.getPageFactory().call(pagination.getCurrentPageIndex());
		imageView.setRotate((imageView.getRotate()+90)%360);
		this.setNewPagination(imageView);
		
    }
	private void setNewPagination(ImageView imageView) {
		int index = pagination.getCurrentPageIndex();

		ImageView[] imageV = new ImageView[pagination.getPageCount()];
		for (int i = 0; i < pagination.getPageCount(); i++) {
			if (i == pagination.getCurrentPageIndex())
				imageV[i] = imageView;
			else {				
				Image image = new Image(photos[i]);
				ImageView imageview =new ImageView(image);
				double width = image.getWidth();
				double height = image.getHeight();
				double numw = width/800;
				double numh = height/500;
				double fitwidth,fitheight;
				if(numw<=1&&numh<=1) {
					fitwidth=0;
					fitheight=0;
				}else if(numw<=1 && numh>1 ) {
					fitwidth = width /numh;
					fitheight = height / numh;
				}else {
					double num = Math.max(numw, numh);
					fitwidth = width/num;
					fitheight = height / num;
				}
				imageview.setFitWidth(fitwidth);
				imageview.setFitHeight(fitheight);
				imageV[i] = imageview;
			}
		}

		Pagination pagination1 = new Pagination(pagination.getPageCount(), index);

		pagination1.setPageFactory((Integer pageIndex) -> {

			return imageV[pageIndex];
		});
		borderpane.setCenter(pagination1);
		pagination = pagination1;
	}
	
}
