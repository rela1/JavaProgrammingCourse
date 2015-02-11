package hr.fer.zemris.java.filechecking.syntax.nodes;

import hr.fer.zemris.java.filechecking.visitor.FCNodeVisitor;

import java.util.List;

/**
 * Cvor koji predstavlja poruku za ispis.
 * 
 * @author Ivan Relic
 * 
 */
public class MessageNode extends FCNode {

	private List<String> messageElements;
	boolean caseSensitivity;

	/**
	 * Konstruktor. Prima listu elemenata koji sacinjavaju poruku i informaciju
	 * je li poruka case sensitive ili ne.
	 * 
	 * @param messageElements
	 *            lista elemenata
	 * @param caseSensitivity
	 *            sensitivity poruke
	 */
	public MessageNode(List<String> messageElements, boolean caseSensitivity) {
		this.messageElements = messageElements;
		this.caseSensitivity = caseSensitivity;
	}

	/**
	 * Vraca listu elemenata koji sacinjavaju poruku.
	 * 
	 * @return lista elemenata
	 */
	public List<String> getMessageElements() {
		return messageElements;
	}

	/**
	 * Vraca informaciju je li poruka case sensitive ili ne.
	 * 
	 * @return true ako je, false ako nije
	 */
	public boolean getCaseSensitivity() {
		return caseSensitivity;
	}

	@Override
	public void acceptVisitor(FCNodeVisitor visitor) {
		// metoda se ne koristi
	}
}
