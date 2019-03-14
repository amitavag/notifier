package utility;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {

	private Connection conn = null;
	private boolean standalone = false;


	public void commit() {
		try {
			conn.commit();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();			
		}
	}


	public void rollback() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.rollback();
			}
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}


	public void close() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();			
		}
	}


	public Connection getConn() {
		return conn;
	}


	public void setConn(Connection conn) {
		try {
			// conn.setAutoCommit(false);
			this.conn = conn;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void beginTransaction() {
		try {
			conn.setAutoCommit(false);
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}

	public boolean isStandalone() {
		return standalone;
	}

	public void setStandalone(boolean standalone) {
		this.standalone = standalone;
	}
}
