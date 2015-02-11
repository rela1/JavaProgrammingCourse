package hr.fer.zemris.java.filechecking.syntax.nodes;

import hr.fer.zemris.java.filechecking.visitor.FCNodeVisitor;

/**
 * Cvor za naredbu def.
 * 
 * @author Ivan Relic
 * 
 */
public class DefNode extends FCNode {

	private String variableName;
	private PathNode path;

	/**
	 * Konstruktor. Preuzima ime varijable i path koji ona predstavlja.
	 * 
	 * @param variableName
	 *            ime varijable
	 * @param path
	 *            path koji predstavlja
	 */
	public DefNode(String variableName, PathNode path) {
		this.variableName = variableName;
		this.path = path;
	}

	/**
	 * Vraca put koji cvor predstavlja.
	 * 
	 * @return put
	 */
	public PathNode getPath() {
		return path;
	}

	/**
	 * Vraca ime varijable koju cvor predstavlja.
	 * 
	 * @return ime varijable
	 */
	public String getVariableName() {
		return variableName;
	}

	@Override
	public void acceptVisitor(FCNodeVisitor visitor) {
		visitor.visit(this);
	}
}
