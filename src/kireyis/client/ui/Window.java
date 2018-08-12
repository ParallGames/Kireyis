package kireyis.client.ui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Window extends Application {
	private static final Group root = new Group();
	private static final Scene scene = new Scene(root, 1280, 720);

	private static final ConnectionPanel connectionPanel = new ConnectionPanel();
	private static final GamePanel gamePanel = new GamePanel();
	private static final PopupPanel popupPanel = new PopupPanel();

	private static final Key key = new Key();

	@Override
	public void start(Stage primaryStage) {
		scene.setFill(Color.rgb(191, 191, 191));

		gamePanel.setVisible(false);
		popupPanel.setVisible(false);

		root.getChildren().addAll(connectionPanel, gamePanel, popupPanel);

		root.getChildren().add(key);

		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.setTitle("Kireyis");
		primaryStage.show();

		// GameLoop.start();
	}

	public static int getHeight() {
		return (int) scene.getHeight();
	}

	public static int getWidth() {
		return (int) scene.getWidth();
	}

	public static void popupPanel() {
		popupPanel.setVisible(!popupPanel.isVisible());
	}

	public static void showGame(boolean show) {
		gamePanel.setVisible(show);
	}

	public static void showConnection(boolean show) {
		if (show) {
			popupPanel.setVisible(false);
		}
		connectionPanel.setVisible(show);
	}

	public static void update() {
		gamePanel.update();
	}

	public static void keyFocus() {
		key.requestFocus();
	}
}