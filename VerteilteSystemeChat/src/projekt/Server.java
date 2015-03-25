package projekt;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 
 * @author Andreas
 * 
 *         Der Server stellt eine Verbindung her auf dem Port 8081 Auf diesen
 *         kann sich der Client verbinden Die Datenbank wird bei jedem start des
 *         Servers initial gelöscht und mit Testdaten befüllt Sind bereits Daten
 *         in der DB vorhanden, so werden diese Verwendet
 * 
 *         TODO Es fehlt noch eine saubere Übergabe der Daten an den Client,
 *         dieser sieht bisher nur seine Eingaben
 * 
 *         TODO Server 2 Server kommunikation
 * 
 *         TODO Login für Clients ausprogrammieren
 * 
 *         TODO Broadcastfunktion
 */
public class Server {

	public static DBController dbc = DBController.getInstance();
	public static ArrayList<ServerSocket> clients = new ArrayList<>();

	public static void main(String[] args) throws SQLException {

		ArrayList<String> history = legeDatenbankAnHoleDatenAusDatenbank();

		// TODO Nur für Testzwecke ausgabe des Datenbank Inhaltes
		for (String string : history) {
			System.out.println(string);
		}

		Server server = new Server();
		try {
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void start() throws IOException, SQLException {
		new Thread(new ServerThread(8081, dbc)).start();
		new Thread(new ServerThread(8082, dbc)).start();

		// schreibeNachricht(client, nachricht);
	}

	private static ArrayList<String> legeDatenbankAnHoleDatenAusDatenbank()
			throws SQLException {

		dbc.initDBConnection();

		// TODO nur zu Testzwecken werte Anlegen
		// dbc.löscheHistory();
		dbc.legeTestDatenAn();

		return getChatHistoryAusEigenerDatenbank(dbc);
	}

	private static ArrayList<String> getChatHistoryAusEigenerDatenbank(
			DBController dbc) throws SQLException {
		ArrayList<String> a = new ArrayList<>();

		return dbc.getHistory();
	}

	public static void addClients(ServerSocket client) {
		Server.clients.add(client);
	}

}
