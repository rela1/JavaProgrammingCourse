package hr.fer.zemris.web.aplikacija5.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Redirekcijski servlet koji parsira path koji je korisnik zatražio i forwarda
 * ga na potrebnu adresu.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/servleti/author/*")
public class AuthorRedirector extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if (pathInfo == null) {
			resp.sendRedirect(req.getServletContext().getContextPath()
					+ "/servleti/main");
			return;
		}
		// ukloni prvi znak '/'
		pathInfo = pathInfo.substring(1);
		String[] pathElements = pathInfo.split("/");
		// ako je samo jedan element nakon '/author/', odvedi korisnika na tu
		// stranicu autora
		if (pathElements.length == 1) {
			req.setAttribute("nick", pathElements[0]);
			req.getRequestDispatcher("/servleti/listEntries")
					.forward(req, resp);
		} else if (pathElements.length == 2) {
			// ako je duljina dva, prvi atribut je nick
			req.setAttribute("nick", pathElements[0]);
			if (pathElements[1].equalsIgnoreCase("new")) {
				req.getRequestDispatcher("/servleti/newEntry").forward(req,
						resp);
				return;
			} else {
				req.setAttribute("id", pathElements[1]);
				req.getRequestDispatcher("/servleti/blogEntry").forward(req,
						resp);
			}
		} else if (pathElements.length == 3) {
			// path s tri elementa dolazi u obzir samo ako je zadnji element
			// edit
			if (!pathElements[2].equalsIgnoreCase("edit")) {
				req.setAttribute("error", "Nepoznata kombinacija adrese!");
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(
						req, resp);
				return;
			}
			req.getRequestDispatcher(
					"/servleti/edit?nick=" + pathElements[0] + "&id="
							+ pathElements[1]).forward(req, resp);
		} else {
			req.setAttribute("error", "Nepoznata kombinacija adrese!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
		}
	}

}
