package hr.fer.zemris.java.custom.scripting.tokens;

/**Klasa TokenConstantDouble koja sluzi za pohranu procitanih Double vrijednosti.
 * 
 * @author Ivan Relic
 *
 */
public class TokenConstantDouble extends Token {

	//read only clanska varijabla value
	private double value;
	
	
	/** Konstruktor s jednim argumentom koji pridjeljuje varijabli value ono sto primi kao
	 * argument.
	 * 
	 * @param name Double broj koji pridjeljujemo varijabli value.
	 */
	public TokenConstantDouble(double value) {
		this.value = value;
		
	}

	
	/**Overrideana metoda iz superklase, vraca clansku varijablu value u stringu.
	 * 
	 */
	public String asText() {
		return Double.toString(value);
	}
	
	
	/**Vraca vrijednost clanske varijable value.
	 * 
	 * @return Clanska varijabla value.
	 */
	public double getName() {
		return value;
	}
}

