package hr.fer.zemris.java.hw13.servleti.glasanje;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Kreira .xls file sa informacijama o izvođačima i njihovom broju glasova.
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
		// pročitaj izvođače
		String artistsFileName = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-definicija.txt");
		String votesFileName = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-rezultati.txt");
		File votes = new File(votesFileName);
		File artistsFile = new File(artistsFileName);
		List<String> artists = GlasanjeRezultatiServlet.readFile(artistsFile);
		// ako je došlo do greške iz čitanja file ili file ne postoji
		if (artists == null) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeFileError.jsp")
					.forward(req, resp);
			return;
		}
		// pročitaj votemap oblika (izvođač, broj glasova)
		LinkedHashMap<String, String> voteMap;
		try {
			voteMap = GlasanjeRezultatiServlet.createVoteMap(votes, artists);
		} catch (Exception e) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeFileError.jsp")
					.forward(req, resp);
			return;
		}
		// kreiraj xls workbook i zapiši ga na output stream
		HSSFWorkbook workbook = createWorkBook(voteMap);
		resp.setContentType("application/vnd.ms-excel");
		workbook.write(resp.getOutputStream());
	}

	/**
	 * Kreira .xls file iz primljene mape glasova.
	 * 
	 * @param voteMap
	 *            mapa glasova
	 * @return .xls workbook
	 */
	private HSSFWorkbook createWorkBook(LinkedHashMap<String, String> voteMap) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("sheet1");
		HSSFRow head = sheet.createRow(0);
		head.createCell(0).setCellValue("Izvođač");
		head.createCell(1).setCellValue("Broj glasova");
		int iterator = 1;
		for (Entry<String, String> e : voteMap.entrySet()) {
			HSSFRow row = sheet.createRow(iterator);
			row.createCell(0).setCellValue(e.getKey());
			row.createCell(1).setCellValue(e.getValue());
			iterator++;
		}
		return workbook;
	}

}
