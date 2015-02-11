package hr.fer.zemris.web.ankete.servleti.glasanje;

import hr.fer.zemris.web.ankete.Poll;
import hr.fer.zemris.web.ankete.PollEntry;
import hr.fer.zemris.web.ankete.dao.DAO;
import hr.fer.zemris.web.ankete.dao.DAOProvider;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Pokreće proces glasanja za izabranu anketu.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/glasanje")
public class GlasanjeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// dohvati ID ankete
		Long pollID = null;
		try {
			pollID = Long.parseLong(req.getParameter("pollID"));
		} catch (Exception e) {
			req.setAttribute("message", "Pogrešan ID ankete!");
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp").forward(req, resp);
			return;
		}
		req.getRequestDispatcher("/init").include(req, resp);
		DAO provider = DAOProvider.getDao();
		// pohrani listu opcija za glasanje ankete u atribute
		List<PollEntry> pollEntries = null;
		Poll poll = null;
		try {
			pollEntries = provider.getPollEntries(pollID);
			poll = provider.getPollInfo(pollID);
			if (poll == null || pollEntries.size() == 0) {
				throw new Exception("Ne postoji zapis u bazi podataka!");
			}
		} catch (Exception e) {
			req.setAttribute("message", e.getMessage());
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp").forward(req, resp);
			return;
		}
		req.setAttribute("pollEntries", pollEntries);
		req.setAttribute("poll", poll);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(
				req, resp);
	}

}
