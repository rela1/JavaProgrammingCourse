package hr.fer.zemris.java.custom.collections;

public class EmptyStackException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmptyStackException() {
	}

	public EmptyStackException(String message) {
		super(message);
	}

	public EmptyStackException(Throwable message) {
		super(message);
	}

	public EmptyStackException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmptyStackException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
