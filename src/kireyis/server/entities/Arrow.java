package kireyis.server.entities;

import kireyis.common.Consts;
import kireyis.common.entityModels.EntityModels;

public class Arrow extends Entity {
	private int age = 0;

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
		return EntityModels.ARROW.getID();
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

		if (x <= 0 || x + this.getSize() >= Consts.WORLD_SIZE || y <= 0 || y + this.getSize() >= Consts.WORLD_SIZE) {
			alive = false;
		}
	}

	@Override
	public boolean isDead() {
		return !alive;
	}
}
