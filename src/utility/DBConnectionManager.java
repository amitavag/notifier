package utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {
	
	public static DBConnection getConnection() {
		Connection conn = null;
		DBConnection dBConnection = null;
		try {
			// Load the JDBC driver
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);
			// Database schema name
			String sid = PropertiesUtil.getValue("db.schema");
			// Database server port no
			String portNo = PropertiesUtil.getValue("db.port");
			// Database Server IP address
			String hostName = PropertiesUtil.getValue("db.ip");
			// Create a connection with the database
			String url = "jdbc:mysql://" + hostName + ":" + portNo + "/" + sid;
			// Database logging userID
			String userName = PropertiesUtil.getValue("db.user");
			// Database logging password
			String password = PropertiesUtil.getValue("db.password");
			// Generating the database connection
			conn = DriverManager.getConnection(url, userName, password);
			dBConnection = new DBConnection();
			dBConnection.setConn(conn);
			dBConnection.setStandalone(true);
		} catch (ClassNotFoundException e) {
			// Could not find the database driver
			e.printStackTrace();
		} catch (SQLException e) {
			// Could not connect to the database
			e.printStackTrace();
		}
		return dBConnection;
	}
	
	/**
	 * 
	 * @param natEdDBConnection
	 * @throws NatEdException
	 */
	public static void close(DBConnection dBConnection){
		dBConnection.close();
	}
	
}