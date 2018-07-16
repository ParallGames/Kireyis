package kireyis.client;

import kireyis.common.Consts;

public class Player {
	private static double x = Consts.WORLD_SIZE / 2d;
	private static double y = Consts.WORLD_SIZE / 2d;

	private static int viewDistance = 5;

	private static final double WIDTH = 0.5;
	private static final double HEIGHT = 0.5;

	public static double getX() {
		return x;
	}

	public static double getY() {
		return y;
	}

	public static double getWidth() {
		return WIDTH;
	}

	public static double getHeight() {
		return HEIGHT;
	}

	public static int getViewDistance() {
		return viewDistance;
	}

	public static void increaseViewDistance() {
		viewDistance++;

		if (viewDistance > 50) {
			viewDistance = 50;
		}
	}

	public static void reduceViewDistance() {
		viewDistance--;
		if (viewDistance < 3) {
			viewDistance = 3;
		}
	}
	
	public static void setPos(double x, double y) {
		Player.x = x;
		Player.y = y;
	}
}
