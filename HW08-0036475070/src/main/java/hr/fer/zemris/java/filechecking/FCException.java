package hr.fer.zemris.java.filechecking;

/**
 * Vrsna iznimka iz koje se izvode ostale - za leksicku, sintaksnu i izvrsnu
 * analizu.
 * 
 * @author Ivan Relic
 * 
 */
public class FCException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FCException() {
	}

	public FCException(String message) {
		super(message);
	}

	public FCException(Throwable cause) {
		super(cause);
	}

	public FCException(String message, Throwable cause) {
		super(message, cause);
	}

	public FCException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
