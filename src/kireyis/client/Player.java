package kireyis.client;

import kireyis.common.Consts;

public class Player {
	private static double x;
	private static double y;
	private static double rotation = 0;

	private static int viewDistance = Consts.DEFAULT_VIEW;

	private static final double WIDTH = 0.5;
	private static final double HEIGHT = 0.5;

	public static double getX() {
		return x;
	}

	public static double getY() {
		return y;
	}

	public static double getRotation() {
		return rotation;
	}

	public static void setRotation(final double rotation) {
		Player.rotation = rotation;
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

	public static void setViewDistance(final int dist) {
		viewDistance = dist;
	}

	public static void increaseViewDistance() {
		viewDistance++;

		if (viewDistance > Consts.MAX_VIEW) {
			viewDistance = Consts.MAX_VIEW;
		}
	}

	public static void reduceViewDistance() {
		viewDistance--;
		if (viewDistance < Consts.MIN_VIEW) {
			viewDistance = Consts.MIN_VIEW;
		}
	}

	public static void setPos(final double x, final double y) {
		Player.x = x;
		Player.y = y;
	}
}
