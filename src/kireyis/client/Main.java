package kireyis.client;

import javafx.application.Application;
import kireyis.client.textures.EntityTextures;
import kireyis.client.textures.TileTextures;
import kireyis.client.ui.Window;

public class Main {
	public static void main(final String[] args) {
		EntityTextures.loadTextures();
		TileTextures.loadTextures();

		Application.launch(Window.class);

		Client.disconnect();
	}
}