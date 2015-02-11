package hr.fer.zemris.java.custom.scripting.tokens;

/**Klasa TokenString koja sluzi za pohranu procitanih stringova iz dokumenta.
 * 
 * @author Ivan Relic
 *
 */
public class TokenString extends Token {
	
	//read only clanska varijabla value
	private String value;
	
	
	/** Konstruktor s jednim argumentom koji pridjeljuje varijabli value ono sto primi kao
	 * argument.
	 * 
	 * @param name String koji pridjeljujemo varijabli value.
	 */
	public TokenString(String value) {
		this.value = value;

	}

	
	/**Overrideana metoda iz superklase, vraca clansku varijablu value.
	 * 
	 */
	public String asText() {
		
		String pom = value;
		
		//vrati escapere gdje treba
		pom = pom.replace("\\", "\\\\");
		pom = pom.replace("\"", "\\\"");
		
		//vrati vrijednost s dodanim znakovima " na pocetku i kraju
		return "\"" + pom + "\"";
	}
	
	
	/**Vraca vrijednost clanske varijable value.
	 * 
	 * @return Clanska varijabla value.
	 */
	public String getName() {
		return value;
	}
	

}
