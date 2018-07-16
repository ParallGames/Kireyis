package kireyis.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import kireyis.common.Consts;
import kireyis.common.DataID;

public class Client {
	private static Socket socket;

	private static DataInputStream in;
	private static DataOutputStream out;

	private static String pseudo;

	public static void close() {
		try {
			socket.close();
		} catch (NullPointerException e) {
			// Ignore
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean connect(String ip, String pseudo) {
		Client.pseudo = pseudo;
		try {
			socket = new Socket(ip, Consts.PORT);

			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

			out.writeUTF(pseudo);

			if (!in.readBoolean()) {
				return false;
			}
		} catch (UnknownHostException e) {
			// e.printStackTrace();

			System.err.println("Ip introuvable");
			return false;
		} catch (IOException e) {
			// e.printStackTrace();

			System.err.println("Serveur introuvable");
			return false;
		}

		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						byte dataID = in.readByte();
						
						if (dataID == DataID.INFO) {
							String info = in.readUTF();
						} else if (dataID == DataID.CLOSE) {
							System.err.println("Server Closed");
							close();

							return;
						} else if (dataID == DataID.CLIENT_CONNEXION) {
							String name = in.readUTF();
						} else if (dataID == DataID.CLIENT_DISCONNEXION) {
							String name = in.readUTF();
						} else if (dataID == DataID.WORLD) {
							int size = in.readInt();
							int x = in.readInt();
							int y = in.readInt();
							
							byte world[] = new byte[size * size];
							
							for(int n = 0; n < world.length; n++) {
								world[n] = in.readByte();
							}
							
							for(int a = 0; a < size; a++) {
								for(int b = 0; b < size; b++) {
									World.set(x + a, y + b, world[a + b * size]);
								}
							}
						} else if (dataID == DataID.ENTITIES) {
							World.getEntities().clear();
							int num = in.readInt();
							
							for(int n = 0; n < num; n++) {
								byte id = in.readByte();
								double x = in.readDouble();
								double y = in.readDouble();
								World.getEntities().add(new Entity(id, x, y));
							}
						} else if (dataID == DataID.PLAYER_POS) {
							Player.setPos(in.readDouble(), in.readDouble());
						}
					} catch (IOException e) {
						System.err.println("Disconnected");
						close();
						return;
					}
				}
			}
		}.start();

		return true;
	}

	public static String getPseudo() {
		return pseudo;
	}
	
	public static void sendMove(double moveX, double moveY) {
		try {
			out.writeByte(DataID.PLAYER_MOVE);
			out.writeDouble(moveX);
			out.writeDouble(moveY);
		} catch (IOException e) {
			System.err.println("Disconnected");
			close();
		}
	}

	public static void sendViewDistance() {
		try {
			out.writeByte(DataID.VIEW_DISTANCE);
			out.writeDouble(Player.getViewDistance());
		} catch (IOException e) {
			System.err.println("Disconnected");
			close();
		}
	}
}
