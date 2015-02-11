package hr.fer.zemris.java.hw13.servleti;

import java.io.IOException;

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
 * Kreira pie chart sliku koja prikazuje korištenje OSa.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/reportImage")
public class ReportImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// kreiraj dataset i chart
		PieDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset, "OS usage: ");
		// postavi mime-type i zapiši chart na output stream
		resp.setContentType("image/png");
		ChartUtilities.writeChartAsPNG(resp.getOutputStream(), chart, 500, 270);
	}

	/**
	 * Kreira podatke za Pie Chart.
	 * 
	 * @return piedataset
	 */
	private PieDataset createDataset() {
		DefaultPieDataset result = new DefaultPieDataset();
		result.setValue("Linux", 29);
		result.setValue("Mac", 20);
		result.setValue("Windows", 51);
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
