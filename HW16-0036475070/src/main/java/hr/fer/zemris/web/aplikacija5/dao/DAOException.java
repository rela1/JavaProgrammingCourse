package hr.fer.zemris.web.aplikacija5.dao;

/**
 * Iznimka DAO sloja koja obuhvaća sve iznimke u radu s perzistencijskim slojem.
 * 
 * @author Ivan Relić
 * 
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public DAOException(String message) {
		super(message);
	}
}