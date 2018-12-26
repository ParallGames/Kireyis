package kireyis.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;

import kireyis.common.Consts;

public class Server {
	private static final int TPS = 100;
	private static final long INTERVAL = 1_000_000_000L / TPS;
	private static long time = System.nanoTime();

	private static final ArrayList<Client> clients = new ArrayList<>();

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
							World.addEntity(client.getPlayer());
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
							client.sendEntities(World.getVisibleEntities(client));
							client.sendPos();
						} else {
							client.close();
							clients.remove(client);

							for (final Client receiver : clients) {
								receiver.sendDisconnection(client.getPseudo());
							}
							System.out.println(client.getPseudo() + " disconnected");
						}
					}

					World.tickEntities();

					final long sleep = time - System.nanoTime() + INTERVAL;
					if (sleep > 0) {
						try {
							Thread.sleep(sleep / 1_000_000L, (int) (sleep % 1_000_000L));
						} catch (final InterruptedException e) {
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
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		for (final Client client : clients) {
			client.sendClose();
			client.close();
		}
		clients.clear();

		System.out.println("Server closed");
	}

	public static boolean isPseudoUsed(final String pseudo) {
		for (final Client client : Server.clients) {
			if (client.getPseudo().equalsIgnoreCase(pseudo)) {
				return true;
			}
		}
		return false;
	}
}
