package hr.fer.zemris.web.aplikacija5.servlets;

import hr.fer.zemris.web.aplikacija5.model.BlogUserFormRegistrationErrors;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet priprema registraciju novog korisnika.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/servleti/register")
public class Register extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BlogUserFormRegistrationErrors userForm = new BlogUserFormRegistrationErrors("", "", "", "", "");
		req.setAttribute("user", userForm);
		req.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(
				req, resp);
	}
}
