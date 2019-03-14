package kireyis.server.entities;

import kireyis.common.entityModels.EntityModels;

public class Player extends LivingEntity {
	private static final double ACCELERATION = 0.002;

	private byte horizontalAccel = 0;
	private byte verticalAccel = 0;

	public Player(final double x, final double y) {
		this.rotation = 0;
		this.x = x;
		this.y = y;
	}

	public void setHorizontalAccel(final byte accel) {
		horizontalAccel = accel;
	}

	public void setVerticalAccel(final byte accel) {
		verticalAccel = accel;
	}

	@Override
	public byte getID() {
		return EntityModels.PLAYER.getID();
	}

	@Override
	public double getSize() {
		return EntityModels.PLAYER.getSize();
	}

	@Override
	public double getFriction() {
		return 0.9;
	}

	@Override
	public void tick() {
		double accelX;
		if (horizontalAccel == 1) {
			accelX = ACCELERATION;
		} else if (horizontalAccel == -1) {
			accelX = -ACCELERATION;
		} else {
			accelX = 0;
		}

		double accelY;
		if (verticalAccel == 1) {
			accelY = ACCELERATION;
		} else if (verticalAccel == -1) {
			accelY = -ACCELERATION;
		} else {
			accelY = 0;
		}

		this.accelerate(accelX, accelY);
	}

	@Override
	public int getMaxHP() {
		return 10;
	}
}
