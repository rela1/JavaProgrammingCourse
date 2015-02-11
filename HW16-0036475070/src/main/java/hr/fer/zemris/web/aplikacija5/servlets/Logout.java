package hr.fer.zemris.web.aplikacija5.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Obavlja postupak odjave korisnika iz sustava.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/servleti/logout")
public class Logout extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getSession().invalidate();
		resp.sendRedirect(req.getServletContext().getContextPath()
				+ "/servleti/main");
	}

}
