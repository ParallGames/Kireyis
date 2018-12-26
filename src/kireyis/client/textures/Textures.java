package kireyis.client.textures;

import javafx.scene.image.Image;

public class Textures {
	private static final String TEXTURES_PATH = "/resources/textures/";

	private static Image playerTexture;
	private static Image arrowTexture;

	public static void loadTextures() {
		playerTexture = new Image(Textures.class.getResourceAsStream(TEXTURES_PATH + "player.png"), 64, 64, true, true);
		arrowTexture = new Image(Textures.class.getResourceAsStream(TEXTURES_PATH + "arrow.png"), 64, 64, true, true);
	}

	public static Image getPlayerTexture() {
		return playerTexture;
	}

	public static Image getArrowTexture() {
		return arrowTexture;
	}
}
