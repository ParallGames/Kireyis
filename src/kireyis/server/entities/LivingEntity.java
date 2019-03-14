package kireyis.server.entities;

public abstract class LivingEntity extends Entity {
	protected int hp = getMaxHP();

	public int getHP() {
		return hp;
	}

	public void damage(final int damagePoints) {
		hp -= damagePoints;

		if (hp <= 0) {
			hp = 0;
			alive = false;
		}
	}

	public void heal(final int healPoints) {
		hp += healPoints;

		if (hp > getMaxHP()) {
			hp = getMaxHP();
		}
	}

	public abstract int getMaxHP();
}
