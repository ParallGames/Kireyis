package kireyis.client;

import java.util.ArrayList;

import kireyis.common.BlockID;
import kireyis.common.Consts;
import kireyis.common.Entity;

public class World {
	private static final ArrayList<Entity> entities = new ArrayList<Entity>();

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

	public static ArrayList<Entity> getEntities() {
		return entities;
	}
}
