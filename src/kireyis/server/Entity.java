package kireyis.server;

public abstract class Entity {
	protected double x;
	protected double y;

	protected int id;

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
