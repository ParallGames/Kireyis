package kireyis.client;

import java.util.ArrayList;
import java.util.List;

import kireyis.common.BlockID;
import kireyis.common.Consts;

public class World {
	private static final ArrayList<RenderEntity> entities = new ArrayList<>();

	private static final byte[][] world = new byte[Consts.WORLD_SIZE][Consts.WORLD_SIZE];

	public static byte get(final int x, final int y) {
		if (x < 0 || x >= Consts.WORLD_SIZE || y < 0 || y >= Consts.WORLD_SIZE) {
			return BlockID.VOID;
		}

		return world[x][y];
	}

	public static void set(final int x, final int y, final byte id) {
		world[x][y] = id;
	}

	public static synchronized List<RenderEntity> getEntities() {
		return new ArrayList<>(entities);
	}

	public static synchronized void setEntities(final List<RenderEntity> newEntities) {
		entities.clear();
		entities.addAll(newEntities);
	}
}
