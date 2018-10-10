package kireyis.server;

import kireyis.common.Consts;

public abstract class Entity {
	protected double x;
	protected double y;

	private double moveX;
	private double moveY;

	public synchronized void updateMove() {
		x += moveX;
		moveX = 0;

		if (x < 0) {
			x = 0;
		} else if (x + this.getSize() > Consts.WORLD_SIZE) {
			x = Consts.WORLD_SIZE - this.getSize();
		}

		y += moveY;
		moveY = 0;

		if (y < 0) {
			y = 0;
		} else if (y + this.getSize() > Consts.WORLD_SIZE) {
			y = Consts.WORLD_SIZE - this.getSize();
		}
	}

	protected synchronized void move(final double moveX, final double moveY) {
		this.moveX += moveX;
		this.moveY += moveY;
	}

	public synchronized double getX() {
		return x;
	}

	public synchronized double getY() {
		return y;
	}

	public abstract double getSize();

	public abstract int getTypeID();

	public static void collide(final Entity e1, final Entity e2) {
		double distX = e1.x - e2.x;
		double distY = e1.y - e2.y;

		double avgSize = (e1.getSize() + e2.getSize()) / 2;

		if (distX * distX + distY * distY > avgSize * avgSize) {
			return;
		}

		double angle = Math.atan2(distX, distY);
		double moveSize = Math.abs(avgSize) - Math.sqrt(distX * distX + distY * distY);

		e1.move(Math.sin(angle) * moveSize, Math.cos(angle) * moveSize);

		angle += Math.PI;

		e2.move(Math.sin(angle) * moveSize, Math.cos(angle) * moveSize);
	}
}
