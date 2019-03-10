package kireyis.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import kireyis.common.Consts;

public class Server {
	private static final int TPS = 100;
	private static final long INTERVAL = 1_000_000_000L / TPS;

	private static final ArrayList<Client> clients = new ArrayList<>();

	private static ServerSocket socket = null;

	private static Thread connectionThread;

	private static final ScheduledExecutorService gameLoop = Executors.newSingleThreadScheduledExecutor();

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
							System.out.println(client.getNickname() + " connected");
							for (final Client receiver : clients) {
								receiver.sendConnection(client.getNickname());
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

		gameLoop.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				for (int n = clients.size() - 1; n >= 0; n--) {
					final Client client = clients.get(n);
					if (client.isConnected()) {
						client.sendEntities(World.getVisibleEntities(client));
						client.sendPos();
					} else {
						client.close();
						clients.remove(client);

						for (final Client receiver : clients) {
							receiver.sendDisconnection(client.getNickname());
						}
						System.out.println(client.getNickname() + " disconnected");
					}
				}

				World.tickEntities();
			}
		}, 0, INTERVAL, TimeUnit.NANOSECONDS);

		System.out.println("Server ready");
	}

	public static void stop() {
		try {
			socket.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		gameLoop.shutdown();

		try {
			connectionThread.join();
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

	public static boolean isNicknameUsed(final String pseudo) {
		for (final Client client : Server.clients) {
			if (client.getNickname().equalsIgnoreCase(pseudo)) {
				return true;
			}
		}
		return false;
	}
}
