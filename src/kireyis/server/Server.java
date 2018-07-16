package kireyis.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Vector;

import kireyis.common.Consts;

public class Server {
	public static final Vector<Client> clients = new Vector<Client>();

	private static ServerSocket socket = null;
	
	public static Vector<Entity> getPlayerEntities() {
		Vector<Entity> players = new Vector<Entity>();
		
		for(Client c : clients) {
			players.add(c.getEntity());
		}
			
		
		return players;
	}

	public static void start() {

		try {
			socket = new ServerSocket(Consts.PORT);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Client client = new Client(socket.accept());

						if (client.isConnected()) {
							System.out.println(client.getPseudo() + " connected");
							for (Client reciever : clients) {
								reciever.sendConnexion(client.getPseudo());
							}

							clients.add(client);
						} else {
							client.close();
						}
					} catch (SocketException e) {
						return;
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		}.start();

		new Thread() {
			@Override
			public void run() {
				while (!socket.isClosed()) {
					for (int n = clients.size() - 1; n >= 0; n--) {
						Client client = clients.get(n);
						if (client.isConnected()) {
							client.sendPos();
						} else {
							System.out.println(client.getPseudo() + " disconnected");
							clients.remove(client);

							for (Client reciever : clients) {
								reciever.sendDisconnexion(client.getPseudo());
							}
						}
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		System.out.println("Server ready");
	}

	public static void stop() {
		for (Client client : clients) {
			client.sendCloseEvent();
			client.close();
		}

		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Server closed");
	}
}
