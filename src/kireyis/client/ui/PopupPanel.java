package kireyis.client.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import kireyis.client.Client;

public class PopupPanel extends Group {
	private final Rectangle background = new Rectangle();

	public PopupPanel() {
		background.setFill(Color.rgb(0, 0, 0, 0.5));
		this.getChildren().add(background);

		final Button disconnect = new Button("Disconnect");
		disconnect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				Client.disconnect();
			}
		});

		this.getChildren().add(disconnect);
	}

	public void update() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				background.setHeight(Window.getHeight());
				background.setWidth(Window.getWidth());
			}
		});
	}
}
