package hr.fer.zemris.java.webserver;

/**
 * Sučelje radnika koji je sposoban izvršiti zahtjev.
 * 
 * @author Ivan Relić
 * 
 */
public interface IWebWorker {

	/**
	 * Izvršava zahtjev nad primljenim contextom.
	 * 
	 * @param context
	 *            kontekst
	 */
	public void processRequest(RequestContext context);
}