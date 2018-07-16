package kireyis.client.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import kireyis.client.Client;
import kireyis.client.GameLoop;

public class PopupPanel extends Group {
	public PopupPanel() {
		Rectangle background = new Rectangle(1280, 720);
		background.setFill(Color.rgb(0, 0, 0, 0.5));
		this.getChildren().add(background);

		Button disconnect = new Button("Disconnect");
		disconnect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				GameLoop.stop();
				Client.close();
			}
		});

		this.getChildren().add(disconnect);
	}
}
