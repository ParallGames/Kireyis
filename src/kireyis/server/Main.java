package kireyis.server;

import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Server.start();

		Scanner sc = new Scanner(System.in);

		while (true) {
			if (sc.nextLine().equalsIgnoreCase("stop")) {
				Server.stop();
				sc.close();
				return;
			}
		}
	}
}
