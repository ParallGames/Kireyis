package kireyis.server;

import java.util.ArrayList;

import kireyis.common.BlockID;
import kireyis.common.Consts;

public class World {
	private static final ArrayList<Entity> entities = new ArrayList<Entity>();

	private static final byte world[][] = new byte[Consts.WORLD_SIZE][Consts.WORLD_SIZE];

	static {
		for (int x = 0; x < world.length; x++) {
			for (int y = 0; y < world[0].length; y++) {
				world[x][y] = BlockID.GRASS;
			}
		}
	}

	public static byte get(int x, int y) {
		return world[x][y];
	}

	public static ArrayList<Entity> getEntities() {
		ArrayList<Entity> list = new ArrayList<Entity>();
		list.addAll(entities);
		list.addAll(Server.getPlayerEntities());

		return list;
	}
}
