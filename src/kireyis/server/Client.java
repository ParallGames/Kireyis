package kireyis.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import kireyis.common.Consts;
import kireyis.common.DataID;
import kireyis.common.Entity;
import kireyis.common.EntityID;

public class Client {
	private double x = 5;
	private double y = 5;

	private int viewDistance;

	private final Socket socket;
	private DataInputStream in;
	private DataOutputStream out;

	private String pseudo;

	private boolean connected = false;

	private ArrayList<String> messages = new ArrayList<String>();

	public Client(Socket socket) {
		this.socket = socket;

		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

			pseudo = in.readUTF();

			boolean pseudoUsed = false;

			for (Client client : Server.clients) {
				if (client.getPseudo().equalsIgnoreCase(pseudo)) {
					pseudoUsed = true;
					break;
				}
			}

			if (pseudo.isEmpty() || pseudoUsed) {
				out.writeBoolean(false);
				return;
			} else {
				out.writeBoolean(true);
			}

			for (int y = 0; y < Consts.WORLD_SIZE; y++) {
				for (int x = 0; x < Consts.WORLD_SIZE; x++) {
					out.writeByte(World.get(x, y));
				}
			}
			out.writeDouble(x);
			out.writeDouble(y);

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						byte dataID = in.readByte();
						if (dataID == DataID.PLAYER_MOVE) {
							x += in.readDouble();
							y += in.readDouble();

							if (x < 0) {
								x = 0;
							} else if (x >= Consts.WORLD_SIZE) {
								x = Consts.WORLD_SIZE;
							}

							if (y < 0) {
								y = 0;
							} else if (y >= Consts.WORLD_SIZE) {
								y = Consts.WORLD_SIZE;
							}
						} else if (dataID == DataID.VIEW_DISTANCE) {
							viewDistance = in.readInt();
						}
					} catch (IOException e) {
						close();
						return;
					}
				}
			}
		}.start();
		connected = true;
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		connected = false;
	}

	public ArrayList<String> getMessages() {
		return messages;
	}

	public String getPseudo() {
		return pseudo;
	}

	public boolean isConnected() {
		return connected;
	}

	public synchronized void sendCloseEvent() {
		try {
			out.writeByte(DataID.CLOSE);
		} catch (IOException e) {
			close();
		}
	}

	public synchronized void sendConnexion(String username) {
		try {
			out.writeByte(DataID.CLIENT_CONNEXION);
			out.writeUTF(username);
		} catch (IOException e) {
			close();
		}
	}

	public synchronized void sendDisconnexion(String username) {
		try {
			out.writeByte(DataID.CLIENT_DISCONNEXION);
			out.writeUTF(username);
		} catch (IOException e) {
			close();
		}
	}

	public synchronized void sendEntities(ArrayList<Entity> entities) {
		try {
			out.writeByte(DataID.ENTITIES);
			out.writeInt(entities.size());

			for (int n = 0; n < entities.size(); n++) {
				out.writeByte(entities.get(n).getID());
				out.writeDouble(entities.get(n).getX());
				out.writeDouble(entities.get(n).getY());
			}
		} catch (IOException e) {
			close();
		}
	}

	public synchronized void sendPos() {
		try {
			out.writeByte(DataID.PLAYER_POS);
			out.writeDouble(x);
			out.writeDouble(y);
		} catch (IOException e) {
			close();
		}
	}

	public Entity getEntity() {
		return new Entity(EntityID.PLAYER, x, y);
	}
}
