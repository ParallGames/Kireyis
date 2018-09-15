package kireyis.client;

import java.util.ArrayList;

import kireyis.common.BlockID;
import kireyis.common.Consts;

public class World {
	private static final ArrayList<RenderEntity> entities = new ArrayList<RenderEntity>();

	private static final byte world[][] = new byte[Consts.WORLD_SIZE][Consts.WORLD_SIZE];

	public static void reset() {
		for (int x = 0; x < Consts.WORLD_SIZE; x++) {
			for (int y = 0; y < Consts.WORLD_SIZE; y++) {
				world[x][y] = BlockID.UNKNOWN;
			}
		}
		entities.clear();
	}

	public static byte get(int x, int y) {
		if (x < 0 || x >= Consts.WORLD_SIZE || y < 0 || y >= Consts.WORLD_SIZE) {
			return BlockID.VOID;
		}

		return world[x][y];
	}

	public static void set(int x, int y, byte id) {
		world[x][y] = id;
	}

	public static synchronized ArrayList<RenderEntity> getEntities() {
		ArrayList<RenderEntity> returnedEntities = new ArrayList<RenderEntity>();

		for (RenderEntity entity : entities) {
			returnedEntities.add(entity);
		}

		return returnedEntities;
	}

	public static synchronized void setEntities(ArrayList<RenderEntity> newEntities) {
		entities.clear();
		entities.addAll(newEntities);
	}
}
