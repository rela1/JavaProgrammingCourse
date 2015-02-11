package hr.fer.zemris.java.custom.scripting.tokens;


/**Klasa TokenConstantInteger koja sluzi za pohranu procitanih Integer vrijednosti.
 * 
 * @author Ivan Relic
 *
 */
public class TokenConstantInteger extends Token {

	//read only clanska varijabla value
	private int value;
	
	
	/** Konstruktor s jednim argumentom koji pridjeljuje varijabli value ono sto primi kao
	 * argument.
	 * 
	 * @param name Integer broj koji pridjeljujemo varijabli value.
	 */
	public TokenConstantInteger(int value) {
		this.value = value;
		
	}

	
	/**Overrideana metoda iz superklase, vraca clansku varijablu value u stringu.
	 * 
	 */
	public String asText() {
		return Integer.toString(value);
	}
	
	
	/**Vraca vrijednost clanske varijable value.
	 * 
	 * @return Clanska varijabla value.
	 */
	public int getName() {
		return value;
	}
}
