package hr.fer.zemris.java.hw13.servleti;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Listener koji će pri kreiranju servleta u kontekst zapisati trenutno vrijeme,
 * tj. vrijeme kad je servlet kreiran.
 * 
 * @author Ivan Relić
 * 
 */
@WebListener
public class ContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		context.setAttribute("time", new Long(System.currentTimeMillis()));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
