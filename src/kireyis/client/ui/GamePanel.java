package kireyis.client.ui;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import kireyis.client.Client;
import kireyis.client.Player;
import kireyis.client.RenderEntity;
import kireyis.client.World;
import kireyis.common.BlockID;
import kireyis.common.EntityID;

public class GamePanel extends Group {
	private final GraphicsContext gc;
	private final Canvas canvas;

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
	}

	public void update() {
		final double camX = Player.getX();
		final double camY = Player.getY();

		final double playerX = Player.getX();
		final double playerY = Player.getY();

		final ArrayList<RenderEntity> entities = World.getEntities();

		final int minX = ((int) camX) - Player.getViewDistance();
		final int minY = ((int) camY) - Player.getViewDistance();

		final int viewSize = Player.getViewDistance() * 2 + 1;
		final double blockSize = (Math.max(Window.getWidth(), Window.getHeight()) / (Player.getViewDistance() * 2d));

		final double translateX = -blockSize * (camX - (int) camX)
				- ((viewSize - 1) * blockSize - Window.getWidth()) / 2;
		final double translateY = -blockSize * (camY - (int) camY)
				- ((viewSize - 1) * blockSize - Window.getHeight()) / 2;

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				canvas.setHeight(Window.getHeight());
				canvas.setWidth(Window.getWidth());

				for (int x = 0; x < viewSize; x++) {
					for (int y = 0; y < viewSize; y++) {
						final byte block = World.get(x + minX, y + minY);

						if (block == BlockID.UNKNOWN) {
							gc.setFill(Color.PURPLE);
						} else if (block == BlockID.VOID) {
							gc.setFill(Color.BLACK);
						} else if (block == BlockID.GRASS) {
							gc.setFill(Color.GREEN);
						} else {
							gc.setFill(Color.WHITE);
						}
						gc.fillRect(translateX + blockSize * x, translateY + blockSize * y, blockSize, blockSize);
					}
				}

				for (final RenderEntity entity : entities) {
					final double x = (entity.getX() - camX) * blockSize + Window.getWidth() / 2;
					final double y = (entity.getY() - camY) * blockSize + Window.getHeight() / 2;

					if (entity.getTypeid() == EntityID.PLAYER) {
						gc.setFill(Color.PURPLE);
					}

					gc.fillOval(x, y, Player.getWidth() * blockSize, Player.getHeight() * blockSize);
				}

				final double x = (playerX - camX) * blockSize + Window.getWidth() / 2;
				final double y = (playerY - camY) * blockSize + Window.getHeight() / 2;

				gc.setFill(Color.BROWN);
				gc.fillOval(x, y, Player.getWidth() * blockSize, Player.getHeight() * blockSize);
			}
		});
	}
}
