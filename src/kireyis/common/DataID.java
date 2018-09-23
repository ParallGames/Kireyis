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
	/*
	 * Nothing
	 */

	public static final byte PLAYER_MOVE = 5;
	/*
	 * double moveX double moveY
	 */

	public static final byte PLAYER_POS = 6;
	/*
	 * double x double y
	 */

	public static final byte ENTITIES = 7;
	/*
	 * int numberOfEntities
	 *
	 * for each entity byte id, double x, double y, double rotation
	 */

	public static final byte WORLD = 8;
	/*
	 * for each block byte id
	 */

	public static final byte VIEW_DISTANCE = 9;
	/*
	 * int viewDistance
	 */
}
