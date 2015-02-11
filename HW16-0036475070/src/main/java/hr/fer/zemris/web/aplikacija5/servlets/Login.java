package hr.fer.zemris.web.aplikacija5.servlets;

import hr.fer.zemris.web.aplikacija5.dao.jpa.DAOProvider;
import hr.fer.zemris.web.aplikacija5.model.BlogUser;
import hr.fer.zemris.web.aplikacija5.model.BlogUserLoginFormErrors;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/servleti/login")
/**
 * Obrađuje login zahtjev korisnika.
 * 
 * @author Ivan Relić
 *
 */
public class Login extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		BlogUserLoginFormErrors userForm = new BlogUserLoginFormErrors(
				req.getParameter("username"), req.getParameter("password"));
		BlogUser user = DAOProvider.getDAO()
				.getBlogUser(userForm.getNickname());
		if (!checkLogin(userForm, user)) {
			req.setAttribute("user", userForm);
			req.getRequestDispatcher("/servleti/main").forward(req, resp);
		} else {
			req.getSession().setAttribute("current.user.id", user.getId());
			req.getSession().setAttribute("current.user.fn",
					user.getFirstName());
			req.getSession()
					.setAttribute("current.user.ln", user.getLastName());
			req.getSession().setAttribute("current.user.nick", user.getNick());
			req.getRequestDispatcher("/servleti/main").forward(req, resp);
		}
	}

	/**
	 * Provjerava je li korisnik obavio login s valjanim podacima - korisničkim
	 * imenom i lozinkom. Ako je sve u redu, metoda vraća true, inače false.
	 * 
	 * @param userForm
	 *            user forma koja se koristi za login
	 * @param podaci
	 *            o korisniku iz baze podataka
	 * @return true ako je login uspješno obavljen, false inače
	 */
	private boolean checkLogin(BlogUserLoginFormErrors userForm, BlogUser user) {
		if (userForm.getNickname().isEmpty()) {
			userForm.setError("nickname", "Korisničko ime mora biti navedeno!");
		} else if (user == null) {
			userForm.setError("nickname",
					"Korisnik s navedenim korisničkim imenom ne postoji!");
		} else if (user != null) {
			if (!user.getPasswordHash().equals(
					SaveUser.createPasswordHash(userForm.getPassword()))) {
				userForm.setError("password", "Neispravna lozinka!");
			}
		}
		if (userForm.isCorrect()) {
			return true;
		} else {
			return false;
		}
	}
}
