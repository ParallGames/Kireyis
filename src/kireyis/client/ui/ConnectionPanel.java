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

		final Text nicknameT = new Text("Nickname : ");
		nicknameT.setTranslateX(10);
		nicknameT.setTranslateY(78);

		final TextField ipTF = new TextField();
		ipTF.setTranslateX(70);
		ipTF.setTranslateY(32);

		final TextField nicknameTF = new TextField();
		nicknameTF.setTranslateX(70);
		nicknameTF.setTranslateY(64);

		final Button bt = new Button("Connexion");
		bt.setTranslateX(70);
		bt.setTranslateY(96);

		bt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				if (Client.connect(ipTF.getText(), nicknameTF.getText())) {
					GameLoop.start();
				} else {
					// TODO
					// ERROR
				}
			}
		});

		this.getChildren().addAll(ipT, nicknameT, ipTF, nicknameTF, bt);
	}
}
