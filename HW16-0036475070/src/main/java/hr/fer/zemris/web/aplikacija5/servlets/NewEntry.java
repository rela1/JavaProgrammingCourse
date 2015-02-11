package hr.fer.zemris.web.aplikacija5.servlets;

import hr.fer.zemris.web.aplikacija5.model.BlogEntryFormErrors;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Priprema podatke za kreiranje novog zapisa bloga.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/servleti/newEntry")
public class NewEntry extends HttpServlet {

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
		if (!nick.equals(req.getSession().getAttribute("current.user.nick"))) {
			req.setAttribute("error", "Neautorizirani pristup stranici!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
			return;
		}
		BlogEntryFormErrors entryForm = new BlogEntryFormErrors("", "", "");
		req.setAttribute("entry", entryForm);
		req.getRequestDispatcher("/WEB-INF/pages/blogEntryForm.jsp").forward(
				req, resp);
	}

}
