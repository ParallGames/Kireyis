package kireyis.server.entities;

import kireyis.common.EntityID;
import kireyis.common.entityModels.EntityModels;
import kireyis.server.World;

public class Zombie extends LivingEntity {
	private static final double ACCELERATION = 0.001;

	private int attackTimer = 0;

	public Zombie(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int getMaxHP() {
		return 5;
	}

	@Override
	public double getSize() {
		return EntityModels.ZOMBIE.getSize();
	}

	@Override
	public byte getID() {
		return EntityID.ZOMBIE;
	}

	@Override
	public double getFriction() {
		return 0.9;
	}

	@Override
	public void tick() {
		final Player player = World.getNearestPlayer(x, y);

		if (player == null) {
			return;
		}

		final double distX = player.x - x;
		final double distY = player.y - y;

		final double angle = Math.atan2(distY, distX);

		this.rotation = angle + Math.PI / 2;

		this.speedX += Math.cos(angle) * ACCELERATION;

		this.speedY += Math.sin(angle) * ACCELERATION;

		if (attackTimer > 0) {
			attackTimer--;
		}
	}

	@Override
	public void collideWith(final Entity e) {
		if (attackTimer == 0 && e instanceof Player) {
			((Player) e).damage(1);
			attackTimer = 100;
		}
	}
}
