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
 * Dohvaća zapis radionice spreman za prosljeđivanje stranici koja je sposobna
 * iscrtavati formular i omogućiti editiranje.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/edit")
public class EditServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Metoda iz parametara dohvaća ID radionice koju je potrebno editirati,
	 * dohvaća je iz baze i prosljeđuje na iscrtavanje.
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// ako nema podataka o trenutnom korisniku o sesiji, skoči na stranicu s
		// porukom pogreške
		if (req.getSession().getAttribute("current.user") == null) {
			req.setAttribute("poruka", "Neautorizirani pristup!");
			req.getRequestDispatcher("/WEB-INF/pages/greska.jsp").forward(req,
					resp);
			return;
		}
		Long id = null;
		try {
			id = Long.valueOf(req.getParameter("id"));
		} catch (Exception ex) {
			req.setAttribute("poruka",
					"Primljeni su neispravni parametri. ID je nepoznat!");
			req.getRequestDispatcher("/WEB-INF/pages/greska.jsp").forward(req,
					resp);
			return;
		}
		// učitaj i postavi mape opreme, publike i trajanja
		RadioniceBaza baza = RadioniceBaza.ucitaj(req.getServletContext()
				.getRealPath("/WEB-INF/baza"));
		RadionicaForm forma = new RadionicaForm();
		Radionica radionica = baza.dohvati(id);
		if (radionica == null) {
			req.setAttribute("poruka", "Tražena radionica ne postoji.");
			req.getRequestDispatcher("/WEB-INF/pages/greska.jsp").forward(req,
					resp);
			return;
		}
		// popuni formu, zakvači mape s atributima opreme, publike i trajanja i
		// pošalji formularu
		forma.popuniIzRadionice(radionica);
		req.setAttribute("radionica", forma);
		req.setAttribute("oprema", baza.getOprema());
		req.setAttribute("publika", baza.getPublika());
		req.setAttribute("trajanje", baza.getTrajanje());
		req.getRequestDispatcher("/WEB-INF/pages/formular.jsp").forward(req,
				resp);
	}
}
