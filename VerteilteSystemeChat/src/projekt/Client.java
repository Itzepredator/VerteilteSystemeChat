package projekt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 
 * @author Andreas Der Client verbindet sich automatisch mit dem Server und
 *         bleibt solange aktiv, bis das Wort BEENDEN eingegeben wurde
 *         (unabh‰ngig ob groﬂ oder klein) !!!!
 */
public class Client {
	public static Scanner scan = new Scanner(System.in);
	public static String benutzername = "";
	public static String passwort = "";
	public static int port = 0;
	public static boolean clientstop = true;

	public static void main(String[] args) {
		Client client = new Client();

		System.out.println("Bitte geben Sie Ihren Benutzernamen ein:");
		benutzername = scan.nextLine();
		System.out.println("Bitte geben Sie Ihr Passwort ein:");
		passwort = scan.nextLine();
		System.out.println("Bitte geben Sie den Serverport ein:");
		port = Integer.valueOf(scan.nextLine());
		try {
			while (clientstop) {
				client.sendeNachrichtAnServer();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void sendeNachrichtAnServer() throws IOException {
		String ip = "127.0.0.1"; // localhost
		java.net.Socket socket = new java.net.Socket(ip, port); // verbindet
																// sich mit
																// Server

		System.out.println("Bitte Nachricht eingeben:");

		String zuSendendeNachricht = scan.nextLine();
		if (zuSendendeNachricht.equalsIgnoreCase("beenden")) {
			clientstop = false;
		}
		schreibeNachricht(socket, benutzername + ";" + passwort + ";"
				+ zuSendendeNachricht);
		// String empfangeneNachricht = leseNachricht(socket);
		// System.out.println(empfangeneNachricht);
	}

	void schreibeNachricht(java.net.Socket socket, String nachricht)
			throws IOException {
		PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		printWriter.print(nachricht);
		printWriter.flush();
	}

	String leseNachricht(java.net.Socket socket) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		char[] buffer = new char[200];
		int anzahlZeichen = bufferedReader.read(buffer, 0, 200); // blockiert
																	// bis
																	// Nachricht
																	// empfangen
		String nachricht = new String(buffer, 0, anzahlZeichen);
		return nachricht;
	}
}