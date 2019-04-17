package kireyis.server;

import java.util.ArrayList;
import java.util.List;

import kireyis.common.Consts;
import kireyis.common.TileID;
import kireyis.server.entities.Entity;
import kireyis.server.entities.Player;
import kireyis.server.entities.Zombie;

public class World {
	private static final ArrayList<Entity> entities = new ArrayList<>();

	private static final byte[][] world = new byte[Consts.WORLD_SIZE][Consts.WORLD_SIZE];

	static {
		for (int x = 0; x < world.length; x++) {
			for (int y = 0; y < world[0].length; y++) {
				world[x][y] = TileID.GRASS;
			}
		}
	}

	public static byte getTile(final int x, final int y) {
		return world[x][y];
	}

	public static synchronized List<Entity> getVisibleEntities(final Client client) {
		final Player player = client.getPlayer();

		final ArrayList<Entity> visibles = new ArrayList<>();

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

	public static synchronized int enemiesCount() {
		int count = 0;

		for (final Entity e : entities) {
			if (e instanceof Zombie) {
				count++;
			}
		}

		return count;
	}

	public static synchronized Player getNearestPlayer(final double x, final double y) {
		double distance = Double.POSITIVE_INFINITY;
		Player nearest = null;

		for (final Entity entity : entities) {
			if (entity instanceof Player) {
				final double distX = x - entity.getX();
				final double distY = y - entity.getY();

				final double dist = distX * distX + distY * distY;

				if (dist < distance) {
					distance = dist;
					nearest = (Player) entity;
				}
			}
		}

		return nearest;
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
			if (entities.get(n).isDead()) {
				World.entities.remove(n);
			}
		}
	}
}
