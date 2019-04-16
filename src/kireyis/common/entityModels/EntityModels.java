package kireyis.common.entityModels;

import kireyis.common.EntityID;

public class EntityModels {
	public static final PlayerModel PLAYER = new PlayerModel();
	public static final ArrowModel ARROW = new ArrowModel();

	public static EntityModel getModelFromID(final byte id) {
		switch (id) {
		case EntityID.PLAYER:
			return PLAYER;
		case EntityID.ARROW:
			return ARROW;
		}
		throw new RuntimeException("Unknown id");
	}
}
