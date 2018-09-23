package kireyis.client.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import kireyis.client.Client;
import kireyis.client.GameLoop;

public class ConnectionPanel extends Group {

	public ConnectionPanel() {

		final Text ipT = new Text("IP : ");
		ipT.setTranslateX(10);
		ipT.setTranslateY(46);

		final Text pseudoT = new Text("Pseudo : ");
		pseudoT.setTranslateX(10);
		pseudoT.setTranslateY(78);

		final TextField ipTF = new TextField();
		ipTF.setTranslateX(70);
		ipTF.setTranslateY(32);

		final TextField pseudoTF = new TextField();
		pseudoTF.setTranslateX(70);
		pseudoTF.setTranslateY(64);

		final Button bt = new Button("Connexion");
		bt.setTranslateX(70);
		bt.setTranslateY(96);

		bt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				if (Client.connect(ipTF.getText(), pseudoTF.getText())) {
					GameLoop.start();
				} else {
					// TODO
					// ERROR
				}
			}
		});

		this.getChildren().addAll(ipT, pseudoT, ipTF, pseudoTF, bt);
	}
}
