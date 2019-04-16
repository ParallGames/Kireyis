package kireyis.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import kireyis.common.Consts;
import kireyis.common.DataID;
import kireyis.common.entityModels.EntityModels;
import kireyis.server.entities.Arrow;
import kireyis.server.entities.Entity;
import kireyis.server.entities.Player;

public final class Client {
	private static final int MAX_QUEUE = 100;

	private int viewDistance = Consts.DEFAULT_VIEW;

	private final Socket socket;
	private DataInputStream in;
	private DataOutputStream out;

	private String nickname;

	private boolean connected = false;

	private final LinkedBlockingQueue<Runnable> sendRequests = new LinkedBlockingQueue<>();

	private Thread receivingThread;
	private Thread sendingThread;

	private Player player;

	public Client(final Socket socket) {
		player = new Player(5, 5);

		this.socket = socket;

		try {
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

			nickname = in.readUTF();

			if (nickname.isEmpty() || Server.isNicknameUsed(nickname)) {
				out.writeBoolean(false);
				out.flush();
				return;
			} else {
				out.writeBoolean(true);
			}

			sendWorld();
			sendViewDistance();
			sendMaxHP();
			out.flush();
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
							final double arrowSize = EntityModels.ARROW.getSize() / 2;
							World.addEntity(new Arrow(player.getX() + arrowSize, player.getY() + arrowSize,
									in.readDouble(), player.getSpeedX(), player.getSpeedY()));
						} else {
							System.err.println("Wrong datatype received from " + nickname + ".");
							connected = false;
							return;
						}
					} catch (final IOException e) {
						if (connected) {
							System.err.println("Connection error with " + nickname + ".");
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

		if (sendingThread != null) {
			sendingThread.interrupt();
		}

		if (receivingThread != null) {
			try {
				receivingThread.join();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (sendingThread != null) {
			try {
				sendingThread.join();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void queue(final Runnable runnable) {
		if (sendRequests.size() > MAX_QUEUE) {
			System.err.println(nickname + "'s connection timed out.");
			connected = false;
		} else {
			sendRequests.add(runnable);
		}
	}

	public String getNickname() {
		return nickname;
	}

	public Player getPlayer() {
		return player;
	}

	public int getViewDistance() {
		return viewDistance;
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
							out.writeByte(World.getTile(x, y));
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

	public synchronized void sendEntities(final List<Entity> entities) {
		queue(new Runnable() {
			@Override
			public void run() {
				try {
					out.writeByte(DataID.ENTITIES);
					out.writeInt(entities.size());

					for (final Entity entity : entities) {
						out.writeByte(entity.getID());
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
					connected = false;
				}
			}
		});
	}

	public synchronized void sendMaxHP() {
		final int maxHP = this.getPlayer().getMaxHP();

		queue(new Runnable() {
			@Override
			public void run() {
				try {
					out.writeByte(DataID.PLAYER_MAX_HEALTH);
					out.writeInt(maxHP);
				} catch (final IOException e) {
					connected = false;
				}
			}
		});
	}

	public synchronized void sendHP() {
		final int hp = this.getPlayer().getHP();

		queue(new Runnable() {
			@Override
			public void run() {
				try {
					out.writeByte(DataID.PLAYER_HEALTH);
					out.writeInt(hp);
				} catch (final IOException e) {
					connected = false;
				}
			}
		});
	}

	public synchronized void flush() {
		queue(new Runnable() {
			@Override
			public void run() {
				try {
					out.flush();
				} catch (final IOException e) {
					connected = false;
				}
			}
		});
	}
}
