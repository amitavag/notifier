package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utility.FileResourceLoader;


public class BaseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5774990626052540313L;
	
	/**
	 * Initializing the BaseServlet.
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			System.out.println("BASE SERVLET INIT");
			FileResourceLoader resourceLoader = new FileResourceLoader();
			resourceLoader.loadResource(config);
		} catch (Exception e) {
			e.printStackTrace();			
		} 
	}
	

	/**
	 * Set the browser cache as false.
	 * 
	 * @param res
	 */
	protected void setNoCache(HttpServletResponse res) {
		res.setHeader("Pragma", "No-cache");
		res.setHeader("Cache-Control", "no-cache,no-store,must-revalidate,max-age=0");
		res.setDateHeader("Expires", 1);		
	}

	/**
	 * This method is used to handle the AJAX Requests.
	 * 
	 * @param res
	 * @param response
	 * @param req
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void createAjaxView(HttpServletResponse res, String response,
			HttpServletRequest req) throws ServletException, IOException {
		PrintWriter pr = null;
		try {
			req.setAttribute("serverReturnPageType", "ajax");
			pr = res.getWriter();
			pr.write(response);
			pr.flush();
		} finally {
			pr.close();			
		}
	}
	
	/**
	 * This method is used to return the XML form of data
	 * 
	 * @param view
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void createXMLView(String view, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter pr = null;
		try {
			res.setContentType("text/xml");
			pr = res.getWriter();
			pr.println(view);
			pr.flush();
		} finally {
			pr.close();
		}	
	}	

	/**
	 * This method is used by all internal method needing to do an include.
	 * 
	 * @param view
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void includeJspView(String view, HttpServletRequest req,
			HttpServletResponse res) throws ServletException, IOException {

		RequestDispatcher rd = req.getRequestDispatcher(view);
		if (rd == null) {
			return;
		}
		rd.include(req, res);
	}

	/**
	 * This method is used by all internal method needing to do a forward.
	 * 
	 * @param view
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void forwardJspView(String view, HttpServletRequest req,
		HttpServletResponse res) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher(view);
		if (rd == null) {
			return;
		}
		req.setAttribute("serverReturnPageType", "jsp");
		rd.forward(req, res);
	}
}