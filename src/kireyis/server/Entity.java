package kireyis.server;

public class Entity {
	private final double x;
	private final double y;

	private final byte typeid;
	private final int id;

	public Entity(final byte typeid, final double x, final double y, final int id) {
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
