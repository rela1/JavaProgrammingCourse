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
 * Kreira novi zapis radionice spreman za prosljeđivanje stranici koja je
 * sposobna iscrtavati formular.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/new")
public class NewServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
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
		// kreiraj novu praznu formu iz radionice
		Radionica radionica = new Radionica();
		RadionicaForm forma = new RadionicaForm();
		forma.popuniIzRadionice(radionica);
		// učitaj i postavi mape opreme, publike i trajanja i pošalji praznu
		// formu formularu na iscrtavanje
		RadioniceBaza baza = RadioniceBaza.ucitaj(req.getServletContext()
				.getRealPath("/WEB-INF/baza"));
		req.setAttribute("radionica", forma);
		req.setAttribute("oprema", baza.getOprema());
		req.setAttribute("publika", baza.getPublika());
		req.setAttribute("trajanje", baza.getTrajanje());
		req.getRequestDispatcher("/WEB-INF/pages/formular.jsp").forward(req,
				resp);
	}
}
