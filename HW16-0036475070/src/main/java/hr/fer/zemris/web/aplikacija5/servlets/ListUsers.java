package hr.fer.zemris.web.aplikacija5.servlets;

import hr.fer.zemris.web.aplikacija5.dao.jpa.DAOProvider;
import hr.fer.zemris.web.aplikacija5.model.BlogUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/servleti/main")
/**
 * Priprema podatke o autorima blogova spremne za ispisivanje.
 * 
 * @author Ivan Relić
 *
 */
public class ListUsers extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<BlogUser> users = DAOProvider.getDAO().getRegisteredUsers();
		if (users == null) {
			users = new ArrayList<>();
		}
		req.setAttribute("users", users);
		req.getRequestDispatcher("/WEB-INF/pages/authors.jsp").forward(req,
				resp);
	}

}
