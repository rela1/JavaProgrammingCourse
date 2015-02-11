package hr.fer.zemris.web.ankete.servleti;

import hr.fer.zemris.web.ankete.dao.SQLConnectionProvider;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.sql.DataSource;

/**
 * Filter koji prilikom pristupima svim servletima ove web aplikacije prvo
 * dodjeljuje konekciju i postavlja je u ThreadLocal mapu da je dretva može
 * dohvaćati. Prilikom izlaska iz servleta filter oslobađa vezu.
 * 
 * @author Ivan Relić
 * 
 */
@WebFilter(filterName = "f1", urlPatterns = { "/*" })
public class ConnectionSetterFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		DataSource ds = (DataSource) request.getServletContext().getAttribute(
				"hr.fer.zemris.dbpool");
		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException e) {
			throw new IOException("Baza podataka nije dostupna.", e);
		}
		SQLConnectionProvider.setConnection(con);
		try {
			chain.doFilter(request, response);
		} finally {
			SQLConnectionProvider.setConnection(null);
			try {
				con.close();
			} catch (SQLException ignorable) {
			}
		}
	}

}
