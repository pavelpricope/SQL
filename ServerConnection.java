import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class ServerConnection{

	public ServerConnection() {
	}

	public ResultSet connectToDB(String url, String username, String pass, String command) {

		ResultSet rs = null;
		try {

			// Load the PostgreSQL JDBC driver
			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException ex) {
			System.out.println("Driver not found");
			System.exit(1);
		}


		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, username, pass);
		} catch (SQLException ex) {
			System.out.println("Ooops, couldn't get a connection");
			System.out.println("Check that <username> & <password> are right");
			System.exit(1);
		}

		if (conn != null) {
			System.out.println("Database accessed!");
		} else {
			System.out.println("Failed to make connection");
			System.exit(1);
		}

		try {
			PreparedStatement studentQuery = conn.prepareStatement(command);

			rs = studentQuery.executeQuery();

		} catch (SQLException sqlE) {
			
			System.out.println(sqlE.getMessage());

		}
		try {
			conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return rs;
	}

}