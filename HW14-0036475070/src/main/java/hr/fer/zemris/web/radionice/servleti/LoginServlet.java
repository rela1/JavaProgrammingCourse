package hr.fer.zemris.web.radionice.servleti;

import hr.fer.zemris.web.radionice.User;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Modelira akciju koju je potrebno izvršiti prilikom prijave korisnika.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Provjerava ima li parametar username, ako nema, nudi korisniku login
	 * formu, ako ima provjerava je li unesen dobar username i password.
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			obradiZahtjev(req, resp);
		} catch (Exception e) {
			req.setAttribute("poruka",
					"Pogreška prilikom obrade akcije prijave!");
			req.getRequestDispatcher("/WEB-INF/pages/greska.jsp").forward(req,
					resp);
		}
	}

	/**
	 * Provjerava ima li parametar username, ako nema, nudi korisniku login
	 * formu, ako ima provjerava je li unesen dobar username i password.
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			obradiZahtjev(req, resp);
		} catch (Exception e) {
			req.setAttribute("poruka",
					"Pogreška prilikom obrade akcije prijave!");
			req.getRequestDispatcher("/WEB-INF/pages/greska.jsp").forward(req,
					resp);
		}
	}

	/**
	 * Provjerava je li predan dobar username i password iz login forme.
	 * 
	 * @param username
	 *            username korisnika
	 * @param password
	 *            password korisnika
	 * @return user ako je sve u redu, null inače
	 */
	private User provjeri(String username, String password) {
		if (username.equals("aante")) {
			if (password != null && password.equals("tajna")) {
				return new User("aante", "tajna", "Ante", "Anić");
			}
		}
		return null;
	}

	/**
	 * Obrađuje login zahtjev.
	 * 
	 * @param req
	 *            request
	 * @param resp
	 *            response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void obradiZahtjev(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		// ako postoji parametar metoda i ako je odustani, pošalji redirect na
		// listaj
		String metoda = req.getParameter("metoda");
		if (metoda != null && metoda.equals("Odustani")) {
			resp.sendRedirect(req.getServletContext().getContextPath()
					+ "/listaj");
			return;
		}
		// ako postoji parametar username, obavi potrebne provjere i provedi
		// moguće akcije
		String username = req.getParameter("username");
		if (username != null) {
			String password = req.getParameter("password");
			User korisnik = provjeri(username, password);
			if (korisnik != null) {
				req.getSession().setAttribute("current.user", korisnik);
				resp.sendRedirect(req.getServletContext().getContextPath()
						+ "/listaj");
				return;
			} else {
				req.setAttribute("greska", "Pogrešan username ili password.");
				req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(
						req, resp);
				return;
			}
		} else {
			req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req,
					resp);
			return;
		}
	}

}
