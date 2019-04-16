package kireyis.client;

public class RenderEntity {
	public final double x;
	public final double y;
	public final double rotation;

	public final byte id;

	public RenderEntity(final byte id, final double x, final double y, final double rotation) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		this.id = id;
	}
}
