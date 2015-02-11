package hr.fer.zemris.web.ankete.servleti.glasanje;

import hr.fer.zemris.web.ankete.Poll;
import hr.fer.zemris.web.ankete.PollEntry;
import hr.fer.zemris.web.ankete.dao.DAO;
import hr.fer.zemris.web.ankete.dao.DAOProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Prilikom pokretanja ovog servleta tablice Polls i PollOptions se pune sa 2
 * moguća seta glasanja ako ne postoje setovi glasanja za bend i operacijski
 * sustav.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/init")
public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Dohvaća sve postojeće ankete i ako ne pronađe anketu za bendove i anketu
	 * za korištenje operacijskih sustava.
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<Poll> polls;
		DAO provider = DAOProvider.getDao();
		try {
			polls = provider.getPolls();
		} catch (Exception e) {
			req.setAttribute("message", e.getMessage());
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp")
					.forward(req, resp);
			return;
		}
		boolean bandTableExists = false;
		boolean OSTableExists = false;
		// ako postoje neki zapisi o anketama, provjeri postoji li zapis s
		// imenom Glasanje za omiljeni bend: i Glasanje za omiljeni operacijski
		// sustav:
		if (polls != null && polls.size() != 0) {
			for (Poll poll : polls) {
				if (poll.getTitle().equals("Glasanje za omiljeni bend")) {
					bandTableExists = true;
				}
				if (poll.getTitle().equals(
						"Glasanje za omiljeni operacijski sustav")) {
					OSTableExists = true;
				}
			}
		}
		if (!bandTableExists) {
			List<PollEntry> entries = createBandEntries();
			try {
				provider.createNewPoll(
						"Glasanje za omiljeni bend",
						"Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!",
						entries);
			} catch (Exception e) {
				req.setAttribute("message", e.getMessage());
				req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp")
						.forward(req, resp);
				return;
			}
		}
		if (!OSTableExists) {
			List<PollEntry> entries = createOSEntries();
			try {
				provider.createNewPoll(
						"Glasanje za omiljeni operacijski sustav",
						"Od sljedećih operacijskih sustava, koji Vam je operacijski sustav najdraži? Kliknite na link kako biste glasali!",
						entries);
			} catch (Exception e) {
				req.setAttribute("message", e.getMessage());
				req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp")
						.forward(req, resp);
				return;
			}
		}
	}

	/**
	 * Kreira listu opcija za anketu operacijskih sustava.
	 * 
	 * @return lista opcija za anketu operacijskih sustava
	 */
	private List<PollEntry> createOSEntries() {
		List<PollEntry> entries = new ArrayList<PollEntry>();
		entries.add(new PollEntry(null, "Windows",
				"http://windows.microsoft.com/hr-hr/windows/home", Long
						.valueOf(0)));
		entries.add(new PollEntry(null, "Unix", "http://www.unix.org/", Long
				.valueOf(0)));
		entries.add(new PollEntry(null, "MacOS",
				"https://www.apple.com/hr/osx/", Long.valueOf(0)));
		return entries;
	}

	/**
	 * Kreira listu opcija za anketu bendova.
	 * 
	 * @return lista opcija za anketu bendova
	 */
	private List<PollEntry> createBandEntries() {
		List<PollEntry> entries = new ArrayList<PollEntry>();
		entries.add(new PollEntry(
				null,
				"The Beatles",
				"http://www.geocities.com/~goldenoldies/TwistAndShout-Beatles.mid",
				Long.valueOf(0)));
		entries.add(new PollEntry(
				null,
				"The Platters",
				"http://www.geocities.com/~goldenoldies/SmokeGetsInYourEyes-Platters-ver2.mid",
				Long.valueOf(0)));
		entries.add(new PollEntry(
				null,
				"The Beach Boys",
				"http://www.geocities.com/~goldenoldies/SurfinUSA-BeachBoys.mid",
				Long.valueOf(0)));
		entries.add(new PollEntry(
				null,
				"The Four Seasons",
				"http://www.geocities.com/~goldenoldies/BigGirlsDontCry-FourSeasons.mid",
				Long.valueOf(0)));
		entries.add(new PollEntry(null, "The Marcels",
				"http://www.geocities.com/~goldenoldies/Bluemoon-Marcels.mid",
				Long.valueOf(0)));
		entries.add(new PollEntry(
				null,
				"The Everly Brothers",
				"http://www.geocities.com/~goldenoldies/All.I.HaveToDoIsDream-EverlyBrothers.mid",
				Long.valueOf(0)));
		entries.add(new PollEntry(
				null,
				"The Mamas And The Papas",
				"http://www.geocities.com/~goldenoldies/CaliforniaDreaming-Mamas-Papas.mid",
				Long.valueOf(0)));
		return entries;
	}

}
