package kireyis.client;

import kireyis.common.Consts;

public class Player {
	private static double x;
	private static double y;
	private static double rotation = 0;

	private static int health;
	private static int maxHealth;

	private static int viewDistance = Consts.DEFAULT_VIEW;

	private static double load = 0;

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

	public static int getHealth() {
		return health;
	}

	public static int getMaxHealth() {
		return maxHealth;
	}

	public static void setHealth(final int health) {
		Player.health = health;
	}

	public static void setMaxHealth(final int maxHealth) {
		Player.maxHealth = maxHealth;
	}

	public static void setLoad(final double load) {
		Player.load = load;
	}

	public static double getLoad() {
		return load;
	}
}
