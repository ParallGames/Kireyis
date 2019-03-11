package kireyis.server.entities;

import kireyis.common.Consts;

public abstract class Entity {
	protected double x = 0;
	protected double y = 0;

	protected double rotation = 0;

	protected double speedX = 0;
	protected double speedY = 0;

	protected boolean alive = true;

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

	public synchronized void accelerate(final double accelX, final double accelY) {
		this.speedX += accelX;
		this.speedY += accelY;
	}

	public synchronized double getX() {
		return x;
	}

	public synchronized double getY() {
		return y;
	}

	public synchronized double getSpeedX() {
		return speedX;
	}

	public synchronized double getSpeedY() {
		return speedY;
	}

	public synchronized double getRotation() {
		return rotation;
	}

	public synchronized void setRotation(final double rotation) {
		this.rotation = rotation;
	}

	public abstract double getSize();

	public abstract byte getID();

	public abstract double getFriction();

	public abstract void tick();

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isDead() {
		return !alive;
	}

	public void collideWith(Entity e) {
		// Do nothing by default
	}

	public static void collide(final Entity e1, final Entity e2) {
		final double e1x = e1.x + e1.getSize() / 2;
		final double e1y = e1.y + e1.getSize() / 2;

		final double e2x = e2.x + e2.getSize() / 2;
		final double e2y = e2.y + e2.getSize() / 2;

		final double distX = e1x - e2x;
		final double distY = e1y - e2y;

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

		e1.collideWith(e2);
		e2.collideWith(e1);
	}
}
