package hr.fer.zemris.java.hw13.servleti.glasanje;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
 * Iscrtava pie chart za trenutno stanje glasova.
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
		// pročitaj izvođače
		String artistsFileName = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-definicija.txt");
		String votesFileName = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-rezultati.txt");
		File votes = new File(votesFileName);
		File artistsFile = new File(artistsFileName);
		List<String> artists = GlasanjeRezultatiServlet.readFile(artistsFile);
		// ako je došlo do greške iz čitanja file ili ne postoji
		if (artists == null) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeFileError.jsp")
					.forward(req, resp);
			return;
		}
		// pročitaj votemap oblika (izvođač, broj glasova)
		Map<String, String> voteMap;
		try {
			voteMap = GlasanjeRezultatiServlet.createVoteMap(votes, artists);
		} catch (Exception e) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeFileError.jsp")
					.forward(req, resp);
			return;
		}
		// kreiraj dataset i chart
		PieDataset dataset = createDataset(voteMap);
		JFreeChart chart = createChart(dataset, "Pie - chart: ");
		// postavi mime-type i zapiši chart na output stream
		resp.setContentType("image/png");
		ChartUtilities.writeChartAsPNG(resp.getOutputStream(), chart, 400, 400);
	}

	/**
	 * Kreira podatke za Pie Chart.
	 * 
	 * @param voteMap
	 *            mapa s podacima glasanja
	 * @return piedataset
	 */
	private PieDataset createDataset(Map<String, String> voteMap) {
		// zbroji vrijednosti svih glasova da možeš računati udio
		int voteSum = 0;
		for (String s : voteMap.values()) {
			voteSum += Integer.parseInt(s);
		}
		if (voteSum == 0) {
			voteSum = 1;
		}
		DefaultPieDataset result = new DefaultPieDataset();
		for (Entry<String, String> e : voteMap.entrySet()) {
			result.setValue(e.getKey(),
					new Integer(Integer.parseInt(e.getValue()) * 100 / voteSum));
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
