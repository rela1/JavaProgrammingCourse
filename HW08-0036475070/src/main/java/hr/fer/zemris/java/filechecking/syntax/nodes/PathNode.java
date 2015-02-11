package hr.fer.zemris.java.filechecking.syntax.nodes;

import hr.fer.zemris.java.filechecking.visitor.FCNodeVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Cvor koji sadrzi path do nekog filea / direktorija.
 * 
 * @author Ivan Relic
 * 
 */
public class PathNode extends FCNode {

	private List<String> pathElements;
	private boolean caseSensitivity;

	/**
	 * Konstruktor. Prima listu stringova koji sacinjavaju kompletan path
	 * (supstitucije varijabli, niz znakova razdvojen znakom '/' ili java pakete
	 * razdvojene znakom '.') i boolean varijablu koja oznacava je li path case
	 * sensitive ili insensitive.
	 */
	public PathNode(List<String> pathElements, boolean caseSensitivity) {
		this.pathElements = new ArrayList<>(pathElements);
		this.caseSensitivity = caseSensitivity;
	}

	/**
	 * Vraca listu elemenata patha ovog cvora.
	 * 
	 * @return lista elemenata patha.
	 */
	public List<String> getPathElements() {
		return new ArrayList<>(pathElements);
	}

	/**
	 * Vraca informaciju je li path case sensitive.
	 * 
	 * @return true ako je, false inace
	 */
	public boolean getCaseSensitivity() {
		return caseSensitivity;
	}

	@Override
	public void acceptVisitor(FCNodeVisitor visitor) {
		// metoda se ne koristi
	}
}
