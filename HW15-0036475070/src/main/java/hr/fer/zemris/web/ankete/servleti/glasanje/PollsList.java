package hr.fer.zemris.web.ankete.servleti.glasanje;

import hr.fer.zemris.web.ankete.Poll;
import hr.fer.zemris.web.ankete.dao.DAOProvider;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Predstavlja programski sloj za dohvaćanje svih anketa i preusmjeravanje
 * dohvaćenih podataka na iscrtavanje.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet({"/index.html", ""})
public class PollsList extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/init").include(req, resp);
		List<Poll> polls = DAOProvider.getDao().getPolls();
		req.setAttribute("polls", polls);
		req.getRequestDispatcher("/WEB-INF/pages/anketeIndex.jsp").forward(req,
				resp);
	}
}
