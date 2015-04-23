package projekt;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Server implements Runnable {
	private Server2ServerThread data = new Server2ServerThread(
			new ServerThread[50], null, null, null, 0, null, true);
	int port = 0;
	boolean connected = false;
	public static DBController dbc = DBController.getInstance();

	public Server(int port) throws SQLException {
		this.port = port;
		try {
			// anlegen der Datenbank und
			dbc.initDBConnection();
			// dbc.löscheHistory();
			data.history = legeDatenbankAnHoleDatenAusDatenbank();

			serverStartenUndPortbinding(port);
			// TODO Message von Server2Server programmieren
			verbindungZuZweitemServerAufbauen(port);

			data.run();

			// data.sendeMessageAnAndereServer(data.server2serverSocket);

		} catch (IOException ioe) {
			System.out.println("Can not bind to port " + port + ": "
					+ ioe.getMessage());
		}
	}

	private void verbindungZuZweitemServerAufbauen(int port) throws IOException {
		if (port == 8081)
			data.server2serverSocket = server2ServerConnector("127.0.0.1", 8080);

		if (port == 8080)
			data.server2serverSocket = server2ServerConnector("127.0.0.1", 8081);

	}

	private void serverStartenUndPortbinding(int port) throws IOException {
		System.out.println("Binding to port " + port + ", please wait  ...");
		data.server = new ServerSocket(port);
		System.out.println("Server started: " + data.server);
		start();
	}

	public Socket server2ServerConnector(String serverName, int serverPort)
			throws IOException {
		try {
			data.server2serverSocket = new Socket(serverName, serverPort);
			System.out.println("Connected: " + data.server2serverSocket);
			start();
		} catch (UnknownHostException uhe) {
			System.out.println("Host unknown: " + uhe.getMessage());
		} catch (IOException ioe) {
			System.out.println("Unexpected exception: " + ioe.getMessage());
		}
		return data.server2serverSocket;
	}

	// legt falls keine Daten in der Datenbank vorhanden Testdaten an
	private static ArrayList<String> legeDatenbankAnHoleDatenAusDatenbank()
			throws SQLException {
		dbc.legeTestDatenAn();
		return dbc.getHistory();
	}

	public void run() {
		while (data.thread != null) {
			try {
				System.out.println("Waiting for a client ...");
				addThread(data.server.accept());
			} catch (IOException ioe) {
				System.out.println("Server accept error: " + ioe);
				stop();
			}
		}
	}

	public void start() {
		if (data.thread == null) {
			data.thread = new Thread(this);
			data.thread.start();
		}
	}

	public void stop() {
		if (data.thread != null) {
			data.thread.stop();
			data.thread = null;
		}
	}

	private int findClient(int ID) {
		for (int i = 0; i < data.clientCount; i++)
			if (data.clients[i].getID() == ID)
				return i;
		return -1;
	}

	public synchronized void handle(int ID, String originalInput)
			throws SQLException {
		String input = originalInput;
		if (input.equalsIgnoreCase("beenden")) {
			data.clients[findClient(ID)].send(".bye");
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
			for (i = 0; i < data.clientCount; i++) {
				if (data.historySend) {
					// Schicken der History mit der ersten Nachricht des Clients
					data.clients[i].send(data.history);
					data.historySend = false;
				}

				data.clients[i].send(benutzerName + ": " + input);

				if (!passwort.isEmpty()) {

					data.sendeMessageAnAndereServer(data.server2serverSocket,
							eMail + ";" + benutzerName + ";" + "" + ";" + input);
				}

				try {
					if (!connected) {
						verbindungZuZweitemServerAufbauen(port);
						connected = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

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
			ServerThread toTerminate = data.clients[pos];
			System.out.println("Removing client thread " + ID + " at " + pos);
			if (pos < data.clientCount - 1)
				for (int i = pos + 1; i < data.clientCount; i++)
					data.clients[i - 1] = data.clients[i];
			data.clientCount--;
			try {
				toTerminate.close();
			} catch (IOException ioe) {
				System.out.println("Error closing thread: " + ioe);
			}
			toTerminate.stop();
		}
	}

	private void addThread(Socket socket) {
		if (data.clientCount < data.clients.length) {
			System.out.println("Client accepted: " + socket);
			data.clients[data.clientCount] = new ServerThread(this, socket);
			try {
				data.clients[data.clientCount].open();
				data.clients[data.clientCount].start();
				data.clientCount++;
			} catch (IOException ioe) {
				System.out.println("Error opening thread: " + ioe);
			}
		} else
			System.out.println("Client refused: maximum " + data.clients.length
					+ " reached.");
	}

	public static void main(String args[]) throws SQLException {

		// Server server = new Server(8080);
		Server server = new Server(8081);
	}
}