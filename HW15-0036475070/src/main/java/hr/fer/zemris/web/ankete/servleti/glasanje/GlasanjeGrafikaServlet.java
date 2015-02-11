package hr.fer.zemris.web.ankete.servleti.glasanje;

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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

/**
 * Iscrtava pie chart za trenutno stanje glasova ankete.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {

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
		// kreiraj dataset i chart
		PieDataset dataset = createDataset(pollEntries);
		JFreeChart chart = createChart(dataset, "Pie - chart: ");
		// postavi mime-type i zapiši chart na output stream
		resp.setContentType("image/png");
		ChartUtilities.writeChartAsPNG(resp.getOutputStream(), chart, 600, 400);
	}

	/**
	 * Kreira podatke za Pie Chart.
	 * 
	 * @param entries
	 *            lista opcija ankete
	 * @return piedataset
	 */
	private PieDataset createDataset(List<PollEntry> entries) {
		// zbroji vrijednosti svih glasova da možeš računati udio
		long voteSum = 0;
		for (PollEntry entry : entries) {
			voteSum += entry.getVotesCount().longValue();
		}
		if (voteSum == 0) {
			voteSum = 1;
		}
		DefaultPieDataset result = new DefaultPieDataset();
		for (PollEntry entry : entries) {
			result.setValue(
					entry.getOptionTitle(),
					Long.valueOf(entry.getVotesCount().longValue() * 100
							/ voteSum));
		}
		return result;
	}

	/**
	 * Kreira pie chart.
	 * 
	 * @param dataset
	 *            pie dataset
	 * @param title
	 *            naslov charta
	 * @return pie chart
	 */
	private JFreeChart createChart(PieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true,
				true, false);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		return chart;
	}
}
