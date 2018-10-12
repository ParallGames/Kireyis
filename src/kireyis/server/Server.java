package kireyis.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Vector;

import kireyis.common.Consts;

public class Server {
	private static final int tps = 100;
	private static final long interval = 1_000_000_000L / tps;
	private static long time = System.nanoTime();

	public static final Vector<Client> clients = new Vector<Client>();

	private static ServerSocket socket = null;

	private static Thread connectionThread;
	private static Thread gameLoopThread;

	public static void start() {

		try {
			socket = new ServerSocket(Consts.PORT);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

		// Client connection thread
		connectionThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						final Client client = new Client(socket.accept());

						if (client.isConnected()) {
							System.out.println(client.getPseudo() + " connected");
							for (final Client receiver : clients) {
								receiver.sendConnection(client.getPseudo());
							}

							clients.add(client);
							World.getEntities().add(client);
						} else {
							client.close();
						}
					} catch (final SocketException e) {
						return;
					} catch (final IOException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		};
		connectionThread.start();

		// Game loop thread
		gameLoopThread = new Thread() {
			@Override
			public void run() {
				while (!socket.isClosed()) {
					for (int n = clients.size() - 1; n >= 0; n--) {
						final Client client = clients.get(n);
						if (client.isConnected()) {
							client.sendEntities(World.getEntities());
							client.sendPos();
						} else {
							client.close();
							clients.remove(client);
							World.getEntities().remove(client);

							for (final Client receiver : clients) {
								receiver.sendDisconnection(client.getPseudo());
							}
							System.out.println(client.getPseudo() + " disconnected");
						}
					}

					for (Entity e : World.getEntities()) {
						e.tick();
					}

					for (int a = 0; a < World.getEntities().size(); a++) {
						for (int b = a + 1; b < World.getEntities().size(); b++) {
							Entity.collide(World.getEntities().get(a), World.getEntities().get(b));
						}
					}

					for (Entity e : World.getEntities()) {
						e.updateMove();
					}

					long sleep = time - System.nanoTime() + interval;
					if (sleep > 0) {
						try {
							Thread.sleep(sleep / 1_000_000L, (int) (sleep % 1_000_000L));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					time = System.nanoTime();
				}
			}
		};
		gameLoopThread.start();

		System.out.println("Server ready");
	}

	public static void stop() {
		try {
			socket.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		try {
			connectionThread.join();
			gameLoopThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (final Client client : clients) {
			client.sendClose();
			client.close();
		}
		clients.clear();

		System.out.println("Server closed");
	}
}
