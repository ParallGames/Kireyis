package kireyis.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import kireyis.client.ui.Key;
import kireyis.client.ui.Window;

public class GameLoop {
	private static final int FPS = 100;
	private static final long INTERVAL = 1_000_000_000L / FPS;

	private static boolean run = false;

	private static ScheduledExecutorService gameLoop;

	public static void start() {
		if (run) {
			throw new RuntimeException("Game loop started twice");
		}
		run = true;

		Window.showConnection(false);
		Window.showGame(true);
		Window.keyFocus();

		gameLoop = Executors.newSingleThreadScheduledExecutor();

		gameLoop.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
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

				Client.flush();

				Window.update();
			}
		}, 0, INTERVAL, TimeUnit.NANOSECONDS);
	}

	public static void stop() {
		if (gameLoop != null) {
			gameLoop.shutdown();
		}

		Window.showConnection(true);
		Window.showGame(false);

		run = false;
	}
}
