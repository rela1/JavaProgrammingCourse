package hr.fer.zemris.web.aplikacija5.servlets;

import hr.fer.zemris.web.aplikacija5.dao.DAO;
import hr.fer.zemris.web.aplikacija5.dao.jpa.DAOProvider;
import hr.fer.zemris.web.aplikacija5.model.BlogEntry;
import hr.fer.zemris.web.aplikacija5.model.BlogEntryFormErrors;
import hr.fer.zemris.web.aplikacija5.model.BlogUser;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Obavlja akcije potrebne za pohranjivanje blog entrya.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/servleti/saveBlogEntry")
public class SaveEntry extends HttpServlet {

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
		String nick = req.getParameter("nick");
		// provjeri korisnikov unos - Pohrani ili Odustani
		String type = req.getParameter("type");
		if (type != null && type.equals("Odustani")) {
			String id = req.getParameter("id");
			if (id == null) {
				id = "";
			}
			resp.sendRedirect(req.getServletContext().getContextPath()
					+ "/servleti/author/" + nick + "/" + id);
			return;
		}
		BlogEntryFormErrors entryForm = new BlogEntryFormErrors(
				req.getParameter("title"), req.getParameter("text"),
				req.getParameter("id"));
		entryForm.validate();
		// ako ima pogrešaka vrati se natrag na ispunjavanje u formi
		if (!entryForm.isCorrect()) {
			req.setAttribute("entry", entryForm);
			req.setAttribute("nick", nick);
			req.getRequestDispatcher("/WEB-INF/pages/blogEntryForm.jsp")
					.forward(req, resp);
		} else {
			DAO dao = DAOProvider.getDAO();
			Long id = entryForm.getLongId();
			// ako ID zapisa nije zadan, riječ je o novom zapisu pa ga napravi
			if (id == null) {
				BlogUser user = dao.getBlogUser(nick);
				// ako nick nije zadan ili ne postoji, ispiši pogrešku
				if (nick == null || user == null) {
					req.setAttribute("error", "Nepostojeći autor bloga!");
					req.getRequestDispatcher("/WEB-INF/pages/error.jsp")
							.forward(req, resp);
					return;
				}
				createBlogEntry(entryForm, dao, user);
				// inače, riječ je o updateanju starog zapisa
			} else {
				BlogEntry entry = dao.updateBlogEntry(id, entryForm.getTitle(),
						entryForm.getText());
				nick = entry.getCreator().getNick();
			}
			resp.sendRedirect(req.getServletContext().getContextPath()
					+ "/servleti/author/" + nick);
		}
	}

	/**
	 * Kreira novi zapis za blog.
	 * 
	 * @param entryForm
	 *            forma za ispunjavanje zapisa bloga
	 * @param dao
	 *            dao provider za pristup perzistencijskom sloju
	 * @param user
	 *            korisnik koji kreira novi zapis
	 */
	private void createBlogEntry(BlogEntryFormErrors entryForm, DAO dao,
			BlogUser user) {
		BlogEntry blogEntry = new BlogEntry();
		Date timestamp = new Date();
		blogEntry.setCreatedAt(timestamp);
		blogEntry.setLastModifiedAt(timestamp);
		blogEntry.setCreator(user);
		blogEntry.setTitle(entryForm.getTitle());
		blogEntry.setText(entryForm.getText());
		blogEntry.setId(entryForm.getLongId());
		dao.addBlogEntry(blogEntry);
	}
}
