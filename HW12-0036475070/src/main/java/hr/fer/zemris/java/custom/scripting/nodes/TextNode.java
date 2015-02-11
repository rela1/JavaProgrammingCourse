package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Klasa za instanciranje objekata tipa TextNode, za pohranjivanje u stablo
 * obicnih stringova.
 * 
 * @author Ivan Relic
 * 
 */
public class TextNode extends Node {

	private String text;

	/**
	 * Konstruktor funkcija za klasu, postavlja tekst u read - only clansku
	 * varijablu.
	 * 
	 * @param text
	 *            Tekst koji se postavlja u clansku varijablu text.
	 */
	public TextNode(String text) {
		this.text = text;
	}

	/**
	 * Funkcija vraca vrijednost clanske varijable text.
	 * 
	 * @return Vraca string.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Override toString metode da vraca clansku varijablu text.
	 * 
	 */
	public String toString() {

		String pom;

		// vrati escapere
		pom = text.replace("{", "\\{");
		return pom;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}
}
