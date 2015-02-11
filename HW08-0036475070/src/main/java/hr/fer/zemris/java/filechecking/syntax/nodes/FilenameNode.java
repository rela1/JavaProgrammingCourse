package hr.fer.zemris.java.filechecking.syntax.nodes;

import hr.fer.zemris.java.filechecking.visitor.FCNodeVisitor;

/**
 * Cvor predstavlja naredbu filename
 * 
 * @author Ivan Relic
 * 
 */
public class FilenameNode extends TestNode {

	private MessageNode name;

	/**
	 * Konstruktor. Prima informaciju je li test invertiran, prima MessageNode
	 * koja predstavlja ime s kojim se usporedjuje i MessageNode koji
	 * predstavlja poruku koju treba ispisati ako test padne.
	 * 
	 * @param inverted
	 * @param name
	 * @param errorMessage
	 */
	public FilenameNode(boolean inverted, MessageNode name,
			MessageNode errorMessage) {
		super(inverted, errorMessage);
		this.name = name;
	}

	/**
	 * Vraca ime s kojim ce test usporedjivati.
	 * 
	 * @return message node s imenom
	 */
	public MessageNode getName() {
		return name;
	}

	@Override
	public void acceptVisitor(FCNodeVisitor visitor) {
		visitor.visit(this);
	}
}
