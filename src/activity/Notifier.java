package activity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Util;

import datatransfer.EmailDataTO;
import datatransfer.EmailTO;
import utility.DBConnection;
import utility.DBConnectionManager;
import utility.Utility;

public class Notifier {
	public static String invokeNotification (HttpServletRequest req, HttpServletResponse res) {
		Connection connection = null;
		DBConnection dBConnection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		EmailDataTO emailDataTO = null;
		String id = req.getParameter("id");
		String timeSpan = req.getParameter("timeSpan").replace("[", "").replace("]", "");
		String[] timespan = timeSpan.split(",");
		boolean dataIdentified = false;
		String difference = "";
		String message = "";
		try  {
			dBConnection = DBConnectionManager.getConnection();
			connection = dBConnection.getConn();
			
			for (int i=0; i<timespan.length; i++) {
				difference = timespan[i];
				preparedStatement = connection.prepareStatement("select * from notifier_email_data where id = ? AND date = DATE_ADD(CURRENT_DATE(), INTERVAL ? DAY)");
				preparedStatement.setString(1, id);
				preparedStatement.setString(2, difference);
				resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {
					dataIdentified = true;
					emailDataTO = new EmailDataTO();
					emailDataTO.setId(resultSet.getString("id"));
					emailDataTO.setBody(resultSet.getString("body"));
					emailDataTO.setTitle(resultSet.getString("title"));
					emailDataTO.setSubject(resultSet.getString("subject"));
					emailDataTO.setEmailTo(resultSet.getString("email_to"));
					emailDataTO.setDate(resultSet.getString("date"));
				}
				if(resultSet!=null)
					resultSet.close();
				if(preparedStatement!=null)
					preparedStatement.close();	
				if (dataIdentified) {
					break;
				}
			}
			if(connection!=null)
				connection.close();
			if(dBConnection!=null)
				dBConnection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (dataIdentified) {
			message = "Found email with " + difference + " days window with ID " + id + ". Email sent successfully to " + emailDataTO.getEmailTo().replaceAll(",", ", "); 
			// Send email
			Utility.sendEmail(emailDataTO.getEmailTo(), emailDataTO.getSubject() + " [" + difference + " DAYS to GO]", emailDataTO.getBody().replaceAll("\n", "<br/>"));
		} else {
			message = "No email with ID " + id + ". No emails sent.";
		}
		System.out.println(message);
		return message;
	}
	
	// Save Emails emtry
	public static void saveEmail (HttpServletRequest req, HttpServletResponse res) {
		String email = req.getParameter("newEmail");
		List emails = getAllEmails(req, res);
		boolean notPresent = true;
		for (int i = 0; i < emails.size(); i++) {
			EmailTO emailTO = (EmailTO) emails.get(i);
			if (emailTO.getEmail().equalsIgnoreCase(email)) {
				// Email already present so do not insert
				notPresent = false;
			}
		}
		try {
			if (notPresent) {
				DBConnection dBConnection = DBConnectionManager.getConnection();
				Connection connection = dBConnection.getConn();
				PreparedStatement preparedStatement = connection.prepareStatement("insert into notifier_emails (email) values (?)");
				preparedStatement.setString(1, email);
				preparedStatement.execute();
				if(preparedStatement!=null)
					preparedStatement.close();
				if(connection!=null)
					connection.close();
				if(dBConnection!=null)
					connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Delete Emails entry
	public static void deleteEmail (HttpServletRequest req, HttpServletResponse res) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		DBConnection dBConnection = null;
		String emailID = req.getParameter("emailID");
		System.out.println("Deleting email with ID " + emailID);
		try {
			dBConnection = DBConnectionManager.getConnection();
			connection = dBConnection.getConn();
			preparedStatement = connection.prepareStatement("delete from notifier_emails where id = ?");
			preparedStatement.setString(1, emailID);
			preparedStatement.execute();
			if (preparedStatement != null)
				preparedStatement.close();
			if (connection != null)
				connection.close();
			if (dBConnection != null)
				dBConnection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Get All Emails entry
	public static List<EmailTO> getAllEmails (HttpServletRequest req, HttpServletResponse res) {
		Connection connection = null;
		DBConnection dBConnection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List emails = null;
		EmailTO emailTO = null;
		String allEmails = "";
		try  {
			dBConnection = DBConnectionManager.getConnection();
			connection = dBConnection.getConn();
			preparedStatement = connection.prepareStatement("select * from notifier_emails");
			resultSet = preparedStatement.executeQuery();
			emails = new ArrayList<EmailTO>();
			while (resultSet.next()) {
				emailTO = new EmailTO();
				emailTO.setEmail(resultSet.getString("email"));
				emailTO.setId(resultSet.getString("id"));
				emails.add(emailTO);
				allEmails += resultSet.getString("email") + ",";
			}
			req.setAttribute("emails", emails);
			if (emails != null && emails.size() > 0)
				req.setAttribute("allEmails", allEmails.substring(0, allEmails.length()-1));
			if(resultSet!=null)
				resultSet.close();
			if(preparedStatement!=null)
				preparedStatement.close();	
			if(connection!=null)
				connection.close();
			if(dBConnection!=null)
				dBConnection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		getAllEmailData(req, res);
		return emails;
	}
	
	// Save email_data emtry
	public static void saveEmailData (HttpServletRequest req, HttpServletResponse res) {
		String emailDataID = req.getParameter("emailDataID");
		try {
			if (Utility.isNullOrEmpty(emailDataID)) {
				// Insert
				DBConnection dBConnection = DBConnectionManager.getConnection();
				Connection connection = dBConnection.getConn();
				PreparedStatement preparedStatement = connection.prepareStatement("insert into notifier_email_data (title, subject, date, body, email_to) values (?, ?, ?, ?, ?)");
				preparedStatement.setString(1, req.getParameter("title"));
				preparedStatement.setString(2, req.getParameter("subject"));
				preparedStatement.setString(3, req.getParameter("date"));
				preparedStatement.setString(4, req.getParameter("body"));
				preparedStatement.setString(5, req.getParameter("emailTo"));
				preparedStatement.execute();
				if(preparedStatement!=null)
					preparedStatement.close();
				if(connection!=null)
					connection.close();
				if(dBConnection!=null)
					connection.close();
			} else {
				// Update
				DBConnection dBConnection = DBConnectionManager.getConnection();
				Connection connection = dBConnection.getConn();
				PreparedStatement preparedStatement = connection.prepareStatement("update notifier_email_data set title = ?, subject = ?, date = ?, body = ?, email_to = ? where id = ?");
				preparedStatement.setString(1, req.getParameter("title"));
				preparedStatement.setString(2, req.getParameter("subject"));
				preparedStatement.setString(3, req.getParameter("date"));
				preparedStatement.setString(4, req.getParameter("body"));
				preparedStatement.setString(5, req.getParameter("emailTo"));
				preparedStatement.setString(6, req.getParameter("emailDataID"));
				preparedStatement.executeUpdate();
				if(preparedStatement!=null)
					preparedStatement.close();
				if(connection!=null)
					connection.close();
				if(dBConnection!=null)
					connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Delete Email Data entry
	public static void deleteEmailData (HttpServletRequest req, HttpServletResponse res) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		DBConnection dBConnection = null;
		String emailDataID = req.getParameter("emailDataID");
		System.out.println("Deleting Email Data with ID " + emailDataID);
		try {
			dBConnection = DBConnectionManager.getConnection();
			connection = dBConnection.getConn();
			preparedStatement = connection.prepareStatement("delete from notifier_email_data where id = ?");
			preparedStatement.setString(1, emailDataID);
			preparedStatement.execute();
			if (preparedStatement != null)
				preparedStatement.close();
			if (connection != null)
				connection.close();
			if (dBConnection != null)
				dBConnection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Get All Email Data entry
	public static List<EmailDataTO> getAllEmailData (HttpServletRequest req, HttpServletResponse res) {
		Connection connection = null;
		DBConnection dBConnection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List emailData = null;
		EmailDataTO emailDataTO = null;
		try  {
			dBConnection = DBConnectionManager.getConnection();
			connection = dBConnection.getConn();
			preparedStatement = connection.prepareStatement("select * from notifier_email_data");
			resultSet = preparedStatement.executeQuery();
			emailData = new ArrayList<EmailDataTO>();
			while (resultSet.next()) {
				emailDataTO = new EmailDataTO();
				emailDataTO.setId(resultSet.getString("id"));
				emailDataTO.setBody(resultSet.getString("body"));
				emailDataTO.setTitle(resultSet.getString("title"));
				emailDataTO.setSubject(resultSet.getString("subject"));
				emailDataTO.setEmailTo(resultSet.getString("email_to"));
				emailDataTO.setDate(resultSet.getString("date"));
				emailData.add(emailDataTO);
			}
			req.setAttribute("emailData", emailData);
			if(resultSet!=null)
				resultSet.close();
			if(preparedStatement!=null)
				preparedStatement.close();	
			if(connection!=null)
				connection.close();
			if(dBConnection!=null)
				dBConnection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return emailData;
	}
	
}
