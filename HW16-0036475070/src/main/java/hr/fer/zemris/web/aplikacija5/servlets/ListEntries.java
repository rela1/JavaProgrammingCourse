package hr.fer.zemris.web.aplikacija5.servlets;

import hr.fer.zemris.web.aplikacija5.dao.jpa.DAOProvider;
import hr.fer.zemris.web.aplikacija5.model.BlogEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Priprema podatke za islistavanje svih zapisa bloga nekog korisnika.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/servleti/listEntries")
public class ListEntries extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String nick = (String) req.getAttribute("nick");
		if (nick == null) {
			req.setAttribute("error", "Nepostojeći autor bloga!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
			return;
		}
		if (DAOProvider.getDAO().getBlogUser(nick) == null) {
			req.setAttribute("error", "Autor " + nick + " nije registriran!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
			return;
		}
		List<BlogEntry> entries = DAOProvider.getDAO().getUserBlogEntries(nick);
		if (entries == null) {
			entries = new ArrayList<>();
		}
		req.setAttribute("blogs", entries);
		req.getRequestDispatcher("/WEB-INF/pages/blogEntries.jsp").forward(req,
				resp);
	}

}
