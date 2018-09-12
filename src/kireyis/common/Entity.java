package kireyis.common;

public class Entity {
	private double x;
	private double y;

	private byte id;

	public Entity(byte id, double x, double y) {
		this.x = x;
		this.y = y;
		this.id = id;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public byte getID() {
		return id;
	}

	public Entity clone() {
		return new Entity(id, x, y);
	}
}
