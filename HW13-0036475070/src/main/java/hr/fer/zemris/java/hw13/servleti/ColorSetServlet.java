package hr.fer.zemris.java.hw13.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet koji obavlja posao postavljanja boje za trenutnog korisnika (trenutnu
 * sesiju).
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/setcolor")
public class ColorSetServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// pročitaj predanu boju, ako nije valjana ili nije predana, boju
		// postavi na white
		String color = req.getParameter("color");
		if (color != null) {
			if (!color.toUpperCase().equals("WHITE")
					&& !color.toUpperCase().equals("RED")
					&& !color.toUpperCase().equals("GREEN")
					&& !color.toUpperCase().equals("CYAN")) {
				color = "WHITE";
			}
		} else {
			color = "WHITE";
		}
		// postavi sesijski parametar pod ključ pickedBgColor
		req.getSession().setAttribute("pickedBgCol", color);
		// vrati se na index
		resp.sendRedirect("/aplikacija2/index.jsp");
	}
}
