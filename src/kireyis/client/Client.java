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
		} catch (final NullPointerException e) {
			// Ignore
		} catch (final IOException e) {
			e.printStackTrace();
		}
		GameLoop.stop();
	}

	public static boolean connect(final String ip, final String pseudo) {
		Client.pseudo = pseudo;
		try {
			socket = new Socket(ip, Consts.PORT);

			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

			out.writeUTF(pseudo);

			if (!in.readBoolean()) {
				return false;
			}
		} catch (final UnknownHostException e) {
			// e.printStackTrace();

			System.err.println("Ip introuvable");
			return false;
		} catch (final IOException e) {
			// e.printStackTrace();

			System.err.println("Serveur introuvable");
			return false;
		}

		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						final byte dataID = in.readByte();

						if (dataID == DataID.INFO) {
							final String info = in.readUTF();
							System.out.println(info);
						} else if (dataID == DataID.CLOSE) {
							System.err.println("Server Closed");
							close();
							return;
						} else if (dataID == DataID.CLIENT_CONNECTION) {
							final String name = in.readUTF();
							System.out.println(name + " connected");
						} else if (dataID == DataID.CLIENT_DISCONNECTION) {
							final String name = in.readUTF();
							System.out.println(name + " disconnected");
						} else if (dataID == DataID.WORLD) {
							for (int y = 0; y < Consts.WORLD_SIZE; y++) {
								for (int x = 0; x < Consts.WORLD_SIZE; x++) {
									World.set(x, y, in.readByte());
								}
							}
						} else if (dataID == DataID.ENTITIES) {
							final ArrayList<RenderEntity> entities = new ArrayList<RenderEntity>();

							final int num = in.readInt();

							for (int n = 0; n < num; n++) {
								final byte id = in.readByte();
								final double x = in.readDouble();
								final double y = in.readDouble();
								entities.add(new RenderEntity(id, x, y));
							}
							World.setEntities(entities);
						} else if (dataID == DataID.PLAYER_POS) {
							Player.setPos(in.readDouble(), in.readDouble());
						} else {
							close();
							throw new RuntimeException("Unknown data type");
						}
					} catch (final IOException e) {
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

	public static synchronized void sendMove(final double moveX, final double moveY) {
		if (moveX != 0 || moveY != 0) {
			try {
				out.writeByte(DataID.PLAYER_MOVE);
				out.writeDouble(moveX);
				out.writeDouble(moveY);
			} catch (final IOException e) {
				System.err.println("Disconnected");
				close();
			}
		}
	}

	public static synchronized void sendViewDistance() {
		try {
			out.writeByte(DataID.VIEW_DISTANCE);
			out.writeInt(Player.getViewDistance());
		} catch (final IOException e) {
			System.err.println("Disconnected");
			close();
		}
	}
}
