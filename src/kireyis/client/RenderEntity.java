package kireyis.client;

public class RenderEntity {
	private final double x;
	private final double y;

	private final byte typeid;

	public RenderEntity(final byte typeid, final double x, final double y) {
		this.x = x;
		this.y = y;
		this.typeid = typeid;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getTypeid() {
		return typeid;
	}
}
