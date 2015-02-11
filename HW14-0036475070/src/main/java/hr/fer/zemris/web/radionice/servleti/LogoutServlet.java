package hr.fer.zemris.web.radionice.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Modelira akciju koju je potrebno izvršiti prilikom odjave korisnika.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Metoda poništava trenutnu sesiju i tako uklanja sve parametre o
	 * prijavljenim korisnicima.
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// invalidiraj trenutnu sesiju i napravi redirect na listaj
		req.getSession().invalidate();
		resp.sendRedirect(req.getServletContext().getContextPath() + "/listaj");
	}

}
