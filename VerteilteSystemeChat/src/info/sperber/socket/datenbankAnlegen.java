package info.sperber.socket;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class datenbankAnlegen {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		
		Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/vorlesung?user=vorlesung&password=vorlesung");

	}

}
