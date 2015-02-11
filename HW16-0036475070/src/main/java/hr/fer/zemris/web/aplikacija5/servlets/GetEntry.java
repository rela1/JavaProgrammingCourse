package hr.fer.zemris.web.aplikacija5.servlets;

import hr.fer.zemris.web.aplikacija5.dao.jpa.DAOProvider;

import java.io.IOException;
import java.util.List;

import hr.fer.zemris.web.aplikacija5.model.BlogComment;
import hr.fer.zemris.web.aplikacija5.model.BlogEntry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Priprema podatke potrebne za prikaz jednog zapisa bloga i njegovih komentara.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/servleti/blogEntry")
public class GetEntry extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String nick = (String) req.getAttribute("nick");
		if (nick == null) {
			req.setAttribute("error", "Nepoznat autor zapisa bloga!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
			return;
		}
		Long id = null;
		try {
			id = Long.parseLong((String) req.getAttribute("id"));
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
		List<BlogComment> comments = entry.getComments();
		req.setAttribute("entry", entry);
		req.setAttribute("comments", comments);
		req.getRequestDispatcher("/WEB-INF/pages/blogEntry.jsp").forward(req,
				resp);
	}

}
