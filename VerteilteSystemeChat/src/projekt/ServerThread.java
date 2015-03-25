package projekt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.SQLException;

public class ServerThread implements Runnable {

	public int port;
	public DBController dbc;

	public ServerThread() {
	}

	public ServerThread(int port, DBController dbc) {
		this.port = port;
		this.dbc = dbc;
		// run();
	}

	@Override
	public void run() {
		try {
			oeffneServerPortFuerClient(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void oeffneServerPortFuerClient(int port) throws IOException,
			SQLException {
		java.net.ServerSocket serverSocket = new java.net.ServerSocket(port);

		// Damit der Server weiss auf welchen Sockets er Clients besitzt
		Server.addClients(serverSocket);

		while (true) {
			java.net.Socket client = warteAufAnmeldungClient(serverSocket);

			String nachricht = leseNachricht(client);

			System.out.println(nachricht);

		}
	}

	java.net.Socket warteAufAnmeldungClient(java.net.ServerSocket serverSocket)
			throws IOException {
		System.out.println("Warte auf Client Port:" + port);
		// blockiert, bis sich ein Client angemeldet hat
		java.net.Socket socket = serverSocket.accept();
		return socket;
	}

	String leseNachricht(java.net.Socket socket) throws IOException,
			SQLException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		char[] buffer = new char[200];
		int anzahlZeichen = bufferedReader.read(buffer, 0, 200);
		String nachricht = new String(buffer, 0, anzahlZeichen);
		nachricht = extrahiereBenutzernameUndPasswortVonNachricht(nachricht);

		return nachricht;
	}

	void schreibeNachricht(java.net.Socket socket, String nachricht)
			throws IOException {
		PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		printWriter.print(nachricht);
		printWriter.flush();
	}

	private String extrahiereBenutzernameUndPasswortVonNachricht(
			String nachricht) throws SQLException {
		String benutzername = nachricht.substring(0, nachricht.indexOf(";"));
		nachricht = nachricht.substring(benutzername.length() + 1);
		String passwort = nachricht.substring(0, nachricht.indexOf(";"));
		nachricht = nachricht.substring(passwort.length() + 1);
		dbc.speicherNachrichtInDatenbank(benutzername, passwort, nachricht);
		return benutzername + ": " + nachricht;
	}

}
