package kireyis.server;

import java.util.ArrayList;

import kireyis.common.BlockID;
import kireyis.common.Consts;
import kireyis.server.entities.Entity;
import kireyis.server.entities.Player;

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

	public static byte get(final int x, final int y) {
		return world[x][y];
	}

	public static synchronized ArrayList<Entity> getVisibleEntities(final Client client) {
		final Player player = client.getPlayer();

		final ArrayList<Entity> visibles = new ArrayList<Entity>();

		for (final Entity entity : entities) {
			final double viewDistance = client.getViewDistance() + entity.getSize();
			if (entity.getX() < player.getX() + viewDistance && entity.getX() > player.getX() - viewDistance
					&& entity.getY() < player.getY() + viewDistance && entity.getY() > player.getY() - viewDistance
					&& entity != player) {
				visibles.add(entity);
			}
		}

		return visibles;
	}

	public static synchronized void addEntity(final Entity e) {
		entities.add(e);
	}

	public static synchronized void tickEntities() {
		for (final Entity e : entities) {
			e.tick();
		}

		for (int a = 0; a < entities.size(); a++) {
			for (int b = a + 1; b < entities.size(); b++) {
				Entity.collide(entities.get(a), entities.get(b));
			}
		}

		for (final Entity e : entities) {
			e.updateMove();
		}

		for (int n = entities.size() - 1; n >= 0; n--) {
			final Entity e = entities.get(n);

			if (e.isDead()) {
				World.entities.remove(e);
			}
		}
	}
}
