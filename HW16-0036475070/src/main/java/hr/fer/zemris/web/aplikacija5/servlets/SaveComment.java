package hr.fer.zemris.web.aplikacija5.servlets;

import hr.fer.zemris.web.aplikacija5.dao.jpa.DAOProvider;
import hr.fer.zemris.web.aplikacija5.model.BlogCommentFormErrors;
import hr.fer.zemris.web.aplikacija5.model.BlogEntry;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Obavlja potrebne operacije za pohranjivanje komentara.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/servleti/saveComment")
public class SaveComment extends HttpServlet {

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
		String id = req.getParameter("id");
		BlogCommentFormErrors commentForm = new BlogCommentFormErrors(
				req.getParameter("email"), req.getParameter("message"));
		commentForm.validate();
		if (!commentForm.isCorrect()) {
			BlogEntry entry = DAOProvider.getDAO().getBlogEntry(
					Long.parseLong(id));
			req.setAttribute("entry", entry);
			req.setAttribute("comments", entry.getComments());
			req.setAttribute("comment", commentForm);
			req.getRequestDispatcher("/WEB-INF/pages/blogEntry.jsp").forward(
					req, resp);
		} else {
			BlogEntry entry = DAOProvider.getDAO().addComment(
					Long.parseLong(id), commentForm.getEmail(),
					commentForm.getMessage());
			resp.sendRedirect(req.getServletContext().getContextPath()
					+ "/servleti/author/" + entry.getCreator().getNick() + "/"
					+ entry.getId().toString());
		}
	}
}
