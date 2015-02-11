package hr.fer.zemris.java.filechecking;

/**
 * Iznimka za pogresku pri izvodenju ulaznog programa.
 * 
 * @author Ivan Relic
 * 
 */
public class FCExecutorException extends FCException {

	private static final long serialVersionUID = 1L;

	public FCExecutorException() {
	}

	public FCExecutorException(String message) {
		super(message);
	}

	public FCExecutorException(Throwable cause) {
		super(cause);
	}

	public FCExecutorException(String message, Throwable cause) {
		super(message, cause);
	}

	public FCExecutorException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
