package hr.fer.zemris.web.radionice.servleti;

import hr.fer.zemris.web.radionice.Radionica;
import hr.fer.zemris.web.radionice.RadioniceBaza;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet izlistava sve dostupne radionice sortirano po datumu, a unutar istog
 * datuma po nazivu radionice.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet({ "/listaj", "" })
public class ListajServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Čita sve radionice iz datoteke radionica i postavlja ih sortirano prvo po
	 * datumu, zatim po nazivu u listu koju zatim prosljeđuje na iscrtavanje.
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// učitaj sve radionice, sortiraj ih i proslijedi listaj.jsp na
		// iscrtavanje
		RadioniceBaza baza = RadioniceBaza.ucitaj(req.getServletContext()
				.getRealPath("/WEB-INF/baza"));
		List<Radionica> radionice = new ArrayList<Radionica>(
				baza.dohvatiSveZapiseRadionica());
		Collections.sort(radionice, new Comparator<Radionica>() {
			@Override
			public int compare(Radionica o1, Radionica o2) {
				int r = o1.getDatum().compareTo(o2.getDatum());
				if (r != 0) {
					return r;
				}
				return o1.getNaziv().compareTo(o2.getNaziv());
			}
		});
		req.setAttribute("radionice", radionice);
		req.getRequestDispatcher("/WEB-INF/pages/listaj.jsp")
				.forward(req, resp);
	}
}
