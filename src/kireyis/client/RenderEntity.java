package kireyis.client;

public class RenderEntity {
	private double x;
	private double y;

	private byte typeid;

	public RenderEntity(byte typeid, double x, double y) {
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
