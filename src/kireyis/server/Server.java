package kireyis.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Vector;

import kireyis.common.Consts;

public class Server {
	public static final Vector<Client> clients = new Vector<Client>();

	private static ServerSocket socket = null;

	public static void start() {

		try {
			socket = new ServerSocket(Consts.PORT);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

		// Client connection thread
		new Thread() {
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
		}.start();

		// Game loop thread
		new Thread() {
			@Override
			public void run() {
				while (!socket.isClosed()) {
					for (int n = clients.size() - 1; n >= 0; n--) {
						final Client client = clients.get(n);
						if (client.isConnected()) {
							client.sendEntities(World.getEntities());
							client.sendPos();
						} else {
							System.out.println(client.getPseudo() + " disconnected");
							clients.remove(client);
							World.getEntities().remove(client);

							for (final Client receiver : clients) {
								receiver.sendDisconnection(client.getPseudo());
							}
						}
					}
					try {
						Thread.sleep(10);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		System.out.println("Server ready");
	}

	public static void stop() {
		for (final Client client : clients) {
			client.sendCloseEvent();
			client.close();
		}

		try {
			socket.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		System.out.println("Server closed");
	}
}
