package kireyis.server;

public class Entity {
	protected double x;
	protected double y;
	protected byte id;
	
	public Entity(double x, double y, byte id) {
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
}
