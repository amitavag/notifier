package utility;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.Base64Decoder;
import com.oreilly.servlet.Base64Encoder;

public class Utility {
	public static String generateGUID(){
	 	UUID uuid = UUID.randomUUID();
        return uuid.toString();
	}
	
	public static String getValueFromCookie(HttpServletRequest req, String cookieName){
		Cookie[] cookies = null;
		Cookie cookie = null;
		String cookieValue = null;
		try {
			cookies = req.getCookies();
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					if (cookies[i].getName().equals(
							cookieName)) {
						cookie = cookies[i];
						break;
					}
				}
			}
			if (cookie != null) {
				cookieValue = cookie.getValue();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return cookieValue;
	}
	
	public static boolean isNullOrEmpty(String str) {
		if(str == null || "".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void setNewValueIntoCookie(HttpServletRequest req, HttpServletResponse res, String cookieName, String cookieValue){
		Cookie[] cookies = null;
		Cookie cookie1 = null;
		cookies = req.getCookies();
		Cookie cookie = null;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(cookieName)) {
					cookie1 = cookies[i];
					break;
				}
			}
		}
		if (cookie1 == null) {
			cookie = new Cookie(cookieName, cookieValue);
			cookie.setPath("/");
			cookie.setMaxAge(Integer.parseInt(PropertiesUtil.getValue("login.timeout")));
			res.addCookie(cookie);
		} else {
			cookie1.setValue(cookieValue);
			cookie1.setPath("/");
			cookie1.setMaxAge(Integer.parseInt(PropertiesUtil.getValue("login.timeout")));
			res.addCookie(cookie1);
		}
	}
	
	public static boolean validateEncryptedPassword(String passToken){
		String data = Base64Decoder.decode(passToken);
		if (data.indexOf(PropertiesUtil.getValue("user.pass")) > -1) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String encryptedPassword(String passToken){
		String data = Base64Encoder.encode(passToken + PropertiesUtil.getValue("user.pass"));
		return data;
	}
	
	public static void sendEmail(String asRecipient, String asSubject, String asBody){
		String emailPersonel = PropertiesUtil.getValue("email.alias");
		sendEmail(PropertiesUtil.getValue("from.email"), asRecipient,
				null, null, asSubject,
				asBody, null, null, emailPersonel, true);
	}
	
	private static void sendEmail(String asSender, String asRecipient,
			String asCcRecipient, String asBccRecipient, String asSubject,
			String asBody, ArrayList arAttachments, List replyTOLst, String emailPersonel, boolean htmlFlag) {
		try {
			
			Properties props = System.getProperties();
			props.put("mail.smtp.host", PropertiesUtil.getValue("email.smtp"));
			props.put("mail.smtp.auth", false);
			props.put("mail.smtp.socketFactory.fallback", true);
			props.put("mail.debug", true);
			Session mvSession = Session.getDefaultInstance(props, null);

			// Create a message;
			Message mvMimeMessage = new MimeMessage(mvSession);

			// Extracts the senders and adds them to the message.
			// Sender is a comma-separated list of e-mail addresses as per
			// RFC822.
			if (asSender != null && asSender.length() > 0) {
				InternetAddress[] marrvInternetAddresses = InternetAddress
						.parse(asSender);
				try {
					if (asSender.indexOf("@") > 0) {
						String fromMailAddress = asSender.split("@")[0];
						String fromMailPersonal = fromMailAddress + ".personal";
						fromMailPersonal = emailPersonel;
						// Setting Personal Name against From Mail ID
						if (fromMailPersonal != null)
							marrvInternetAddresses[0]
									.setPersonal(fromMailPersonal);
					}
				} catch (UnsupportedEncodingException e) {
				}
				mvMimeMessage.addFrom(marrvInternetAddresses);
			}
			// Extract the recipients and assign them to the message.
			// Recipient is a comma-separated list of e-mail addresses as per
			// RFC822.
			if (asRecipient != null && asRecipient.length() > 0) {
				InternetAddress[] marrvInternetAddresses = InternetAddress
						.parse(asRecipient);
				mvMimeMessage.addRecipients(Message.RecipientType.TO,
						marrvInternetAddresses);
			}
			// Extract the Cc-recipients and assign them to the message.
			// CcRecipient is a comma-separated list of e-mail addresses as per
			// RFC822.
			if (asCcRecipient != null && asCcRecipient.length() > 0) {
				InternetAddress[] marrvInternetAddresses = InternetAddress
						.parse(asCcRecipient);
				mvMimeMessage.addRecipients(Message.RecipientType.CC,
						marrvInternetAddresses);
			}
			// Extract the Bcc-recipients and assign them to the message.
			// BccRecipient is a comma-separated list of e-mail addresses as per
			// RFC822.
			if (asBccRecipient != null && asBccRecipient.length() > 0) {
				InternetAddress[] marrvInternetAddresses = InternetAddress
						.parse(asBccRecipient);
				mvMimeMessage.addRecipients(Message.RecipientType.BCC,
						marrvInternetAddresses);
			}
			// Set the subject field;
			System.out.println(asSubject);
			mvMimeMessage.setSubject(asSubject);

			// Create the Multipart to be added the parts to.
			Multipart mvMp = new MimeMultipart();
			// Create and fill the first message part.
			if (asBody != null) {
				MimeBodyPart mvBp = new MimeBodyPart();
				//mvBp.setContentMD5("text/html");
				if(true)
					mvBp.setContent(asBody, "text/html");
				else
					mvBp.setText(asBody);
				// Attach the part to the multipart.
				mvMp.addBodyPart(mvBp);
			}
			// Attach the files to the message;
			if (arAttachments != null && arAttachments.size() > 0) {
				for (int i = 0; i < arAttachments.size(); i++) {
					if (arAttachments.get(i) == null
							|| arAttachments.get(i).toString().length() == 0) {
						continue;
					}
					// Create and fill other message parts.
					MimeBodyPart mvBp = new MimeBodyPart();
					FileDataSource mvFds = new FileDataSource(arAttachments
							.get(i).toString());

					mvBp.setDataHandler(new DataHandler(mvFds));
					mvBp.setFileName(mvFds.getName());
					mvMp.addBodyPart(mvBp);
				}
			}
			// Add the Multipart to the message.
			//mvMimeMessage.setContent(asBody, "text/html");
			mvMimeMessage.setContent(mvMp);
			//mvMimeMessage.setContent(mvMp);

			// Set the Date: header.
			mvMimeMessage.setSentDate(new Date());
			if (replyTOLst != null && replyTOLst.size() > 0) {
				InternetAddress[] replyToAddress = new InternetAddress[replyTOLst
						.size()];
				for (int cnt = 0; cnt < replyTOLst.size(); cnt++) {
					replyToAddress[cnt] = new InternetAddress(
							(String) replyTOLst.get(cnt));
				}
				mvMimeMessage.setReplyTo(replyToAddress);
			}
			// Send the message;
			Transport.send(mvMimeMessage);
			System.out.println("Successfully sent the email: " + asRecipient);
		} catch (MessagingException messagingException) {
			messagingException.printStackTrace();
		} finally {
		}
	}
}