package kireyis.client;

import kireyis.client.ui.Key;
import kireyis.client.ui.Window;

public class GameLoop {
	private static final int FPS = 100;
	private static final long INTERVAL = 1_000_000_000L / FPS;
	private static long time = System.nanoTime();

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

					Client.sendPlayerRotation();

					Window.update();

					final long sleep = time - System.nanoTime() + INTERVAL;
					if (sleep > 0) {
						try {
							Thread.sleep(sleep / 1_000_000L, (int) (sleep % 1_000_000L));
						} catch (final InterruptedException e) {
							e.printStackTrace();
						}
					}
					time = System.nanoTime();
				}
			}
		}.start();
	}

	public static void stop() {
		Window.showConnection(true);
		Window.showGame(false);

		run = false;
	}
}
