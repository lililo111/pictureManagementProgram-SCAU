package address;

import java.util.HashSet;

import address.view.MainController;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

public class FlowpaneObservable {
	
	private static SimpleSetProperty<ImageRead> selectedThumbnailsProperty = new SimpleSetProperty<>(
			FXCollections.observableSet(new HashSet<ImageRead>()));
	
	private ObservableList<Node>  la;
	private FlowPane flowpane;
	private MainController main;
	Node node;
	private Label labelPane;

	public FlowpaneObservable(MainController main, FlowPane flowpane) {
		this.node = flowpane;
		this.flowpane = flowpane;
		ObservableList<Node>  la = main.getFlowpane().getChildren();
		this.main = main;
		mouseEvent();
	}

	public void mouseEvent() {
		
		selectedThumbnailsProperty.addListener(new SetChangeListener<ImageRead>() {

			@Override
			public void onChanged(Change<? extends ImageRead> change) {
				// System.out.println(change);
				// System.out.println("------------------");
				if (change.wasAdded()) {
					change.getElementAdded().setSelected(true);
				}
				if (change.wasRemoved()) {
					change.getElementRemoved().setSelected(false);
				}
			}

		});
		

		
		flowpane.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// 如果直接点击在thumbnailPane上面
				if (event.getPickResult().getIntersectedNode() == flowpane) {
					selectedThumbnailsProperty.get().clear();
				}
			}

		});	
		
	}
	

	public static SimpleSetProperty<ImageRead> selectedThumbnailsProperty() {
		return selectedThumbnailsProperty;
	}
}
