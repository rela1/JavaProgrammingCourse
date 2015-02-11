package hr.fer.zemris.java.filechecking.syntax.nodes;

import hr.fer.zemris.java.filechecking.visitor.FCNodeVisitor;

/**
 * Cvor koji predstavlja naredbu terminate.
 * 
 * @author Ivan Relic
 * 
 */
public class TerminateNode extends FCNode {

	@Override
	public void acceptVisitor(FCNodeVisitor visitor) {
		visitor.visit(this);
	}
}
