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
 * Obavlja proces glasanja po primljenim id-ovima iz parametara.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Long pollID;
		Long id;
		try {
			pollID = Long.parseLong(req.getParameter("pollID"));
			id = Long.parseLong(req.getParameter("id"));
		} catch (Exception e) {
			req.setAttribute("message", "Pogrešan ID ankete ili opcije ankete!");
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp")
					.forward(req, resp);
			return;
		}
		req.getRequestDispatcher("/init").include(req, resp);
		DAO provider = DAOProvider.getDao();
		Poll poll;
		List<PollEntry> pollEntries;
		try {
			provider.updatePollEntry(pollID, id);
			poll = provider.getPollInfo(pollID);
			pollEntries = provider.getPollEntries(pollID);
			if (poll == null || pollEntries.size() == 0) {
				throw new Exception("Ne postoji zapis u bazi podataka!");
			}
		} catch (Exception e) {
			req.setAttribute("message", e.getMessage());
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp")
					.forward(req, resp);
			return;
		}
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati?pollID="
				+ pollID.toString());
	}
}
