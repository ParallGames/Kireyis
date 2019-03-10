package kireyis.server;

import java.util.Scanner;

public class Main {
	public static void main(final String[] args) {
		Server.start();

		final Scanner sc = new Scanner(System.in);

		while (true) {
			if (sc.nextLine().equalsIgnoreCase("stop")) {
				Server.stop();
				sc.close();
				return;
			}
		}
	}
}