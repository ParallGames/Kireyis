package kireyis.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import kireyis.common.Consts;
import kireyis.common.DataID;
import kireyis.common.EntityID;

public final class Client {
	private final int id;

	private double x = 5;
	private double y = 5;

	private int viewDistance = 5;

	private final Socket socket;
	private DataInputStream in;
	private DataOutputStream out;

	private String pseudo;

	private boolean connected = false;

	private final ArrayList<String> messages = new ArrayList<String>();

	private final LinkedBlockingQueue<Runnable> sendRequests = new LinkedBlockingQueue<Runnable>();

	public Client(final Socket socket) {
		this.socket = socket;

		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

			pseudo = in.readUTF();

			boolean pseudoUsed = false;

			for (final Client client : Server.clients) {
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

			sendWorld();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			id = pseudo.hashCode();
		}

		// Data receiving loop
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						final byte dataID = in.readByte();
						if (dataID == DataID.PLAYER_MOVE) {
							final double moveX = in.readDouble();
							final double moveY = in.readDouble();

							if (x + moveX < 0) {
								x = 0;
							} else if (x + moveX > Consts.WORLD_SIZE) {
								x = Consts.WORLD_SIZE;
							} else {
								x += moveX;
							}

							if (y + moveY < 0) {
								y = 0;
							} else if (y + moveY > Consts.WORLD_SIZE) {
								y = Consts.WORLD_SIZE;
							} else {
								y += moveY;
							}
						} else if (dataID == DataID.VIEW_DISTANCE) {
							viewDistance = in.readInt();
						}
					} catch (final IOException e) {
						close();
						return;
					}
				}
			}
		}.start();

		// Data sending loop
		new Thread() {
			@Override
			public void run() {
				while (connected) {
					while (!sendRequests.isEmpty()) {
						sendRequests.remove().run();
					}
					try {
						Thread.sleep(10);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();

		connected = true;
	}

	public void close() {
		try {
			socket.close();
		} catch (final IOException e) {
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

	public synchronized void sendWorld() {
		sendRequests.add(new Runnable() {
			@Override
			public void run() {
				try {
					out.writeByte(DataID.WORLD);

					for (int y = 0; y < Consts.WORLD_SIZE; y++) {
						for (int x = 0; x < Consts.WORLD_SIZE; x++) {
							out.writeByte(World.get(x, y));
						}
					}
				} catch (final IOException e) {
					close();
				}
			}
		});
	}

	public synchronized void sendCloseEvent() {
		sendRequests.add(new Runnable() {
			@Override
			public void run() {
				try {
					out.writeByte(DataID.CLOSE);
				} catch (final IOException e) {
					close();
				}
			}
		});
	}

	public synchronized void sendConnexion(final String username) {
		sendRequests.add(new Runnable() {
			@Override
			public void run() {
				try {
					out.writeByte(DataID.CLIENT_CONNEXION);
					out.writeUTF(username);
				} catch (final IOException e) {
					close();
				}
			}
		});
	}

	public synchronized void sendDisconnexion(final String username) {
		sendRequests.add(new Runnable() {
			@Override
			public void run() {
				try {
					out.writeByte(DataID.CLIENT_DISCONNEXION);
					out.writeUTF(username);
				} catch (final IOException e) {
					close();
				}
			}
		});
	}

	public synchronized void sendEntities(final ArrayList<Entity> entities) {
		sendRequests.add(new Runnable() {
			@Override
			public void run() {
				final ArrayList<Entity> sended = new ArrayList<Entity>();

				for (final Entity entity : entities) {
					if (entity.getX() < x + viewDistance && entity.getX() > x - viewDistance
							&& entity.getY() < y + viewDistance && entity.getY() > y - viewDistance
							&& entity.getID() != id) {
						sended.add(entity);
					}
				}

				try {
					out.writeByte(DataID.ENTITIES);
					out.writeInt(sended.size());

					for (final Entity entity : sended) {
						out.writeByte(entity.getTypeid());
						out.writeDouble(entity.getX());
						out.writeDouble(entity.getY());
					}
				} catch (final IOException e) {
					close();
				}
			}
		});
	}

	public synchronized void sendPos() {
		sendRequests.add(new Runnable() {
			@Override
			public void run() {
				try {
					out.writeByte(DataID.PLAYER_POS);
					out.writeDouble(x);
					out.writeDouble(y);
				} catch (final IOException e) {
					close();
				}
			}
		});
	}

	public Entity getEntity() {
		return new Entity(EntityID.PLAYER, x, y, id);
	}
}
