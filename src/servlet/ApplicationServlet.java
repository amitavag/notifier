package servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import activity.Notifier;
import utility.PropertiesUtil;
import utility.Utility;


public class ApplicationServlet extends BaseServlet {
	
	public void init(ServletConfig config) throws ServletException {
		System.out.println("############ Entered init() method of ApplicationServlet - NO SUPER.INIT() ################");		
	}

	/**
	 * Processes the common requests.
	 * 
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	private boolean process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		boolean checkRedirect = false;
		if (checkRedirect == false) {
			moduleProcess(req, res);
		}
		return checkRedirect;
	}

	/**
	 * Processes the particular servlet specific jobs.
	 * 
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	private void moduleProcess(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String view = null;
		boolean ajaxFlag = false;
		boolean sendRedirectFlag = false;
		boolean returnXmlType = false;
		boolean downloadFlag = false;
		boolean loginStatus = false;
		try {
			req.setAttribute("serverUrl", PropertiesUtil.getValue("server.url"));
			String cookieData = Utility.getValueFromCookie(req, "ntoken");
			if (!Utility.isNullOrEmpty(cookieData)) {
				loginStatus = true;
			}
			if (loginStatus && Utility.validateEncryptedPassword(cookieData)) {
				// Reinserting cookie and session will be valid for next 2 minutes
				String date = String.valueOf(System.currentTimeMillis());
				String str = Utility.encryptedPassword(date);
		        Utility.setNewValueIntoCookie(req, res, "ntoken", str);
				req.setAttribute("loginStatus", "Y");
				sendRedirectFlag = true;
				ajaxFlag = false;
				view = "/notifier/application";
				if ("saveEmail".equalsIgnoreCase(req.getParameter("action"))) {
					Notifier.saveEmail(req, res);
				} else if ("deleteEmail".equalsIgnoreCase(req.getParameter("action"))) {
					Notifier.deleteEmail(req, res);
				} else if ("saveNotification".equalsIgnoreCase(req.getParameter("action"))) {
					Notifier.saveEmailData(req, res);
				} else if ("deleteNotification".equalsIgnoreCase(req.getParameter("action"))) {
					Notifier.deleteEmailData(req, res);
				} else if ("invokeNotification".equalsIgnoreCase(req.getParameter("action"))) {
					view = Notifier.invokeNotification(req, res);
					sendRedirectFlag = false;
					ajaxFlag = true;
				} else if ("logOut".equalsIgnoreCase(req.getParameter("action"))) {
					Utility.setNewValueIntoCookie(req, res, "ntoken", "");
				} else {
					Notifier.getAllEmails(req, res);
					sendRedirectFlag = false;
					view = "/jsp/index.jsp";
				}
			 } else {
				if("login".equalsIgnoreCase(req.getParameter("action"))) {
					String inputEmail = req.getParameter("inputEmail");
					String inputPass = req.getParameter("inputPassword");
					if (inputEmail.equals(PropertiesUtil.getValue("user.name")) && inputPass.equals(PropertiesUtil.getValue("user.pass"))) {
						String date = String.valueOf(System.currentTimeMillis());
						String str = Utility.encryptedPassword(date);
				        Utility.setNewValueIntoCookie(req, res, "ntoken", str);
				        req.setAttribute("loginStatus", "Y");
					}
					sendRedirectFlag = true;
					ajaxFlag = false;
					view = "/notifier/application";
				} else {
					req.setAttribute("loginStatus", "N");
					view = "/jsp/index.jsp";
				}
			 }
			setNoCache(res);
			if(returnXmlType){
				createXMLView(view, req, res);
			} else {
				if (!downloadFlag) {
					if (ajaxFlag) {
						createAjaxView(res, view, req);
					} else {
						if (sendRedirectFlag) {
							res.sendRedirect(view);
						} else {
							forwardJspView(view, req, res);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();			
		} 
	}

	/**
	 * This method is accepting the request and passing it to the process
	 * method.
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @param res
	 *            HttpServletResponse
	 * @throws IOException
	 *             If an input or output exception occurred
	 * @throws ServletException
	 *             If an servlet exception occurred
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		process(req, res);
	}

	/**
	 * This method is accepting the request and passing it to the process
	 * method.
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @param res
	 *            HttpServletResponse
	 * @throws IOException
	 *             If an input or output exception occurred
	 * @throws ServletException
	 *             If an servlet exception occurred
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		process(req, res);
	}
}
