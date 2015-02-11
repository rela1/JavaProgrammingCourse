package hr.fer.zemris.java.hw13.servleti.glasanje;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Obavlja proces glasanja po primljenom id-u iz parametara.
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
		// dohvati broj izvođača preko definicijske datoteke
		int numberOfArtists = 0;
		try {
			numberOfArtists = (Files.readAllLines(
					Paths.get(req.getServletContext().getRealPath(
							"/WEB-INF/glasanje-definicija.txt")),
					StandardCharsets.UTF_8)).size();
		} catch (Exception e) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeFileError.jsp")
					.forward(req, resp);
			return;
		}

		// dohvati broj ID-a iz parametara
		Integer id = null;
		try {
			id = Integer.parseInt(req.getParameter("id"));
		} catch (Exception e) {
			id = null;
		}
		// ako je nevaljan ID, skoči na stranicu koja korisnika informira o tome
		if (id == null || id.intValue() < 1 || id.intValue() > numberOfArtists) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp")
					.forward(req, resp);
			return;
		} else {
			// pokušaj updateati glasove, ako ne uspiješ skoči na stranicu koja
			// prikazuje informacije o pogrešci
			try {
				updateVotes(id.intValue(), req, numberOfArtists);
				resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
			} catch (Exception e) {
				req.getRequestDispatcher("/WEB-INF/pages/glasanjeFileError.jsp")
						.forward(req, resp);
				return;
			}
		}
	}

	/**
	 * Kreira file s glasovima za pojedinog izvođača, ili updatea ako file već
	 * postoji.
	 * 
	 * @param id
	 *            id izvođača za koga je dodan glas
	 * @param req
	 *            request context
	 * @param numberOfArtists
	 *            broj izvođača
	 * @throws IOException
	 */
	private void updateVotes(int id, HttpServletRequest req, int numberOfArtists)
			throws IOException {
		String filePath = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-rezultati.txt");
		File votes = new File(filePath);
		List<String> fileVotes;
		// ako nije kreirana mapa s glasovima kreiraj je i postavi vrijednost 1
		// samo za primljeni ID
		if (!votes.exists()) {
			fileVotes = new ArrayList<String>();
			votes.createNewFile();
			for (int i = 0; i < numberOfArtists; i++) {
				String row = String.valueOf(i + 1) + "\t";
				row += (i + 1 == id) ? "1" : "0";
				fileVotes.add(row);
			}
		}
		// inače, modificiraj samo onaj ID koji je predan
		else {
			fileVotes = Files.readAllLines(votes.toPath(),
					StandardCharsets.UTF_8);
			String row = fileVotes.get(id - 1);
			// dohvati broj glasova iz tražene linije (traženi ID - 1)
			int numberOfVotes = Integer.parseInt(row.substring(row
					.indexOf("\t") + 1));
			numberOfVotes++;
			// updateaj redak s novim brojem glasova i postavi ga na mjesto
			// staroga u listu linija filea
			row = row.substring(0, row.indexOf("\t")) + "\t"
					+ String.valueOf(numberOfVotes);
			fileVotes.remove(id - 1);
			fileVotes.add(id - 1, row);
		}
		// nakon toga, svakako zapiši kreiranu listu u file
		FileWriter fw = new FileWriter(votes);
		for (int i = 0, length = fileVotes.size(); i < length; i++) {
			fw.write(fileVotes.get(i) + "\r\n");
		}
		fw.close();
	}
}
