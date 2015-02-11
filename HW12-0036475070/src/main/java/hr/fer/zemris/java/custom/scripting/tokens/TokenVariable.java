package hr.fer.zemris.java.custom.scripting.tokens;

/**Klasa TokenVariable koja sluzi za pohranu procitanih varijabli iz dokumenta.
 * 
 * @author Ivan Relic
 *
 */
public class TokenVariable extends Token {
	
	//read only clanska varijabla name
	private String name;
	
	/** Konstruktor s jednim argumentom koji pridjeljuje varijabli name ono sto primi kao
	 * argument.
	 * 
	 * @param name String koji pridjeljujemo varijabli name.
	 */
	public TokenVariable(String name) {
		this.name = name;

	}

	
	/**Overrideana metoda iz superklase, vraca clansku varijablu name.
	 * 
	 */
	public String asText() {
		return name;
	}
	
	
	/**Vraca vrijednost clanske varijable name.
	 * 
	 * @return Clanska varijabla name.
	 */
	public String getName() {
		return name;
	}
	

}
