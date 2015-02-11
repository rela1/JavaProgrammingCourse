package hr.fer.zemris.java.filechecking.syntax.nodes;

import hr.fer.zemris.java.filechecking.visitor.FCNodeVisitor;

/**
 * Cvor koji predstavlja naredbu format
 * 
 * @author Ivan Relic
 * 
 */
public class FormatNode extends TestNode {

	private String format;

	/**
	 * Konstruktor. Prima informaciju je li test invertiran, format s kojim se
	 * mora usporedjivati i poruku koju mora ispisati ukoliko test padne.
	 * 
	 * @param inverted
	 *            true ako je invertiran, false inace
	 * @param format
	 *            format s kojim provjerava
	 * @param errorMessage
	 *            poruka koju treba ispisati
	 */
	public FormatNode(boolean inverted, String format, MessageNode errorMessage) {
		super(inverted, errorMessage);
		this.format = format;
	}

	/**
	 * Format s kojim ce test usporedjivati predani file.
	 * 
	 * @return string zapis formata
	 */
	public String getFormat() {
		return format;
	}

	@Override
	public void acceptVisitor(FCNodeVisitor visitor) {
		visitor.visit(this);
	}
}
