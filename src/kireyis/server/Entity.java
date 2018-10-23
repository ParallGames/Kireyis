package kireyis.server;

import kireyis.common.Consts;

public abstract class Entity {
	protected double x;
	protected double y;

	private double speedX;
	private double speedY;

	public synchronized void updateMove() {
		x += speedX;
		speedX *= getFriction();

		if (x < 0) {
			x = 0;
		} else if (x + this.getSize() > Consts.WORLD_SIZE) {
			x = Consts.WORLD_SIZE - this.getSize();
		}

		y += speedY;
		speedY *= getFriction();

		if (y < 0) {
			y = 0;
		} else if (y + this.getSize() > Consts.WORLD_SIZE) {
			y = Consts.WORLD_SIZE - this.getSize();
		}
	}

	protected synchronized void accelerate(final double accelX, final double accelY) {
		this.speedX += accelX;
		this.speedY += accelY;
	}

	public synchronized double getX() {
		return x;
	}

	public synchronized double getY() {
		return y;
	}

	public abstract double getSize();

	public abstract int getTypeID();

	public abstract double getFriction();

	public abstract double getAcceleration();

	public abstract void tick();

	public static void collide(final Entity e1, final Entity e2) {
		final double distX = e1.x - e2.x;
		final double distY = e1.y - e2.y;

		final double avgSize = (e1.getSize() + e2.getSize()) / 2;

		if (distX * distX + distY * distY > avgSize * avgSize) {
			return;
		}

		double moveSize = avgSize - Math.sqrt(distX * distX + distY * distY);
		moveSize /= 32;

		double angle = Math.atan2(distX, distY);
		e1.accelerate(Math.sin(angle) * moveSize, Math.cos(angle) * moveSize);

		angle += Math.PI;
		e2.accelerate(Math.sin(angle) * moveSize, Math.cos(angle) * moveSize);
	}
}
