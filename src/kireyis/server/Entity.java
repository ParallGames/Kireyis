package kireyis.server;

public class Entity {
	private double x;
	private double y;

	private byte typeid;
	private int id;

	public Entity(byte typeid, double x, double y, int id) {
		this.x = x;
		this.y = y;
		this.typeid = typeid;
		this.id = id;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getID() {
		return id;
	}

	public int getTypeid() {
		return typeid;
	}
}
