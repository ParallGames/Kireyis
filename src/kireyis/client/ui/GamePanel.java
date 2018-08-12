package kireyis.client.ui;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import kireyis.client.Client;
import kireyis.client.Player;
import kireyis.client.World;
import kireyis.common.BlockID;
import kireyis.common.Entity;
import kireyis.common.EntityID;

public class GamePanel extends Group {
	private final GraphicsContext gc;
	private final Canvas canvas;

	public GamePanel() {
		canvas = new Canvas();
		this.getChildren().add(canvas);

		gc = canvas.getGraphicsContext2D();

		canvas.setOnScroll(new EventHandler<ScrollEvent>() {
			public void handle(ScrollEvent e) {
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

		// final double playerX = Player.getX();
		// final double playerY = Player.getY();

		final int minX = ((int) camX) - Player.getViewDistance();
		final int minY = ((int) camY) - Player.getViewDistance();

		final int viewSize = Player.getViewDistance() * 2 + 1;
		final double blockSize = (Math.max(Window.getWidth(), Window.getHeight()) / (Player.getViewDistance() * 2d));

		final double translateX = -blockSize * (camX - (int) camX)
				- ((viewSize - 1) * blockSize - Window.getWidth()) / 2;
		final double translateY = -blockSize * (camY - (int) camY)
				- ((viewSize - 1) * blockSize - Window.getHeight()) / 2;

		Platform.runLater(new Runnable() {
			public void run() {
				canvas.setHeight(Window.getHeight());
				canvas.setWidth(Window.getWidth());

				for (int x = 0; x < viewSize; x++) {
					for (int y = 0; y < viewSize; y++) {
						byte block = World.get(x + minX, y + minY);

						if (block == BlockID.UNKNOWN) {
							gc.setFill(Color.PURPLE);
						}
						if (block == BlockID.VOID) {
							gc.setFill(Color.BLACK);
						}
						if (block == BlockID.GRASS) {
							gc.setFill(Color.GREEN);
						}
						gc.fillRect(translateX + blockSize * x, translateY + blockSize * y, blockSize, blockSize);
					}
				}

				for (Entity entity : World.getEntities()) {
					double x = (entity.getX() - camX) * blockSize + Window.getWidth() / 2
							- Player.getWidth() / 2 * blockSize;
					double y = (entity.getY() - camY) * blockSize + Window.getHeight() / 2
							- Player.getHeight() / 2 * blockSize;

					if (entity.getID() == EntityID.PLAYER) {
						gc.setFill(Color.PURPLE);
					}

					gc.fillOval(x, y, Player.getWidth() * blockSize, Player.getHeight() * blockSize);
				}

			}
		});
	}
}
