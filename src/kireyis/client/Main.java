package kireyis.client;

import javafx.application.Application;
import kireyis.client.ui.Window;

public class Main {
	public static void main(final String[] args) {
		Application.launch(Window.class);
		GameLoop.stop();

		Client.sendClose();
		Client.close();
	}
}
