package kireyis.client.textures;

import javafx.scene.image.Image;

public class Textures {
	private static final String TEXTURES_PATH = "/resources/images/";

	private static Image playerTexture;

	public static void loadTextures() {
		playerTexture = new Image(Textures.class.getResourceAsStream(TEXTURES_PATH + "player.png"));
	}

	public static Image getPlayerTexture() {
		return playerTexture;
	}
}
