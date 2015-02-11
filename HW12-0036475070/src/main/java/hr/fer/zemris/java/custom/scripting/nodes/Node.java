package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Osnovna klasa za elemente koji izgraduju stabla parsiranog dokumenta.
 * 
 * @author Ivan Relic
 * 
 */
public class Node {

	// clanska varijabla za kolekciju djece
	private List<Node> tree;

	/**
	 * Dodaje zadano dijete u internu kolekciju klase.
	 * 
	 * @param child
	 *            Dijete koje se dodaje u listu.
	 */
	public void addChildNode(Node child) {

		// ako funkcija jos nije pokretana, prvo kreiraj kolekciju
		if (tree == null) {
			tree = new ArrayList<Node>();
		}
		// dodaj dijete u kolekciju
		tree.add(child);
	}

	/**
	 * Vraca broj direktne djece stabla.
	 * 
	 * @return Broj djece stabla.
	 */
	public int numberOfChildren() {
		if (tree == null) {
			return 0;
		} else {
			return tree.size();
		}
	}

	/**
	 * Vraca dijete sa odabranog indeksa.
	 * 
	 * @param index
	 *            Indeks s kojeg zelimo dohvat, u intervalu [0, size-1]
	 * @return Vraca dijete sa zadanog indeksa.
	 */
	public Node getChild(int index) {

		if (tree != null) {

			// vrati dijete sa zadanog indeksa, exception throw je vec ugradjen
			// u pozivanoj metodi
			return (Node) tree.get(index);
		}

		else {
			throw new IllegalArgumentException("Kolekcija nije stvorena!");
		}
	}

	/**
	 * Svaki razred koji nasljeđuje razred node mora ponuditi vlastitu
	 * implementaciju za prihvaćanje visitora.
	 * 
	 * @param visitor
	 *            visitor koji posjećuje node
	 */
	public void accept(INodeVisitor visitor) {
	}
}
