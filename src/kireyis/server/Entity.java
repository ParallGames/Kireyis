package kireyis.server;

public abstract class Entity {
	protected double x;
	protected double y;

	protected int id;

	/*
	 * public Entity(final byte typeID, final double x, final double y, final int
	 * id) { this.x = x; this.y = y; this.typeID = typeID; this.id = id; }
	 */

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getID() {
		return id;
	}

	public abstract int getTypeID();
}
