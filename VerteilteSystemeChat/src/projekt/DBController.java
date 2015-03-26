package projekt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * 
 * @author Andreas Der DBController stellt die Allgemeinen Funktionalitäten für
 *         die Datenbank zur Verfügung Hierzu gehören das Speicher von
 *         Nachrichten in die DB als auch das Auslesen der History
 */

class DBController {

	private static final DBController dbcontroller = new DBController();
	private static Connection connection;
	private static final String DB_PATH = System.getProperty("user.home") + "/"
			+ "testdb.db";

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.err.println("Fehler beim Laden des JDBC-Treibers");
			e.printStackTrace();
		}
	}

	private DBController() {
	}

	public static DBController getInstance() {
		return dbcontroller;
	}

	// Aufbau der Datenbankconnection
	void initDBConnection() {
		try {
			if (connection != null)
				return;
			System.out.println("Creating Connection to Database...");
			connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
			if (!connection.isClosed())
				System.out.println("...Connection established");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					if (!connection.isClosed() && connection != null) {
						connection.close();
						if (connection.isClosed())
							System.out.println("Connection to Database closed");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Anlegen von Testdaten wenn in der Datenbank keine Daten vorhanden sind
	public void legeTestDatenAn() throws SQLException {
		Statement stmt = connection.createStatement();
		// stmt.executeUpdate("DROP TABLE IF EXISTS history;");
		try {
			stmt.executeUpdate("CREATE TABLE history (email,benutzername, passwort,id, message);");
			stmt.execute("INSERT INTO history (email,benutzername, passwort,id, message) VALUES ('Heinen-andreas@web.de','Andreas', '1234', '1234', 'Ich liebe Verteilte Systeme')");
			stmt.execute("INSERT INTO history ( email,benutzername, passwort,id, message) VALUES ('marvin.obreiter@gmx.de','Marvin', '1234','1234', 'Ich mag Payday 2')");
			stmt.execute("INSERT INTO history ( email,benutzername, passwort,id, message) VALUES ('fabian-kunzke@web.de','Fabian', '1234', '1234','Ich hasse CsGo Office')");

		} catch (Exception e) {
			System.out
					.println("Datenbank existierte bereits daher keine Testdaten angelegt.");
		}

	}

	// Speichern einer Nachricht des Clients in die DB
	public void speicherNachrichtInDatenbank(String eMail, String benutzername,
			String passwort, int ID, String message) throws SQLException {
		Statement stmt = connection.createStatement();
		System.out
				.println("INSERT INTO history (email, benutzername, passwort,id, message) VALUES ('"
						+ eMail
						+ "','"
						+ benutzername
						+ "', '"
						+ passwort
						+ "', '" + ID + "', '" + message + "')");
		stmt.execute("INSERT INTO history (email, benutzername, passwort,id, message) VALUES ('"
				+ eMail
				+ "','"
				+ benutzername
				+ "', '"
				+ passwort
				+ "', '"
				+ ID + "', '" + message + "')");

	}

	// löschen der kompletten History
	public void löscheHistory() throws SQLException {
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("DROP TABLE IF EXISTS history;");
	}

	// Auslesen der History
	public ArrayList<String> getHistory() throws SQLException {
		ArrayList<String> results = new ArrayList<>();
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM history;");
		while (rs.next()) {
			results.add(rs.getString("benutzername") + ": "
					+ rs.getString("message"));
		}

		return results;
	}

}