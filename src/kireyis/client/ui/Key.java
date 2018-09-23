package kireyis.client.ui;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Key extends Group {
	private static final KeyCode QUIT = KeyCode.ESCAPE;

	private static final KeyCode UP = KeyCode.W;
	private static final KeyCode DOWN = KeyCode.S;
	private static final KeyCode LEFT = KeyCode.A;
	private static final KeyCode RIGHT = KeyCode.D;

	private static boolean quitDown = false;

	private static boolean upDown = false;
	private static boolean downDown = false;
	private static boolean leftDown = false;
	private static boolean rightDown = false;

	public Key() {
		this.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(final KeyEvent e) {
				final KeyCode code = e.getCode();

				if (code == QUIT) {
					if (!quitDown) {
						Window.popupPanel();
						quitDown = true;
					}
				} else if (code == UP) {
					upDown = true;
				} else if (code == DOWN) {
					downDown = true;
				} else if (code == LEFT) {
					leftDown = true;
				} else if (code == RIGHT) {
					rightDown = true;
				}
			}
		});

		this.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(final KeyEvent e) {
				final KeyCode code = e.getCode();

				if (code == QUIT) {
					quitDown = false;
				} else if (code == UP) {
					upDown = false;
				} else if (code == DOWN) {
					downDown = false;
				} else if (code == LEFT) {
					leftDown = false;
				} else if (code == RIGHT) {
					rightDown = false;
				}
			}
		});
	}

	public static boolean isUpDown() {
		return upDown;
	}

	public static boolean isDownDown() {
		return downDown;
	}

	public static boolean isLeftDown() {
		return leftDown;
	}

	public static boolean isRightDown() {
		return rightDown;
	}
}
