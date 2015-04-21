package projekt;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Server implements Runnable {
	private ServerThread clients[] = new ServerThread[50];
	private ServerSocket server = null;
	private Socket server2serverSocket = null;
	private Thread thread = null;
	private int clientCount = 0;
	private ArrayList<String> history = null;
	private boolean historySend = true;

	public static DBController dbc = DBController.getInstance();

	public Server(int port) throws SQLException {
		try {
			// anlegen der Datenbank und
			dbc.initDBConnection();
			// dbc.löscheHistory();
			history = legeDatenbankAnHoleDatenAusDatenbank();

			serverStartenUndPortbinding(port);
			// TODO Message von Server2Server programmieren
			verbindungZuZweitemServerAufbauen(port);

		} catch (IOException ioe) {
			System.out.println("Can not bind to port " + port + ": "
					+ ioe.getMessage());
		}
	}

	private void verbindungZuZweitemServerAufbauen(int port) throws IOException {
		if (port == 8081)
			server2serverSocket = server2ServerConnector("127.0.0.1", 8080);

		if (port == 8080)
			server2serverSocket = server2ServerConnector("127.0.0.1", 8081);
	}

	private void serverStartenUndPortbinding(int port) throws IOException {
		System.out.println("Binding to port " + port + ", please wait  ...");
		server = new ServerSocket(port);
		System.out.println("Server started: " + server);
		start();
	}

	public Socket server2ServerConnector(String serverName, int serverPort)
			throws IOException {
		try {
			server2serverSocket = new Socket(serverName, serverPort);
			System.out.println("Connected: " + server2serverSocket);
			start();
		} catch (UnknownHostException uhe) {
			System.out.println("Host unknown: " + uhe.getMessage());
		} catch (IOException ioe) {
			System.out.println("Unexpected exception: " + ioe.getMessage());
		}
		return server2serverSocket;
	}

	// legt falls keine Daten in der Datenbank vorhanden Testdaten an
	private static ArrayList<String> legeDatenbankAnHoleDatenAusDatenbank()
			throws SQLException {
		dbc.legeTestDatenAn();
		return dbc.getHistory();
	}

	public void run() {
		while (thread != null) {
			try {
				System.out.println("Waiting for a client ...");
				addThread(server.accept());
			} catch (IOException ioe) {
				System.out.println("Server accept error: " + ioe);
				stop();
			}
		}
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
	}

	private int findClient(int ID) {
		for (int i = 0; i < clientCount; i++)
			if (clients[i].getID() == ID)
				return i;
		return -1;
	}

	public synchronized void handle(int ID, String input) throws SQLException {
		if (input.equalsIgnoreCase("beenden")) {
			clients[findClient(ID)].send(".bye");
			remove(ID);
		} else {
			// System.out.println(input);
			String eMail = extrahiereBenutzerdaten(input);
			input = input.substring(eMail.length() + 1);
			String benutzerName = extrahiereBenutzerdaten(input);
			input = input.substring(benutzerName.length() + 1);
			String passwort = extrahiereBenutzerdaten(input);
			input = input.substring(passwort.length() + 1);

			int i = 0;
			for (i = 0; i < clientCount; i++) {
				if (historySend) {
					// Schicken der History mit der ersten Nachricht des Clients
					clients[i].send(history);
					historySend = false;
				}

				clients[i].send(benutzerName + ": " + input);

				dbc.speicherNachrichtInDatenbank(eMail, benutzerName, passwort,
						ID, input);
			}
		}

	}

	private String extrahiereBenutzerdaten(String input) {
		String s = "";
		s = input.substring(0, input.indexOf(";"));
		return s;
	}

	public synchronized void remove(int ID) {
		int pos = findClient(ID);
		if (pos >= 0) {
			ServerThread toTerminate = clients[pos];
			System.out.println("Removing client thread " + ID + " at " + pos);
			if (pos < clientCount - 1)
				for (int i = pos + 1; i < clientCount; i++)
					clients[i - 1] = clients[i];
			clientCount--;
			try {
				toTerminate.close();
			} catch (IOException ioe) {
				System.out.println("Error closing thread: " + ioe);
			}
			toTerminate.stop();
		}
	}

	private void addThread(Socket socket) {
		if (clientCount < clients.length) {
			System.out.println("Client accepted: " + socket);
			clients[clientCount] = new ServerThread(this, socket);
			try {
				clients[clientCount].open();
				clients[clientCount].start();
				clientCount++;
			} catch (IOException ioe) {
				System.out.println("Error opening thread: " + ioe);
			}
		} else
			System.out.println("Client refused: maximum " + clients.length
					+ " reached.");
	}

	public static void main(String args[]) throws SQLException {

		Server server = new Server(8081);
	}
}