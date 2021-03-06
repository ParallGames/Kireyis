package kireyis.common;

public class DataID {
	public static final byte INFO = 0;
	/*
	 * String message
	 */

	public static final byte CLIENT_CONNECTION = 2;
	/*
	 * String clientName
	 */

	public static final byte CLIENT_DISCONNECTION = 3;
	/*
	 * String clientName
	 */

	public static final byte CLOSE = 4;

	public static final byte HORIZONTAL_ACCEL = 5;
	/*
	 * 1 to go right 0 to don't move -1 to go left
	 */

	public static final byte VERTICAL_ACCEL = 6;
	/*
	 * 1 to go down 0 to don't move -1 to go up
	 */

	public static final byte PLAYER_POS = 7;
	/*
	 * double x double y
	 */

	public static final byte ENTITIES = 8;
	/*
	 * int numberOfEntities
	 *
	 * for each entity byte id, double x, double y, double rotation
	 */

	public static final byte WORLD = 9;
	/*
	 * for each tile : byte id
	 */

	public static final byte VIEW_DISTANCE = 10;
	/*
	 * int viewDistance
	 */

	public static final byte PLAYER_ROTATION = 11;
	/*
	 * double angle
	 */

	public static final byte LOAD = 12;

	public static final byte THROW = 13;
	/*
	 * double angle
	 */

	public static final byte LOAD_STATE = 14;

	public static final byte PLAYER_MAX_HEALTH = 15;
	/*
	 * int maxHealth
	 */

	public static final byte PLAYER_HEALTH = 16;
	/*
	 * int health
	 */
}
