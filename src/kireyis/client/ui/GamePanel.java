package kireyis.client.ui;

import java.util.List;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import kireyis.client.Client;
import kireyis.client.Player;
import kireyis.client.RenderEntity;
import kireyis.client.World;
import kireyis.client.textures.EntityTextures;
import kireyis.client.textures.TileTextures;
import kireyis.common.entityModels.EntityModels;

public class GamePanel extends Group {
	private final GraphicsContext gc;
	private final Canvas canvas;

	private void drawRotatedImage(final Image image, final double angle, final double x, final double y,
			final double width, final double height) {

		final Rotate r = new Rotate(angle, x + width / 2, y + height / 2);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
		gc.drawImage(image, x, y, width, height);
		gc.setTransform(1, 0, 0, 1, 0, 0);
	}

	public GamePanel() {
		canvas = new Canvas();
		this.getChildren().add(canvas);

		gc = canvas.getGraphicsContext2D();

		canvas.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(final ScrollEvent e) {
				if (e.getDeltaY() > 0) {
					Player.reduceViewDistance();
				} else {
					Player.increaseViewDistance();
				}
				Client.sendViewDistance();
			}
		});

		final EventHandler<MouseEvent> mouseMove = new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent e) {
				final double x = e.getX() - canvas.getWidth() / 2;
				final double y = e.getY() - canvas.getHeight() / 2;

				final double angle = Math.atan2(y, x);

				Player.setRotation(angle + Math.PI / 2);
			}
		};

		canvas.setOnMouseMoved(mouseMove);
		canvas.setOnMouseDragged(mouseMove);

		canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent e) {
				if (e.getButton() == MouseButton.PRIMARY) {
					Client.sendPlayerLoad();
				}
			}
		});

		canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent e) {
				if (e.getButton() == MouseButton.PRIMARY) {
					Client.sendPlayerThrow();
				}
			}
		});
	}

	public void update() {
		final double playerSize = EntityModels.PLAYER.getSize();

		final double camX = Player.getX() + playerSize / 2;
		final double camY = Player.getY() + playerSize / 2;

		final double playerX = Player.getX();
		final double playerY = Player.getY();

		final List<RenderEntity> entities = World.getEntities();

		final int minX = ((int) camX) - Player.getViewDistance();
		final int minY = ((int) camY) - Player.getViewDistance();

		final int viewSize = Player.getViewDistance() * 2;
		final double blockSize = Math.max(Window.getWidth(), Window.getHeight()) / (Player.getViewDistance() * 2);

		final double translateX = -blockSize * (camX - (int) camX) - (viewSize * blockSize - Window.getWidth()) / 2;
		final double translateY = -blockSize * (camY - (int) camY) - (viewSize * blockSize - Window.getHeight()) / 2;

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				canvas.setHeight(Window.getHeight());
				canvas.setWidth(Window.getWidth());

				for (int x = 0; x <= viewSize; x++) {
					for (int y = 0; y <= viewSize; y++) {
						final byte tile = World.get(x + minX, y + minY);

						final Image texture = TileTextures.getTextureFromID(tile);

						gc.drawImage(texture, translateX + blockSize * x, translateY + blockSize * y, blockSize,
								blockSize);
					}
				}

				for (final RenderEntity entity : entities) {
					final double x = (entity.x - camX) * blockSize + Window.getWidth() / 2;
					final double y = (entity.y - camY) * blockSize + Window.getHeight() / 2;

					final Image texture = EntityTextures.getTextureFromID(entity.id);
					final double size = EntityModels.getModelFromID(entity.id).getSize();

					drawRotatedImage(texture, entity.rotation * 180 / Math.PI, x, y, size * blockSize,
							size * blockSize);
				}

				{
					final double x = (playerX - camX) * blockSize + Window.getWidth() / 2;
					final double y = (playerY - camY) * blockSize + Window.getHeight() / 2;

					drawRotatedImage(EntityTextures.getPlayerTexture(), Player.getRotation() * 180 / Math.PI, x, y,
							playerSize * blockSize, playerSize * blockSize);
				}

				if (Player.getMaxHealth() > 0) {
					final double x = Window.getWidth() / 2 - 100;
					final double y = Window.getHeight() - 60;

					final double sizeFactor = 200D / Player.getMaxHealth();

					gc.setFill(Color.DARKRED);
					gc.fillRect(x, y, 200, 50);
					gc.setFill(Color.RED);
					gc.fillRect(x, y, Player.getHealth() * sizeFactor, 50);

					gc.setFill(Color.BLACK);
					gc.setFont(new Font("Noto Mono", 32));
					gc.setTextAlign(TextAlignment.CENTER);
					gc.fillText(Player.getHealth() + "/" + Player.getMaxHealth(), Window.getWidth() / 2,
							Window.getHeight() - 24);

				}

				if (Player.getLoad() > 0) {
					gc.setFill(Color.DARKBLUE);
					gc.fillRect(50, Window.getHeight() - 150, 50, 100);
					gc.setFill(Color.rgb(63, 63, 255));
					gc.fillRect(50, Window.getHeight() - Player.getLoad() * 100 - 50, 50, 100 * Player.getLoad());
				}
			}
		});
	}
}
