package projekt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client implements Runnable {
	public static Scanner scan = new Scanner(System.in);

	private Socket socket = null;
	private Thread thread = null;
	private DataInputStream console = null;
	private DataOutputStream streamOut = null;
	private ClientThread client = null;
	String eMail = "";
	String benutzerName = "";
	String passwort = "";

	public Client(String serverName, int serverPort) throws IOException {
		System.out.println("Establishing connection. Please wait ...");
		System.out.println("Bitte geben Sie Ihre EMail ein: ");
		eMail = scan.nextLine();
		System.out.println("Bitte geben Sie Ihren Benutzernamen ein: ");
		benutzerName = scan.nextLine();
		System.out.println("Bitte geben Sie Ihr Passwort ein: ");
		passwort = scan.nextLine();
		try {
			socket = new Socket(serverName, serverPort);
			System.out.println("Connected: " + socket);
			start();
		} catch (UnknownHostException uhe) {
			System.out.println("Host unknown: " + uhe.getMessage());
		} catch (IOException ioe) {
			System.out.println("Unexpected exception: " + ioe.getMessage());
		}
	}

	public void run() {
		while (thread != null) {
			try {

				streamOut.writeUTF(eMail + ";" + benutzerName + ";" + passwort
						+ ";" + scan.nextLine());
				streamOut.flush();
			} catch (IOException ioe) {
				System.out.println("Sending error: " + ioe.getMessage());
				stop();
			}
		}
	}

	public void handle(String msg) {
		if (msg.equalsIgnoreCase("beenden")) {
			System.out.println("Tschöö ...");
			stop();
		} else
			System.out.println(msg);
	}

	public void start() throws IOException {
		console = new DataInputStream(System.in);
		streamOut = new DataOutputStream(socket.getOutputStream());
		if (thread == null) {
			client = new ClientThread(this, socket);
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
		try {
			if (console != null)
				console.close();
			if (streamOut != null)
				streamOut.close();
			if (socket != null)
				socket.close();
		} catch (IOException ioe) {
			System.out.println("Error closing ...");
		}
		client.close();
		client.stop();
	}

	public static void main(String args[]) throws IOException {
		// Client client = new Client("127.0.0.1", 8080);
		Client client = new Client("127.0.0.1", 8081);
	}
}