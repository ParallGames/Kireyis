package kireyis.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import kireyis.common.Consts;
import kireyis.common.DataID;
import kireyis.server.entities.Arrow;
import kireyis.server.entities.Entity;
import kireyis.server.entities.Player;

public final class Client {
	private int viewDistance = Consts.DEFAULT_VIEW;

	private final Socket socket;
	private DataInputStream in;
	private DataOutputStream out;

	private String pseudo;

	private boolean connected = false;

	private final LinkedBlockingQueue<Runnable> sendRequests = new LinkedBlockingQueue<Runnable>();

	private Thread receivingThread;
	private Thread sendingThread;

	private Player player;

	public Client(final Socket socket) {
		player = new Player(5, 5);

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
			sendViewDistance();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		// Data receiving loop
		receivingThread = new Thread() {
			@Override
			public void run() {
				while (connected) {
					try {
						final byte dataID = in.readByte();
						if (dataID == DataID.HORIZONTAL_ACCEL) {
							player.setHorizontalAccel(in.readByte());
						} else if (dataID == DataID.VERTICAL_ACCEL) {
							player.setVerticalAccel(in.readByte());
						} else if (dataID == DataID.VIEW_DISTANCE) {
							viewDistance = in.readInt();
						} else if (dataID == DataID.CLOSE) {
							connected = false;
							return;
						} else if (dataID == DataID.PLAYER_ROTATION) {
							player.setRotation(in.readDouble());
						} else if (dataID == DataID.THROW_ARROW) {
							World.getEntities().add(new Arrow(player.getX(), player.getY(), in.readDouble(),
									player.getSpeedX(), player.getSpeedY()));
						} else {
							System.err.println("Wrong datatype received from " + pseudo + ".");
							connected = false;
							return;
						}
					} catch (final IOException e) {
						if (connected) {
							System.err.println("Connection error with " + pseudo + ".");
							connected = false;
						}
						return;
					}
				}
			}
		};

		// Data sending loop
		sendingThread = new Thread() {
			@Override
			public void run() {
				while (connected) {
					try {
						sendRequests.take().run();
					} catch (final InterruptedException e) {
						return;
					}
				}
			}
		};
		connected = true;

		receivingThread.start();
		sendingThread.start();
	}

	public void close() {
		connected = false;
		player.setAlive(false);

		try {
			socket.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		try {
			sendingThread.interrupt();
		} catch (final NullPointerException e) {
			// Ignore
		}

		try {
			receivingThread.join();
		} catch (final NullPointerException e) {
			// Ignore
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		try {
			sendingThread.join();
		} catch (final NullPointerException e) {
			// Ignore
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void queue(final Runnable runnable) {
		if (sendRequests.size() > Consts.MAX_QUEUE) {
			System.err.println(pseudo + "'s connection timed out.");
			connected = false;
		} else {
			sendRequests.add(runnable);
		}
	}

	public String getPseudo() {
		return pseudo;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isConnected() {
		return connected;
	}

	public synchronized void sendWorld() {
		queue(new Runnable() {
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
					connected = false;
				}
			}
		});
	}

	public synchronized void sendClose() {
		queue(new Runnable() {
			@Override
			public void run() {
				try {
					out.writeByte(DataID.CLOSE);
				} catch (final IOException e) {
					connected = false;
				}
			}
		});
	}

	public synchronized void sendConnection(final String username) {
		queue(new Runnable() {
			@Override
			public void run() {
				try {
					out.writeByte(DataID.CLIENT_CONNECTION);
					out.writeUTF(username);
				} catch (final IOException e) {
					connected = false;
				}
			}
		});
	}

	public synchronized void sendDisconnection(final String username) {
		queue(new Runnable() {
			@Override
			public void run() {
				try {
					out.writeByte(DataID.CLIENT_DISCONNECTION);
					out.writeUTF(username);
				} catch (final IOException e) {
					connected = false;
				}
			}
		});
	}

	public synchronized void sendEntities(final ArrayList<Entity> entities) {
		final ArrayList<Entity> sended = new ArrayList<Entity>();

		for (final Entity entity : entities) {
			final double viewDistance = Client.this.viewDistance + entity.getSize();
			if (entity.getX() < player.getX() + viewDistance && entity.getX() > player.getX() - viewDistance
					&& entity.getY() < player.getY() + viewDistance && entity.getY() > player.getY() - viewDistance
					&& entity != player) {
				sended.add(entity);
			}
		}

		queue(new Runnable() {
			@Override
			public void run() {
				try {
					out.writeByte(DataID.ENTITIES);
					out.writeInt(sended.size());

					for (final Entity entity : sended) {
						out.writeByte(entity.getTypeID());
						out.writeDouble(entity.getX());
						out.writeDouble(entity.getY());
						out.writeDouble(entity.getRotation());
					}
				} catch (final IOException e) {
					connected = false;
				}
			}
		});
	}

	public synchronized void sendPos() {
		final double x = player.getX();
		final double y = player.getY();

		queue(new Runnable() {
			@Override
			public void run() {
				try {
					out.writeByte(DataID.PLAYER_POS);
					out.writeDouble(x);
					out.writeDouble(y);
				} catch (final IOException e) {
					connected = false;
				}
			}
		});
	}

	public synchronized void sendViewDistance() {
		final int viewDistance = this.viewDistance;

		queue(new Runnable() {
			@Override
			public void run() {
				try {
					out.writeByte(DataID.VIEW_DISTANCE);
					out.writeInt(viewDistance);
				} catch (final IOException e) {

				}
			}
		});
	}
}
