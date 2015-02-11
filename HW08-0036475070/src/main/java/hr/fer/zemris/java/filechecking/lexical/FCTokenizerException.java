package hr.fer.zemris.java.filechecking.lexical;

import hr.fer.zemris.java.filechecking.FCException;

/**
 * Iznimka za pogreske kod tokeniziranja.
 * 
 * @author Ivan Relic
 * 
 */
public class FCTokenizerException extends FCException {

	private static final long serialVersionUID = 1L;

	public FCTokenizerException() {
	}

	public FCTokenizerException(String message) {
		super(message);
	}

	public FCTokenizerException(Throwable cause) {
		super(cause);
	}

	public FCTokenizerException(String message, Throwable cause) {
		super(message, cause);
	}

	public FCTokenizerException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
