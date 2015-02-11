package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;

/**Klasa za instanciranje objekata tipa ForLoopNode, za pohranjivanje u stablo za tag FOR.
 * 
 * @author Ivan Relic
 *
 */
public class ForLoopNode extends Node {

	/*pretpostavljen je oblik FOR naredbe oblika varijabla, token, token, (token), zadnji 
	 * token ne mora nuzno biti naveden */
	private TokenVariable variable;
	private Token startExpression;
	private Token endExpression;
	private Token stepExpression = null;
	
	
	/**Konstruktor za postavljanje clanskih varijabli ako korak nije null.
	 * 
	 * @param variable Varijabla
	 * @param start Pocetna vrijednost
	 * @param end Zavrsna vrijednost
	 * @param step Vrijednost koraka
	 */
	public ForLoopNode(TokenVariable variable, Token start, Token end, Token step) {
		
		//kreiraj objekte za clanske varijable
		this.variable = variable;
		startExpression = start;
		endExpression = end;
		stepExpression= step;
		
	}
	
	
	/**Konstruktor za postavljanje clanskih varijabli ako je korak null.
	 * 
	 * @param variable Varijabla
	 * @param start Pocetna vrijednost
	 * @param end Zavrsna vrijednost
	 */
	public ForLoopNode(TokenVariable variable, Token start, Token end) {
		
		//kreiraj objekte za clanske varijable
		this.variable = variable;
		startExpression = start;
		endExpression = end;		
	}

	
	/**Funkcija vraca vrijednost clanske varijable variable.
	 * 
	 * @return Variable
	 */
	public TokenVariable getVariable() {
		
		//kreiraj novi TokenVariable i vrati ga jer je TokenVariable mutable objekt 
		TokenVariable variable = new TokenVariable(this.variable.asText());
		return variable;
	}


	/**Vraca pocetnu vrijednost izraza.
	 * 
	 * @return Pocetna vrijednost
	 */
	public TokenConstantInteger getStartExpression() {
		
		//kreiraj novi Token i vrati ga jer je Token mutable objekt 
		TokenConstantInteger startExpression = 
				new TokenConstantInteger(Integer.parseInt(this.startExpression.asText()));
		return (TokenConstantInteger) startExpression;
	}


	/**Vraca zavrsnu vrijednost izraza.
	 * 
	 * @return Zavrsna vrijednost
	 */
	public TokenConstantInteger getEndExpression() {

		//kreiraj novi Token i vrati ga jer je Token mutable objekt 
		TokenConstantInteger endExpression = 
				new TokenConstantInteger(Integer.parseInt(this.endExpression.asText()));
		return (TokenConstantInteger) endExpression;
	}


	/**Vraca vrijednost koraka izraza.
	 * 
	 * @return Vrijednost koraka
	 */
	public TokenConstantInteger getStepExpression() {
		
		//kreiraj novi Token i vrati ga jer je Token mutable objekt 
		TokenConstantInteger stepExpression = 
				new TokenConstantInteger(Integer.parseInt(this.stepExpression.asText()));
		return (TokenConstantInteger) stepExpression;
	}	
	
	
	/**Override metode toString za FOR naredbu, vraca takav oblik stringa.
	 * 
	 */
	public String toString() {
		
		//ako nema step izraza, vrati oblik naredbe bez njega, inace s njim
		if (stepExpression == null) {
			return "{$FOR " + variable.asText() + " " + startExpression.asText() + " " 
					+ endExpression.asText() + " $}";
		}
		
		else {
			return "{$FOR " + variable.asText() + " " + startExpression.asText() + " " 
					+ endExpression.asText() + " " + stepExpression.asText() + " $}";
		}
	}
	
}
