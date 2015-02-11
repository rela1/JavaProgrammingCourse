package hr.fer.zemris.java.custom.scripting.parser;


/**Klasa za exceptione vrste SmartScriptParserException koje izbacuju objekti tipa 
 * SmartScriptParser ako naidju na problem pri parsiranju dokumenta.
 * 
 * @author Ivan Relic
 *
 */
public class SmartScriptParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SmartScriptParserException() {
		
	}

	public SmartScriptParserException(String message) {
		super(message);
	}

	public SmartScriptParserException(Throwable cause) {
		super(cause);
	}

	public SmartScriptParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public SmartScriptParserException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
