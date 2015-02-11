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
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Kreira .xls file sa informacijama o stavkama ankete i njihovom broju glasova.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/glasanje-xls")
public class GlasanjeXLSServlet extends HttpServlet {

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
		// kreiraj xls workbook i zapiši ga na output stream
		HSSFWorkbook workbook = createWorkBook(pollEntries);
		resp.setContentType("application/vnd.ms-excel");
		workbook.write(resp.getOutputStream());
	}

	/**
	 * Kreira .xls file iz primljene liste zapisa opcija ankete.
	 * 
	 * @param entries
	 *            lista zapisa opcija ankete
	 * @return .xls workbook
	 */
	private HSSFWorkbook createWorkBook(List<PollEntry> entries) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("sheet1");
		HSSFRow head = sheet.createRow(0);
		head.createCell(0).setCellValue("Opcija");
		head.createCell(1).setCellValue("Broj glasova");
		int iterator = 1;
		for (PollEntry entry : entries) {
			HSSFRow row = sheet.createRow(iterator);
			row.createCell(0).setCellValue(entry.getOptionTitle());
			row.createCell(1).setCellValue(entry.getVotesCount().toString());
			iterator++;
		}
		return workbook;
	}

}
