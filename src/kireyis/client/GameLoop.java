package kireyis.client;

import kireyis.client.ui.Key;
import kireyis.client.ui.Window;

public class GameLoop {

	private static boolean run = false;

	public static void start() {
		// World.reset();

		if (run) {
			throw new RuntimeException("Game loop started twice");
		}
		run = true;

		Window.showConnection(false);
		Window.showGame(true);
		Window.keyFocus();

		new Thread() {
			public void run() {
				while (run) {
					final double speed = 0.02;

					double moveX = 0;
					double moveY = 0;

					if (Key.isUpDown()) {
						moveY -= speed;
					}
					if (Key.isDownDown()) {
						moveY += speed;
					}
					if (Key.isLeftDown()) {
						moveX -= speed;
					}
					if (Key.isRightDown()) {
						moveX += speed;
					}

					Client.sendMove(moveX, moveY);

					Window.update();

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		}.start();
	}

	public static void stop() {
		run = false;
		Window.showConnection(true);
		Window.showGame(false);
	}
}
