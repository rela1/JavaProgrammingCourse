package hr.fer.zemris.java.custom.scripting.tokens;

/**Klasa TokenOperator koja sluzi za pohranu procitanih vrijednosti operatora iz dokumenta.
 * 
 * @author Ivan Relic
 *
 */
public class TokenOperator extends Token {
	
	//read only clanska varijabla symbol
	private String symbol;
	
	/** Konstruktor s jednim argumentom koji pridjeljuje varijabli symbol ono sto primi kao
	 * argument.
	 * 
	 * @param name String koji pridjeljujemo varijabli symbol.
	 */
	public TokenOperator(String symbol) {
		this.symbol = symbol;

	}

	
	/**Overrideana metoda iz superklase, vraca clansku varijablu symbol.
	 * 
	 */
	public String asText() {
		return symbol;
	}
	
	
	/**Vraca vrijednost clanske varijable symbol.
	 * 
	 * @return Clanska varijabla symbol.
	 */
	public String getName() {
		return symbol;
	}

}
