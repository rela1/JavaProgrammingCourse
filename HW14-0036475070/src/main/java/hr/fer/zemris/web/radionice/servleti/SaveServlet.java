package hr.fer.zemris.web.radionice.servleti;

import hr.fer.zemris.web.radionice.Radionica;
import hr.fer.zemris.web.radionice.RadionicaForm;
import hr.fer.zemris.web.radionice.RadioniceBaza;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Prima zapis radionice od formulara i provjerava je li sve uneseno u redu, ako
 * je, pohranjuje zapis radionice u bazu i vraća se na početni ekran, inače
 * vraća korisnika na formular s ispisanim pogreškama.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/save")
public class SaveServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Obavlja potrebne operacije operacije save (dodatan opis je u opisniku
	 * klase).
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			obradiZahtjev(req, resp);
		} catch (Exception e) {
			req.setAttribute("poruka",
					"Pogreška prilikom obrade akcije pohranjivanja!");
			req.getRequestDispatcher("/WEB-INF/pages/greska.jsp").forward(req,
					resp);
		}
	}

	/**
	 * Obavlja potrebne operacije operacije save (dodatan opis je u opisniku
	 * klase).
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			obradiZahtjev(req, resp);
		} catch (Exception e) {
			req.setAttribute("poruka",
					"Pogreška prilikom obrade akcije pohranjivanja!");
			req.getRequestDispatcher("/WEB-INF/pages/greska.jsp").forward(req,
					resp);
		}
	}

	/**
	 * Obrađuje zahtjev (sprema radionicu ako je valjana, vraća korisnika na
	 * popunjavanje formi ako radionica nije valjana i ispisuje mu sve pogreške,
	 * vraća se na početni ekran listanja ako korisnik pritisne odustani).
	 * 
	 * @param req
	 *            request
	 * @param resp
	 *            response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void obradiZahtjev(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		// ako nema podataka o trenutnom korisniku o sesiji, skoči na stranicu s
		// porukom pogreške
		if (req.getSession().getAttribute("current.user") == null) {
			req.setAttribute("poruka", "Neautorizirani pristup!");
			req.getRequestDispatcher("/WEB-INF/pages/greska.jsp").forward(req,
					resp);
			return;
		}

		req.setCharacterEncoding("UTF-8");

		// ako je metoda odustani, napravi redirect na listaj
		String metoda = req.getParameter("metoda");
		if (!"Pohrani".equals(metoda)) {
			resp.sendRedirect(req.getServletContext().getContextPath()
					+ "/listaj");
			return;
		}
		// pročitaj bazu podataka i provjeri je li forma valjana
		String direktorij = req.getServletContext()
				.getRealPath("/WEB-INF/baza");
		RadioniceBaza baza = RadioniceBaza.ucitaj(direktorij);
		RadionicaForm forma = new RadionicaForm();
		forma.popuniIzHttpRequesta(req);
		forma.validiraj();
		// ako je forma nevaljana, vrati je na iscrtavanje formularu s kreiranom
		// mapom pogrešaka
		if (forma.imaPogresaka()) {
			req.setAttribute("radionica", forma);
			req.setAttribute("oprema", baza.getOprema());
			req.setAttribute("publika", baza.getPublika());
			req.setAttribute("trajanje", baza.getTrajanje());
			req.getRequestDispatcher("/WEB-INF/pages/formular.jsp").forward(
					req, resp);
			return;
		}
		// ako je sve u redu, kreiraj novu radionicu iz forme i zapiši je u bazu
		// te napravi redirect na listaj
		Radionica radionica = new Radionica();
		forma.popuniURadionicu(radionica, baza.getOprema(), baza.getPublika(),
				baza.getTrajanje());
		baza.snimi(radionica);
		baza.snimi();
		resp.sendRedirect(req.getServletContext().getContextPath() + "/listaj");
	}
}
