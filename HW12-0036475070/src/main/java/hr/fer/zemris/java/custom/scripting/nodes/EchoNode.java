package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;

/**
 * Klasa za instanciranje objekata tipa EchoNode, za pohranjivanje u stablo za
 * tag =.
 * 
 * @author Ivan Relic
 * 
 */
public class EchoNode extends Node {

	private Token[] tokens;

	/**
	 * Konstruktor u clanske varijable sprema primljeni niz tokena.
	 * 
	 * @param tokens
	 *            Niz tokena koji cine izraz.
	 */
	public EchoNode(Token[] tokens) {

		this.tokens = new Token[tokens.length];

		for (int i = 0, length = tokens.length; i < length; i++) {

			this.tokens[i] = tokens[i];

		}
	}

	/**
	 * Vraca niz tokena iz clanske varijable tokens.
	 * 
	 * @return Niz tokena.
	 */
	public Token[] getTokens() {

		// kreiraj novi niz tokena i vrati ga jer su objekti Token mutable
		Token[] tokens = new Token[this.tokens.length];
		tokens = this.tokens;
		return tokens;
	}

	/**
	 * Override metode toString za ispis EchoNodea kao string naredbe.
	 * 
	 */
	public String toString() {
		String pom = "";

		// prodji kroz sve tokene i nadodaj ih u string
		for (int i = 0, duljina = tokens.length; i < duljina; i++) {
			pom = pom + " " + tokens[i].asText();
		}

		// dodaj tagove za echo naredbu i vrati ju
		pom = "{$=" + pom + " $}";
		return pom;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
}
