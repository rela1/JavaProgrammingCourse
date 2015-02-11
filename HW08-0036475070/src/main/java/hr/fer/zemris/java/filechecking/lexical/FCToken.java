package hr.fer.zemris.java.filechecking.lexical;

/**
 * Razred predstavlja jedan token ulaznog programa.
 * 
 * @author Ivan Relic
 * 
 */
public class FCToken {

	private FCTokenType tokenType;
	private Object value;

	/**
	 * Konstruktor. Tip tokena ne smije biti null vrijednost, dok vrijednost
	 * tokena smije biti null.
	 * 
	 * @param tokenType
	 *            tip tokena
	 * @param value
	 *            vrijednost tokena
	 */
	public FCToken(FCTokenType tokenType, Object value) {
		if (tokenType == null) {
			throw new RuntimeException("Token type should not be null!");
		}
		this.tokenType = tokenType;
		this.value = value;
	}

	/**
	 * Dohvaca tip spremljenog tokena.
	 * 
	 * @return tip tokena
	 */
	public FCTokenType getTokenType() {
		return tokenType;
	}

	/**
	 * Dohvaca vrijednost tokena.
	 * 
	 * @return vrijednost tokena ili null ako token nema pridruzenu vrijednost
	 */
	public Object getValue() {
		return value;
	}

}
