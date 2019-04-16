package kireyis.client.textures;

import javafx.scene.image.Image;
import kireyis.common.TileID;

public class TileTextures {
	private static final String TEXTURES_PATH = "/resources/textures/tiles/";

	private static Image unknownTexture;
	private static Image voidTexture;
	private static Image grassTexture;

	public static void loadTextures() {
		unknownTexture = new Image(EntityTextures.class.getResourceAsStream(TEXTURES_PATH + "unknown.png"), 128, 128,
				true, true);
		voidTexture = new Image(EntityTextures.class.getResourceAsStream(TEXTURES_PATH + "void.png"), 128, 128, true,
				true);
		grassTexture = new Image(EntityTextures.class.getResourceAsStream(TEXTURES_PATH + "grass.png"), 128, 128, true,
				true);
	}

	public static Image getTextureFromID(final byte id) {
		switch (id) {
		case TileID.UNKNOWN:
			return unknownTexture;
		case TileID.VOID:
			return voidTexture;
		case TileID.GRASS:
			return grassTexture;
		}
		throw new RuntimeException("Unknown tile id");
	}
}
