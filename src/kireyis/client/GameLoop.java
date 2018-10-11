package kireyis.client;

import kireyis.client.ui.Key;
import kireyis.client.ui.Window;

public class GameLoop {

	private static boolean run = false;

	public static void start() {
		if (run) {
			throw new RuntimeException("Game loop started twice");
		}
		run = true;

		Window.showConnection(false);
		Window.showGame(true);
		Window.keyFocus();

		new Thread() {
			@Override
			public void run() {
				while (run) {
					byte moveX = 0;
					if (Key.isLeftDown()) {
						moveX--;
					}
					if (Key.isRightDown()) {
						moveX++;
					}
					Client.sendHorizontalAccel(moveX);

					byte moveY = 0;
					if (Key.isUpDown()) {
						moveY--;
					}
					if (Key.isDownDown()) {
						moveY++;
					}
					Client.sendVerticalAccel(moveY);

					Window.update();

					try {
						Thread.sleep(10);
					} catch (final InterruptedException e) {
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
