package hr.fer.zemris.java.hw13.servleti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet iz primljenih parametara računa kvadrate brojeva u primljenom
 * rasponu. Ako parametri nisu postavljeni ili s krivi, postavljaju se defaultne
 * vrijednost 0 - 20.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/squares")
public class SquaresServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// dohvati parametre a i b iz zahtjeva
		Integer a = null;
		Integer b = null;
		try {
			a = Integer.valueOf(req.getParameter("a"));
		} catch (Exception e) {
			a = 0;
		}
		try {
			b = Integer.valueOf(req.getParameter("b"));
		} catch (Exception e) {
			b = 20;
		}
		// ako je donja granica veća od gornje, zamijeni ih
		if (a > b) {
			Integer tmp = a;
			a = b;
			b = tmp;
		}
		// ako je gornja granica veća od donje + 20, postavi gornju na donja +
		// 20
		if (b > a + 20) {
			b = a + 20;
		}
		// u listu podataka pohrani uređene parove oblika - broj, kvadratna
		// vrijednost, i to za sve brojeve u rasponu predanom zahtijevom
		List<Par> podaci = new ArrayList<Par>();
		for (int i = a; i <= b; i++) {
			podaci.add(new Par(i, i * i));
		}
		// pohrani podatke i proslijedi ispis podataka novoj stranici
		req.setAttribute("rezultati", podaci);
		req.getRequestDispatcher("WEB-INF/pages/prikaz.jsp").forward(req, resp);
	}

	/**
	 * Enkapsulira uređeni par brojeva oblika - broj, vrijednost.
	 * 
	 * @author Ivan Relić
	 * 
	 */
	public static class Par {
		int broj;
		int vrijednost;

		/**
		 * Konstruktor. Kreira uređeni par s predanom vrijednosti za broj i za
		 * vrijednost.
		 * 
		 * @param broj
		 *            broj
		 * @param vrijednost
		 *            vrijednost pridružena broju
		 */
		public Par(int broj, int vrijednost) {
			this.broj = broj;
			this.vrijednost = vrijednost;
		}

		/**
		 * Dohvaća broj.
		 * 
		 * @return broj
		 */
		public int getBroj() {
			return broj;
		}

		/**
		 * Dohvaća vrijednost pridruženu broju.
		 * 
		 * @return vrijednost pridružena broju
		 */
		public int getVrijednost() {
			return vrijednost;
		}
	}
}
