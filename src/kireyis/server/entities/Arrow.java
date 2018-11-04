package kireyis.server.entities;

import kireyis.common.Consts;
import kireyis.common.EntityID;

public class Arrow extends Entity {
	private int age = 0;

	private boolean alive = true;

	public Arrow(final double x, final double y, final double rotation, final double throwerSpeedX,
			final double throwerSpeedY) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;

		this.speedX = throwerSpeedX + Math.cos(rotation - Math.PI / 2) * 0.05;
		this.speedY = throwerSpeedY + Math.sin(rotation - Math.PI / 2) * 0.05;
	}

	@Override
	public double getSize() {
		return 0.25;
	}

	@Override
	public int getTypeID() {
		return EntityID.ARROW;
	}

	@Override
	public double getFriction() {
		return 1;
	}

	@Override
	public void tick() {
		age++;

		if (age > 200) {
			alive = false;
		}

		if (x <= 0) {
			alive = false;
		} else if (x + this.getSize() >= Consts.WORLD_SIZE) {
			alive = false;
		}

		if (y <= 0) {
			alive = false;
		} else if (y + this.getSize() >= Consts.WORLD_SIZE) {
			alive = false;
		}
	}

	@Override
	public boolean isDead() {
		return !alive;
	}
}
