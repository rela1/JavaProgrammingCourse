package hr.fer.zemris.java.hw13.servleti.glasanje;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Pokreće proces glasanja.
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
		// dohvati sve izvođače, postavi ih u mapu i pošalji jsp-u
		String fileName = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-definicija.txt");
		List<String> fileLines = Files.readAllLines(Paths.get(fileName),
				StandardCharsets.UTF_8);
		Map<String, String> artists = new LinkedHashMap<>();
		for (int i = 0, length = fileLines.size(); i < length; i++) {
			Scanner sc = new Scanner(fileLines.get(i));
			sc.useDelimiter("\t");
			artists.put(sc.next(), sc.next());
			sc.close();
		}
		// pohrani izvođače kao parametar u request
		req.setAttribute("artists", artists);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(
				req, resp);
	}

}
