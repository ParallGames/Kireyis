package kireyis.client.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import kireyis.client.Client;

public class PopupPanel extends Group {
	public PopupPanel() {
		final Rectangle background = new Rectangle(1280, 720);
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
}
