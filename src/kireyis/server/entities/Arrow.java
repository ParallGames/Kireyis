package kireyis.server.entities;

import kireyis.common.Consts;
import kireyis.common.entityModels.EntityModels;

public class Arrow extends Entity {
	private static final double INITIAL_SPEED = 0.05;

	private int age = 0;

	public Arrow(final double x, final double y, final double rotation, final double throwerSpeedX,
			final double throwerSpeedY) {
		this.x = x + Math.cos(rotation - Math.PI / 2) * 0.4;
		this.y = y + Math.sin(rotation - Math.PI / 2) * 0.4;
		this.rotation = rotation;

		this.speedX = throwerSpeedX + Math.cos(rotation - Math.PI / 2) * INITIAL_SPEED;
		this.speedY = throwerSpeedY + Math.sin(rotation - Math.PI / 2) * INITIAL_SPEED;
	}

	@Override
	public double getSize() {
		return EntityModels.ARROW.getSize();
	}

	@Override
	public byte getID() {
		return EntityModels.ARROW.getID();
	}

	@Override
	public double getFriction() {
		return 1;
	}

	@Override
	public void tick() {
		age++;

		if (age > 200 || x <= 0 || x + this.getSize() >= Consts.WORLD_SIZE || y <= 0
				|| y + this.getSize() >= Consts.WORLD_SIZE) {
			alive = false;
		}
	}

	@Override
	public void collideWith(Entity e) {
		alive = false;
	}
}
