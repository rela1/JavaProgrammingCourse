package hr.fer.zemris.web.aplikacija5.servlets;

import hr.fer.zemris.web.aplikacija5.dao.jpa.DAOProvider;
import hr.fer.zemris.web.aplikacija5.model.BlogUser;
import hr.fer.zemris.web.aplikacija5.model.BlogUserFormRegistrationErrors;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/servleti/saveUser")
public class SaveUser extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		// provjeri korisnikov unos - Registriraj ili Odustani
		String type = req.getParameter("type");
		if (type != null && type.equals("Odustani")) {
			resp.sendRedirect(req.getServletContext().getContextPath()
					+ "/servleti/main");
			return;
		}
		BlogUserFormRegistrationErrors userForm = new BlogUserFormRegistrationErrors(
				req.getParameter("firstName"), req.getParameter("lastName"),
				req.getParameter("email"), req.getParameter("nickname"),
				req.getParameter("password"));
		checkUserForm(userForm);
		if (userForm.isCorrect()) {
			BlogUser user = new BlogUser();
			user.setEmail(userForm.getEmail());
			user.setFirstName(userForm.getFirstName());
			user.setLastName(userForm.getLastName());
			user.setNick(userForm.getNickname());
			user.setPasswordHash(createPasswordHash(userForm.getPassword()));
			DAOProvider.getDAO().addBlogUser(user);
			resp.sendRedirect(req.getServletContext().getContextPath()
					+ "/servleti/main");
		} else {
			List<BlogUser> users = DAOProvider.getDAO().getRegisteredUsers();
			req.setAttribute("users", users);
			req.setAttribute("user", userForm);
			req.getRequestDispatcher("/WEB-INF/pages/registration.jsp")
					.forward(req, resp);
		}
	}

	/**
	 * Kreira hashirani password za pohranjivanje u bazu.
	 * 
	 * @param password
	 *            originalni password
	 * @return string s hashiranim passwordom
	 */
	public static String createPasswordHash(String password) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException ignorable) {
		}
		md.update(password.getBytes(StandardCharsets.UTF_8));
		byte[] digestedPassword = md.digest();
		StringBuilder sb = new StringBuilder();
		for (int i = 0, length = digestedPassword.length - 4; i <= length; i += 4) {
			sb.append(String.format("%02X", digestedPassword[i]));
			sb.append(String.format("%02X", digestedPassword[i + 1]));
			sb.append(String.format("%02X", digestedPassword[i + 2]));
			sb.append(String.format("%02X", digestedPassword[i + 3]));
		}
		return sb.toString();
	}

	/**
	 * Provjerava jesu li sve stavke registracije ispravno unesene.
	 * 
	 * @param userForm
	 *            forma za registraciju korisnika s podacima i pogreškama
	 */
	private void checkUserForm(BlogUserFormRegistrationErrors userForm) {
		if (userForm.getFirstName().isEmpty()) {
			userForm.setError("firstName", "Ime mora biti navedeno!");
		} else if (userForm.getFirstName().length() > 30) {
			userForm.setError("firstName",
					"Najveća dozvoljena duljina imena je 30!");
		}
		if (userForm.getLastName().isEmpty()) {
			userForm.setError("lastName", "Prezime mora biti navedeno!");
		} else if (userForm.getLastName().length() > 40) {
			userForm.setError("lastName",
					"Najveća dozvoljena duljina prezimena je 40!");
		}
		if (userForm.getEmail().isEmpty()) {
			userForm.setError("email", "E-mail mora biti naveden!");
		} else if (userForm.getEmail().length() > 100) {
			userForm.setError("email",
					"Najveća dozvoljena duljina E-Maila je 100!");
		} else if (!userForm.getEmail().matches(EMAIL_REGEX)) {
			userForm.setError("email", "Neispravan E-Mail!");
		}
		if (userForm.getPassword().isEmpty()) {
			userForm.setError("password", "Lozinka mora biti navedena!");
		}
		if (userForm.getNickname().isEmpty()) {
			userForm.setError("nickname", "Korisničko ime mora biti navedeno!");
		} else if (!Charset.forName("US-ASCII").newEncoder()
				.canEncode(userForm.getNickname())) {
			userForm.setError("nickname",
					"Nadimak ne smije sadržavati nikakve posebne znakove!");
		} else if (DAOProvider.getDAO().getBlogUser(userForm.getNickname()) != null) {
			userForm.setError("nickname",
					"Korisnik s navedenim korisničkim imenom već postoji, molimo odaberite drugo!");
		}
	}
}
