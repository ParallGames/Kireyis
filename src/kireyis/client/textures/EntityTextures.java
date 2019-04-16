package kireyis.client.textures;

import javafx.scene.image.Image;
import kireyis.common.EntityID;

public class EntityTextures {
	private static final String TEXTURES_PATH = "/resources/textures/";

	private static Image playerTexture;
	private static Image arrowTexture;

	public static void loadTextures() {
		playerTexture = new Image(EntityTextures.class.getResourceAsStream(TEXTURES_PATH + "player.png"), 64, 64, true,
				true);
		arrowTexture = new Image(EntityTextures.class.getResourceAsStream(TEXTURES_PATH + "arrow.png"), 64, 64, true,
				true);
	}

	public static Image getTextureFromID(final int id) {
		switch (id) {
		case EntityID.PLAYER:
			return playerTexture;
		case EntityID.ARROW:
			return arrowTexture;
		}
		throw new RuntimeException("Unknown entity id");
	}

	public static Image getPlayerTexture() {
		return playerTexture;
	}

	public static Image getArrowTexture() {
		return arrowTexture;
	}
}
