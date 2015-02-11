package hr.fer.zemris.java.hw13.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Kreira .xml file ako su primljeni parametri korektni, ako nisu, otvara se
 * .jsp file s informacijom da uneseni parametri nisu u redu.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/powers")
public class PowersServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer a = null;
		Integer b = null;
		Integer n = null;
		// dohvati parametre iz zahtjeva
		try {
			a = Integer.parseInt(req.getParameter("a"));
			if (a < -100 || a > 100) {
				a = null;
			}
		} catch (Exception e) {
			a = null;
		}
		try {
			b = Integer.parseInt(req.getParameter("b"));
			if (b < -100 || b > 100) {
				b = null;
			}
		} catch (Exception e) {
			b = null;
		}
		try {
			n = Integer.parseInt(req.getParameter("n"));
			if (n < 1 || n > 5) {
				n = null;
			}
		} catch (Exception e) {
			n = null;
		}
		// ako je bilo koji od parametara krivo zadan, dojavi to
		if (a == null || b == null || n == null) {
			req.getRequestDispatcher("WEB-INF/pages/powererror.jsp").forward(
					req, resp);
			return;
		} else {
			// ako je donja granica veća od gornje, zamijeni ih
			if (a > b) {
				Integer temp = a;
				a = b;
				b = temp;
			}
			// kreiraj xls workbook i zapiši ga na output stream
			HSSFWorkbook workbook = createWorkBook(a, b, n);
			resp.setContentType("application/vnd.ms-excel");
			workbook.write(resp.getOutputStream());
		}
	}

	/**
	 * Kreira .xls workbook prema primljenim argumentima.
	 * 
	 * @param a
	 *            donja granica
	 * @param b
	 *            gornja granica
	 * @param n
	 *            broj stranica
	 * @return workbook
	 */
	private HSSFWorkbook createWorkBook(Integer a, Integer b, Integer n) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		for (int i = 1, length = n.intValue(); i <= length; i++) {
			HSSFSheet sheet = workbook.createSheet("sheet" + i);
			HSSFRow head = sheet.createRow(0);
			head.createCell(0).setCellValue("Broj: ");
			head.createCell(1).setCellValue("Broj na " + i + ". potenciju: ");
			for (int j = 0, rows = b.intValue() - a.intValue(); j <= rows; j++) {
				HSSFRow row = sheet.createRow(j + 1);
				row.createCell(0).setCellValue(a + j);
				row.createCell(1).setCellValue(Math.pow(a + j, i));
			}
		}
		return workbook;
	}
}
