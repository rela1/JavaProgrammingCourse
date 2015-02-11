package hr.fer.zemris.java.filechecking.syntax.nodes;

import hr.fer.zemris.java.filechecking.visitor.FCNodeVisitor;

/**
 * Cvor koji predstavlja naredbu exists.
 * 
 * @author Ivan Relic
 * 
 */
public class ExistsNode extends TestNode {

	private boolean isDir;
	private PathNode path;

	/**
	 * Konstruktor. Prima message node i pohranjuje u svoju varijablu.
	 * 
	 * @param message
	 *            message node
	 */
	public ExistsNode(boolean inverted, boolean isDir, MessageNode message,
			PathNode path) {
		super(inverted, message);
		this.path = path;
		this.isDir = isDir;
	}

	/**
	 * Vraca predstavlja li test i path direktorij ili file.
	 * 
	 * @return
	 */
	public boolean isDir() {
		return isDir;
	}

	/**
	 * Vraca path node koji predstavlja path koji se testira.
	 * 
	 * @return path node
	 */
	public PathNode getPath() {
		return path;
	}

	@Override
	public void acceptVisitor(FCNodeVisitor visitor) {
		visitor.visit(this);
	}
}
