package hr.fer.zemris.java.filechecking.syntax;

import hr.fer.zemris.java.filechecking.FCException;

/**
 * Predstavlja iznimke nastale zbog pogresaka pri parsiranju.
 * 
 * @author Ivan Relic
 * 
 */
public class FCParserException extends FCException {

	private static final long serialVersionUID = 1L;

	public FCParserException() {
	}

	public FCParserException(String message) {
		super(message);
	}

	public FCParserException(Throwable cause) {
		super(cause);
	}

	public FCParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public FCParserException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
