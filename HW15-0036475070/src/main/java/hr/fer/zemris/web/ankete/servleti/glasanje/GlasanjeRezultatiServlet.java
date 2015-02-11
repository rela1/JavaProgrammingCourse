package hr.fer.zemris.web.ankete.servleti.glasanje;

import hr.fer.zemris.web.ankete.PollEntry;
import hr.fer.zemris.web.ankete.dao.DAO;
import hr.fer.zemris.web.ankete.dao.DAOProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Obrađuje podatke ankete i priprema ih za prikaz.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Long pollID;
		try {
			pollID = Long.parseLong(req.getParameter("pollID"));
		} catch (Exception e) {
			req.setAttribute("message", "Pogrešan ID ankete!");
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp")
					.forward(req, resp);
			return;
		}
		req.getRequestDispatcher("/init").include(req, resp);
		DAO provider = DAOProvider.getDao();
		List<PollEntry> pollEntries;
		try {
			pollEntries = provider.getPollEntries(pollID);
			if (pollEntries.size() == 0) {
				throw new Exception("Ne postoji zapis u bazi podataka!");
			}
		} catch (Exception e) {
			req.setAttribute("message", e.getMessage());
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp")
					.forward(req, resp);
			return;
		}
		try {
			Collections.sort(pollEntries);
			req.setAttribute("pollEntries", pollEntries);
			req.setAttribute("maxPollEntries", createMaxVotesList(pollEntries));
			req.setAttribute("pollID", pollID.toString());
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(
					req, resp);
			return;
		} catch (Exception e) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeFileError.jsp")
					.forward(req, resp);
			return;
		}
	}

	/**
	 * Kreira listu u kojoj su samo one stavke ankete koje imaju najveći broj
	 * glasova.
	 * 
	 * @param pollEntries
	 *            lista svih stavki ankete
	 * @return lista stavki ankete s najvećim brojem glasova
	 */
	private List<PollEntry> createMaxVotesList(List<PollEntry> pollEntries) {
		List<PollEntry> maxPollEntry = new ArrayList<PollEntry>();
		long maxVotes = 0;
		for (PollEntry entry : pollEntries) {
			maxVotes = Math.max(maxVotes, entry.getVotesCount().longValue());
		}
		for (PollEntry entry : pollEntries) {
			if (entry.getVotesCount().longValue() == maxVotes) {
				maxPollEntry.add(entry);
			}
		}
		return maxPollEntry;
	}
}
