package kireyis.server.entities;

import kireyis.common.Consts;

public abstract class Projectile extends Entity {
	protected int age = 0;

	public Projectile(final double x, final double y, final double rotation, final double speedX, final double speedY) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;

		this.speedX = speedX;
		this.speedY = speedY;
	}

	@Override
	public void tick() {
		age++;

		if (age > getLifeTime() || x <= 0 || x + getSize() >= Consts.WORLD_SIZE || y <= 0
				|| y + getSize() >= Consts.WORLD_SIZE) {
			alive = false;
		}
	}

	public abstract int getLifeTime();
}
