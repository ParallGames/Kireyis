package kireyis.client;

import java.util.ArrayList;

import kireyis.common.BlockID;
import kireyis.common.Consts;

public class World {
	private static final ArrayList<RenderEntity> entities = new ArrayList<RenderEntity>();

	private static final byte world[][] = new byte[Consts.WORLD_SIZE][Consts.WORLD_SIZE];

	public static byte get(final int x, final int y) {
		if (x < 0 || x >= Consts.WORLD_SIZE || y < 0 || y >= Consts.WORLD_SIZE) {
			return BlockID.VOID;
		}

		return world[x][y];
	}

	public static void set(final int x, final int y, final byte id) {
		world[x][y] = id;
	}

	public static synchronized ArrayList<RenderEntity> getEntities() {
		final ArrayList<RenderEntity> returnedEntities = new ArrayList<RenderEntity>();

		for (final RenderEntity entity : entities) {
			returnedEntities.add(entity);
		}

		return returnedEntities;
	}

	public static synchronized void setEntities(final ArrayList<RenderEntity> newEntities) {
		entities.clear();
		entities.addAll(newEntities);
	}
}
