package hr.fer.zemris.web.aplikacija5.servlets;

import hr.fer.zemris.web.aplikacija5.dao.jpa.DAOProvider;
import hr.fer.zemris.web.aplikacija5.model.BlogEntry;
import hr.fer.zemris.web.aplikacija5.model.BlogEntryFormErrors;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Priprema podatke za editiranje zapisa bloga.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/servleti/edit")
public class EditEntry extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String nick = req.getParameter("nick");
		if (nick == null) {
			req.setAttribute("error", "Nepoznat autor zapisa bloga!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
			return;
		}
		if (!nick.equals(req.getSession().getAttribute("current.user.nick"))) {
			req.setAttribute("error", "Neautorizirani pristup stranici!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
			return;
		}
		Long id = null;
		try {
			id = Long.parseLong(req.getParameter("id"));
		} catch (Exception e) {
			id = null;
		}
		if (id == null) {
			req.setAttribute("error", "Nepoznat zapis bloga!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
			return;
		}
		BlogEntry entry = DAOProvider.getDAO().getUserBlogEntry(nick, id);
		if (entry == null) {
			req.setAttribute("error",
					"Ne postoji traženi zapis bloga za traženog korisnika!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
			return;
		}
		BlogEntryFormErrors entryForm = new BlogEntryFormErrors(
				entry.getTitle(), entry.getText(), entry.getId().toString());
		req.setAttribute("entry", entryForm);
		req.setAttribute("nick", nick);
		req.getRequestDispatcher("/WEB-INF/pages/blogEntryForm.jsp").forward(
				req, resp);
	}
}
