package kireyis.client;

public class RenderEntity {
	public final double x;
	public final double y;
	public final double rotation;

	public final byte typeid;

	public RenderEntity(final byte typeid, final double x, final double y, final double rotation) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		this.typeid = typeid;
	}
}
