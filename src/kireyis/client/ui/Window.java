package kireyis.client.ui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Window extends Application {
	private static final Group root = new Group();
	private static final Scene scene = new Scene(root, 1280, 720);
	private static Stage primaryStage;

	private static final ConnectionPanel connectionPanel = new ConnectionPanel();
	private static final GamePanel gamePanel = new GamePanel();
	private static final PopupPanel popupPanel = new PopupPanel();

	private static final Key key = new Key();

	@Override
	public void start(final Stage primaryStage) {
		Window.primaryStage = primaryStage;

		primaryStage.setFullScreenExitHint("");
		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

		scene.setFill(Color.rgb(191, 191, 191));

		gamePanel.setVisible(false);
		popupPanel.setVisible(false);

		root.getChildren().addAll(connectionPanel, gamePanel, popupPanel, key);

		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.setTitle("Kireyis");

		primaryStage.show();
	}

	public static double getHeight() {
		return scene.getHeight();
	}

	public static double getWidth() {
		return scene.getWidth();
	}

	public static void popupPanel() {
		popupPanel.setVisible(!popupPanel.isVisible());
	}

	public static void showGame(final boolean show) {
		gamePanel.setVisible(show);
	}

	public static void showConnection(final boolean show) {
		if (show) {
			popupPanel.setVisible(false);
		}
		connectionPanel.setVisible(show);
	}

	public static void update() {
		gamePanel.update();
		popupPanel.update();
	}

	public static void keyFocus() {
		key.requestFocus();
	}

	public static void switchFullScreen() {
		primaryStage.setFullScreen(!primaryStage.isFullScreen());
	}
}