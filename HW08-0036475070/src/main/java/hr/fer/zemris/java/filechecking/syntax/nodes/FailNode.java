package hr.fer.zemris.java.filechecking.syntax.nodes;

import hr.fer.zemris.java.filechecking.visitor.FCNodeVisitor;

/**
 * Cvor predstavlja naredbu fail.
 * 
 * @author Ivan Relic
 * 
 */
public class FailNode extends TestNode {

	/**
	 * Konstruktor. Prima informaciju je li test invertiran te koju poruku treba
	 * ispisati ako test padne.
	 * 
	 * @param inverted
	 *            true ako je invertiran, false inace
	 * @param errorMessage
	 *            poruka koju treba ispisati
	 */
	public FailNode(boolean inverted, MessageNode errorMessage) {
		super(inverted, errorMessage);
	}

	@Override
	public void acceptVisitor(FCNodeVisitor visitor) {
		visitor.visit(this);
	}
}
