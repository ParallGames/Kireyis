package kireyis.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
		GameLoop.stop();
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

			for (int y = 0; y < Consts.WORLD_SIZE; y++) {
				for (int x = 0; x < Consts.WORLD_SIZE; x++) {
					World.set(x, y, in.readByte());
				}
			}

			Player.setPos(in.readDouble(), in.readDouble());

			System.out.println("World recieved");
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
							System.out.println(info);
						} else if (dataID == DataID.CLOSE) {
							System.err.println("Server Closed");
							close();
							return;
						} else if (dataID == DataID.CLIENT_CONNEXION) {
							String name = in.readUTF();
							System.out.println(name + " connected");
						} else if (dataID == DataID.CLIENT_DISCONNEXION) {
							String name = in.readUTF();
							System.out.println(name + " disconnected");
						} else if (dataID == DataID.WORLD) {

						} else if (dataID == DataID.ENTITIES) {
							ArrayList<RenderEntity> entities = new ArrayList<RenderEntity>();

							int num = in.readInt();

							for (int n = 0; n < num; n++) {
								byte id = in.readByte();
								double x = in.readDouble();
								double y = in.readDouble();
								entities.add(new RenderEntity(id, x, y));
							}
							World.setEntities(entities);
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

	public static synchronized void sendMove(double moveX, double moveY) {
		try {
			out.writeByte(DataID.PLAYER_MOVE);
			out.writeDouble(moveX);
			out.writeDouble(moveY);
		} catch (IOException e) {
			System.err.println("Disconnected");
			close();
		}
	}

	public static synchronized void sendViewDistance() {
		try {
			out.writeByte(DataID.VIEW_DISTANCE);
			out.writeInt(Player.getViewDistance());
		} catch (IOException e) {
			System.err.println("Disconnected");
			close();
		}
	}
}
