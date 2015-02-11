package hr.fer.zemris.web.aplikacija5.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Glavni server koji vrši redirekciju.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/index.jsp")
public class Main extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect(req.getServletContext().getContextPath()
				+ "/servleti/main");
	}
}
