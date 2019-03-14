package kireyis.server.entities;

import kireyis.common.entityModels.EntityModels;

public class Arrow extends Projectile {
	private static final double INITIAL_SPEED = 0.05;

	public Arrow(final double x, final double y, final double rotation, final double throwerSpeedX,
			final double throwerSpeedY) {
		super(x + Math.cos(rotation - Math.PI / 2) * 0.4, y + Math.sin(rotation - Math.PI / 2) * 0.4, rotation,
				throwerSpeedX + Math.cos(rotation - Math.PI / 2) * INITIAL_SPEED,
				throwerSpeedY + Math.sin(rotation - Math.PI / 2) * INITIAL_SPEED);
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
	public int getLifeTime() {
		return 200;
	}

	@Override
	public void collideWith(final Entity e) {
		if (e instanceof LivingEntity) {
			((LivingEntity) e).damage(1);
			alive = false;
		}
	}
}
