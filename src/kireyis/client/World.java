package kireyis.client;

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
		
		world[0][0] = BlockID.VOID;
	}

	public static byte get(int x, int y) {
		while (x < 0) {
			x += Consts.WORLD_SIZE;
		} 
		
		while (x >= Consts.WORLD_SIZE) {
			x -= Consts.WORLD_SIZE;
		}
		
		while (y < 0) {
			y += Consts.WORLD_SIZE;
		}
		
		while (y >= Consts.WORLD_SIZE) {
			y -= Consts.WORLD_SIZE;
		}
		
		if (x < 0 || x >= world.length || y < 0 || y >= world[0].length) {
			return BlockID.UNKNOWN;
		}

		return world[x][y];
	}

	public static void set(int x, int y, byte id) {
		
	}
	
	public static ArrayList<Entity> getEntities() {
		return entities;
	}
}
