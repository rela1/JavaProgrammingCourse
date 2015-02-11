package hr.fer.zemris.web.ankete.servleti;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * Definira potrebne operacije koje treba obaviti pri kreiranju i pri
 * uništavanju contexta web aplikacije. Prilikom pokretanja servera potrebno je
 * kreirati ConnectionPool prema bazi podataka, a pri uništenju potrebno je isti
 * taj pool uništiti.
 * 
 * @author Ivan Relić
 * 
 */
@WebListener
public class DBPoolContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Properties DBParameters = new Properties();
		try {
			DBParameters.load(new FileInputStream(new File(sce.getServletContext()
					.getRealPath("/WEB-INF/properties/database.properties"))));
		} catch (IOException e1) {
		}
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass(DBParameters.getProperty("driverClass"));
		} catch (PropertyVetoException e) {
			throw new RuntimeException(
					"Pogreška prilikom inicijalizacije connection poola!");
		}
		cpds.setUser(DBParameters.getProperty("username"));
		cpds.setPassword(DBParameters.getProperty("password"));
		cpds.setJdbcUrl(DBParameters.getProperty("URL"));
		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce
				.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if (cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				throw new RuntimeException(
						"Pogreška prilikom uništavanja connection poola!");
			}
		}
	}

}
